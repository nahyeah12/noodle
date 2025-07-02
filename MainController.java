package com.ppi.utility.importer;

import com.ppi.utility.importer.service.ExcelService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * JavaFX Controller for the main-view.fxml.
 * Handles UI interactions, file selection, and delegates Excel processing to ExcelService.
 */
@Component // Mark as a Spring component
@Scope("prototype") // Important: JavaFX controllers are instantiated by FXML loader, not Spring.
                    // Using prototype scope ensures a new instance is created and dependencies are injected.
public class MainController {

    @FXML
    private Button uploadButton;

    @FXML
    private Label messageLabel;

    private final ExcelService excelService;

    // Use constructor injection for Spring-managed services
    @Autowired
    public MainController(ExcelService excelService) {
        this.excelService = excelService;
    }

    /**
     * Initializes the controller. This method is automatically called after the FXML fields are injected.
     */
    @FXML
    public void initialize() {
        messageLabel.setText("Click 'Upload File' to select an Excel document.");
    }

    /**
     * Handles the action when the "Upload File" button is clicked.
     */
    @FXML
    private void onUploadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Get the current stage to show the file chooser dialog
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            messageLabel.setText("Upload successful. Processing...");
            uploadButton.setDisable(true); // Disable button during processing

            // Create a Task to perform the long-running operation in a background thread
            Task<String> processTask = new Task<>() {
                @Override
                protected String call() throws Exception {
                    // This code runs in a background thread
                    return excelService.processExcelFile(selectedFile);
                }

                @Override
                protected void succeeded() {
                    // This code runs on the JavaFX Application Thread
                    messageLabel.setText(getValue()); // Display success message
                    uploadButton.setDisable(false); // Re-enable button
                }

                @Override
                protected void failed() {
                    // This code runs on the JavaFX Application Thread
                    Throwable exception = getException();
                    String errorMessage = "Error: " + (exception != null ? exception.getMessage() : "Unknown error.");
                    messageLabel.setText(errorMessage); // Display error message
                    System.err.println("Error processing Excel file: " + errorMessage);
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    uploadButton.setDisable(false); // Re-enable button
                }
            };

            // Start the task in a new thread
            new Thread(processTask).start();

        } else {
            messageLabel.setText("File upload cancelled.");
        }
    }
}
