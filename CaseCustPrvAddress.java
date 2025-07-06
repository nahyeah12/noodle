package com.ppi.utility.importer.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime; // For CURRENT TS

/**
 * JPA Entity for the CASE_CUST_PRV_ADDRESS table.
 */
@Entity
@Table(name = "CASE_CUST_PRV_ADDRESS")
public class CaseCustPrvAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prv_addr_seq_generator")
    @SequenceGenerator(name = "prv_addr_seq_generator", sequenceName = "PRV_ADDR_SEQ", allocationSize = 1) // Corrected sequence name
    @Column(name = "PRV_ADDR_SEQ_ID", nullable = false)
    private Long prvAddrSeqId; // NUMBER(20)

    @Column(name = "UK_FLG", length = 1)
    private Character ukFlg; // CHAR(1)

    @Column(name = "EXRTRACT_IND", length = 1)
    private Character extractInd; // CHAR(1)

    @Column(name = "ORACLE_LOAD_TIMESTAMP")
    private Timestamp oracleLoadTimestamp; // TIMESTAMP(6)

    @Column(name = "CASE_ID", length = 20)
    private String caseId; // VARCHAR(20) - Foreign key to CASE_MASTER_TBL (conceptually)

    /**
     * Default constructor. Sets default values for certain fields upon instantiation.
     */
    public CaseCustPrvAddress() {
        this.ukFlg = '1'; // Default to '1' as per requirements
        this.extractInd = 'N'; // Default to 'N' as per requirements
        this.oracleLoadTimestamp = Timestamp.valueOf(LocalDateTime.now()); // Set current timestamp
    }

    // Getters and Setters

    public Long getPrvAddrSeqId() {
        return prvAddrSeqId;
    }

    public void setPrvAddrSeqId(Long prvAddrSeqId) {
        this.prvAddrSeqId = prvAddrSeqId;
    }

    public Character getUkFlg() {
        return ukFlg;
    }

    public void setUkFlg(Character ukFlg) {
        this.ukFlg = ukFlg;
    }

    public Character getExtractInd() {
        return extractInd;
    }

    public void setExtractInd(Character extractInd) {
        this.extractInd = extractInd;
    }

    public Timestamp getOracleLoadTimestamp() {
        return oracleLoadTimestamp;
    }

    public void setOracleLoadTimestamp(Timestamp oracleLoadTimestamp) {
        this.oracleLoadTimestamp = oracleLoadTimestamp;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @Override
    public String toString() {
        return "CaseCustPrvAddress{" +
                "prvAddrSeqId=" + prvAddrSeqId +
                ", ukFlg=" + ukFlg +
                ", extractInd=" + extractInd +
                ", oracleLoadTimestamp=" + oracleLoadTimestamp +
                ", caseId='" + caseId + '\'' +
                '}';
    }
}
