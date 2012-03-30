package com.sigmasys.kuali.ksa.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Electronic contact
 * <p/>
 * <p/>
 * User: ivanovm
 * Date: 3/13/12
 * Time: 3:56 PM
 */
@Entity
@Table(name = "KSSA_ELECTRONIC_CONTACT")
public class ElectronicContact implements Identifiable {

    /**
     * Person name ID
     */
    private Long id;

    /**
     * KIM email address type
     */
    private String kimEmailAddressType;

    /**
     * KIM phone type
     */
    private String kimPhoneType;

    /**
     * Email address
     */
    private String emailAddress;

    /**
     * Last name
     */
    private String phoneCountry;

    /**
     * Suffix
     */
    private String phoneNumber;

    /**
     * Title
     */
    private String phoneExtension;

    /**
     * Creator user ID
     */
    private String creatorId;

    /**
     * Editor user ID
     */
    private String editorId;

    /**
     * Timestamp
     */
    private Date lastUpdate;

    /**
     * Reference to the corresponding account
     */
    private Account account;

    /**
     * Is default
     */
    private Boolean isDefault;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_CONTACT",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "ELECTRONIC_CONTACT_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_CONTACT")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "KIM_EMAIL_ADDRESS_TYPE", length = 45)
    public String getKimEmailAddressType() {
        return kimEmailAddressType;
    }

    public void setKimEmailAddressType(String kimEmailAddressType) {
        this.kimEmailAddressType = kimEmailAddressType;
    }

    @Column(name = "KIM_PHONE_TYPE", length = 45)
    public String getKimPhoneType() {
        return kimPhoneType;
    }

    public void setKimPhoneType(String kimPhoneType) {
        this.kimPhoneType = kimPhoneType;
    }

    @Column(name = "EMAIL_ADDRESS", length = 255)
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = "PHONE_COUNTRY", length = 5)
    public String getPhoneCountry() {
        return phoneCountry;
    }

    public void setPhoneCountry(String phoneCountry) {
        this.phoneCountry = phoneCountry;
    }

    @Column(name = "PHONE_NUMBER", length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "PHONE_EXTN", length = 10)
    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    @Column(name = "CREATOR_ID", length = 45)
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "EDITOR_ID", length = 45)
    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    @Column(name = "LAST_UPDATE")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACNT_ID_FK")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @org.hibernate.annotations.Type(type="yes_no")
    @Column(name = "IS_DEFAULT")
    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
