package com.ppi.utility.importer.service;

import com.ppi.utility.importer.model.CaseMaster;
import com.ppi.utility.importer.repository.CaseMasterRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service class responsible for parsing Excel files and inserting data into the database.
 */
@Service
public class ExcelService {

    private final CaseMasterRepository caseMasterRepository;

    @Autowired
    public ExcelService(CaseMasterRepository caseMasterRepository) {
        this.caseMasterRepository = caseMasterRepository;
    }

    /**
     * Processes the given Excel file, extracts data, and saves it to the database.
     *
     * @param excelFile The Excel file to process.
     * @return A message indicating the success or failure of the operation.
     * @throws IOException If there's an error reading the file.
     * @throws Exception   For any other processing errors.
     */
    @Transactional // Ensures that all database operations within this method are part of a single transaction.
    public String processExcelFile(File excelFile) throws IOException, Exception {
        List<CaseMaster> caseMasters = new ArrayList<>();
        Timestamp submittedTs = null;

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) { // For .xlsx files

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // 1. Parse Cell D6 for SUBMITTED_TS
            Row rowD6 = sheet.getRow(5); // D6 is row index 5 (0-indexed)
            if (rowD6 == null) {
                throw new IllegalArgumentException("Excel file is empty or does not have data in row 6 (D6).");
            }
            Cell cellD6 = rowD6.getCell(3); // D is column index 3 (0-indexed)

            if (cellD6 != null && cellD6.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellD6)) {
                Date dateD6 = cellD6.getDateCellValue();
                submittedTs = new Timestamp(dateD6.getTime());
            } else {
                throw new IllegalArgumentException("Cell D6 must contain a valid date/time value.");
            }

            // 2. Parse Rows 10 onward (until blank) -> Columns B to I
            // Rows 10 onward means starting from row index 9 (0-indexed)
            for (int rowIndex = 9; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);

                // Stop if the row is null or if critical cells (e.g., B, C, D, E) are blank
                // Check if the first cell (B) is blank to determine end of data
                if (currentRow == null || isRowEmpty(currentRow, 1, 8)) { // Check columns B to I (index 1 to 8)
                    break;
                }

                CaseMaster caseMaster = new CaseMaster();
                caseMaster.setSubmittedTs(submittedTs); // Set the SUBMITTED_TS from D6

                // Fetch data from Excel columns B to I
                // B: THIRD_PARTY_REFERENCE_1 (index 1)
                // C: THIRD_PARTY_REFERENCE_2 (index 2)
                // D: LAST_NAME (index 3)
                // E: FIRST_NAME (index 4)
                // F: DATE_OF_BIRTH (index 5)
                // G: POST_CODE (index 6)
                // H: (not specified, but within B-I range, will be skipped or set to null if not used)
                // I: (not specified, but within B-I range, will be skipped or set to null if not used)

                // Column B: THIRD_PARTY_REFERENCE_1
                caseMaster.setThirdPartyReference1(getCellValueAsString(currentRow.getCell(1)));
                // Column C: THIRD_PARTY_REFERENCE_2
                caseMaster.setThirdPartyReference2(getCellValueAsString(currentRow.getCell(2)));
                // Column D: LAST_NAME
                caseMaster.setLastName(getCellValueAsString(currentRow.getCell(3)));
                // Column E: FIRST_NAME
                caseMaster.setFirstName(getCellValueAsString(currentRow.getCell(4)));

                // Column F: DATE_OF_BIRTH
                Cell dobCell = currentRow.getCell(5);
                if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
                    Date dobDate = dobCell.getDateCellValue();
                    caseMaster.setDateOfBirth(dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                } else {
                    // Handle cases where DOB is missing or not in date format
                    caseMaster.setDateOfBirth(null); // Or throw an error if DOB is mandatory
                }

                // Column G: POST_CODE
                caseMaster.setPostCode(getCellValueAsString(currentRow.getCell(6)));

                // Columns H and I are not mapped to specific fields based on your schema.
                // If they contain data, they will be ignored as no corresponding setters exist.

                caseMasters.add(caseMaster);
            }

            if (caseMasters.isEmpty()) {
                return "No valid data rows found in the Excel file from row 10 onwards.";
            }

            // Save all entities in a single batch transaction
            caseMasterRepository.saveAll(caseMasters);

            return "Upload successful! Processed " + caseMasters.size() + " records.";

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            throw new IOException("Failed to read Excel file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.err.println("Excel parsing error: " + e.getMessage());
            throw new Exception("Excel file format error: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during Excel processing or database insertion: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to get cell value as String, handling different cell types.
     *
     * @param cell The Excel cell.
     * @return The string representation of the cell value, or null if cell is null or blank.
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    // If it's a date formatted numeric cell, convert to string representation of date
                    yield cell.getDateCellValue().toString(); // Or format it as needed
                } else {
                    // For general numbers, return as string to avoid scientific notation issues
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                // Evaluate formula cells to get their result
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                yield switch (cellValue.getCellType()) {
                    case STRING -> cellValue.getStringValue();
                    case NUMERIC -> String.valueOf(cellValue.getNumberValue());
                    case BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                    case ERROR -> "ERROR";
                    default -> null;
                };
            }
            case BLANK, ERROR -> null;
            default -> null;
        };
    }

    /**
     * Checks if a row is effectively empty within a given range of columns.
     *
     * @param row The row to check.
     * @param startColIndex The starting column index (inclusive).
     * @param endColIndex The ending column index (inclusive).
     * @return true if all cells in the specified range are null or blank, false otherwise.
     */
    private boolean isRowEmpty(Row row, int startColIndex, int endColIndex) {
        if (row == null) {
            return true;
        }
        for (int i = startColIndex; i <= endColIndex; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String cellValue = getCellValueAsString(cell);
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    return false; // Found non-empty cell
                }
            }
        }
        return true; // All cells in range are empty
    }
}
