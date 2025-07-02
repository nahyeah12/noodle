package com.ppi.utility.importer.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate; // For DATE_OF_BIRTH
import java.time.LocalDateTime; // For SUBMITTED_TS (if using LocalDateTime directly)

/**
 * JPA Entity for the CASE_MASTER_TBL table.
 * Maps Excel data to database columns and handles default values.
 */
@Entity
@Table(name = "CASE_MASTER_TBL")
public class CaseMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_id_seq_generator")
    @SequenceGenerator(name = "case_id_seq_generator", sequenceName = "CASE_ID_SEQ", allocationSize = 1)
    @Column(name = "CASE_ID", length = 20, nullable = false)
    private String caseId;

    @Column(name = "CHANNEL_ID", length = 3)
    private String channelId;

    @Column(name = "USER_ID", length = 50, nullable = false)
    private String userId;

    @Column(name = "SUBMITTED_TS")
    private Timestamp submittedTs; // Use java.sql.Timestamp for Oracle TIMESTAMP(6)

    @Column(name = "CASE_TYPE", length = 20)
    private String caseType;

    @Column(name = "CASE_STATUS_ID")
    private Long caseStatusId; // Use Long for NUMBER type

    @Column(name = "IS_CURRENT_UK_RESIDENT", length = 1)
    private Character isCurrentUkResident;

    @Column(name = "TITLE_CODE", length = 35)
    private String titleCode;

    @Column(name = "FIRST_NAME", length = 35)
    private String firstName;

    @Column(name = "MIDDLE_NAME", length = 35)
    private String middleName;

    @Column(name = "LAST_NAME", length = 35)
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth; // Use java.time.LocalDate for Oracle DATE

    @Column(name = "POST_CODE", length = 37)
    private String postCode;

    @Column(name = "THIRD_PARTY_REFERENCE_1", length = 50)
    private String thirdPartyReference1;

    @Column(name = "THIRD_PARTY_REFERENCE_2", length = 50)
    private String thirdPartyReference2;

    /**
     * Default constructor. Sets default values for certain fields upon instantiation.
     */
    public CaseMaster() {
        this.channelId = "10"; // Default value as per requirements
        this.userId = "SYS"; // Default value as per requirements
        this.caseType = "QRY"; // Default value as per requirements
        this.caseStatusId = 8L; // Default value as per requirements, L for Long literal
        this.isCurrentUkResident = 'Y'; // Default value as per requirements
        this.titleCode = null; // Default to null as per requirements
        this.middleName = null; // Default to null as per requirements
    }

    // Getters and Setters for all fields

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getSubmittedTs() {
        return submittedTs;
    }

    public void setSubmittedTs(Timestamp submittedTs) {
        this.submittedTs = submittedTs;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public Long getCaseStatusId() {
        return caseStatusId;
    }

    public void setCaseStatusId(Long caseStatusId) {
        this.caseStatusId = caseStatusId;
    }

    public Character getIsCurrentUkResident() {
        return isCurrentUkResident;
    }

    public void setIsCurrentUkResident(Character isCurrentUkResident) {
        this.isCurrentUkResident = isCurrentUkResident;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getThirdPartyReference1() {
        return thirdPartyReference1;
    }

    public void setThirdPartyReference1(String thirdPartyReference1) {
        this.thirdPartyReference1 = thirdPartyReference1;
    }

    public String getThirdPartyReference2() {
        return thirdPartyReference2;
    }

    public void setThirdPartyReference2(String thirdPartyReference2) {
        this.thirdPartyReference2 = thirdPartyReference2;
    }

    @Override
    public String toString() {
        return "CaseMaster{" +
                "caseId='" + caseId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", userId='" + userId + '\'' +
                ", submittedTs=" + submittedTs +
                ", caseType='" + caseType + '\'' +
                ", caseStatusId=" + caseStatusId +
                ", isCurrentUkResident=" + isCurrentUkResident +
                ", titleCode='" + titleCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", postCode='" + postCode + '\'' +
                ", thirdPartyReference1='" + thirdPartyReference1 + '\'' +
                ", thirdPartyReference2='" + thirdPartyReference2 + '\'' +
                '}';
    }
}
