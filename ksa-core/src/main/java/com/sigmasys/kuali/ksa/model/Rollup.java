package com.sigmasys.kuali.ksa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * Transaction is an abstract class, used to generate different types of transactions within the system.
 *
 * @author Paul Heald, Michael Ivanov
 */
@Entity
@Table(name="KSSA_TRANSACTION")
public class Rollup {

    /**
     * The unique transaction identifier for the KSA product.
     */
	@Id
	@Column(name = "ID", nullable = false, unique = true, updatable = false)
    @TableGenerator(name="TABLE_GEN", 
        table="SEQUENCE_TABLE", 
        pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_VALUE", 
        pkColumnValue="ROLLUP_SEQ")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="TABLE_GEN")
    protected Long id;

	/**
     * Rollup name
     */
	@Column(name = "NAME")
    private String name;
	
	/**
     * Creator user ID
     */
	@Column(name = "CREATOR_ID")
    private String creatorId;

	/**
     * Editor user ID
     */
	@Column(name = "EDITOR_ID")
    private String editorId;
	
	/**
     * Editor user ID
     */
	@Column(name = "LAST_UPDATE")
    private Date lastUpdate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getEditorId() {
		return editorId;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
  
}
	


