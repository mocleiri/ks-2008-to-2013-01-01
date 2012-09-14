package com.sigmasys.kuali.ksa.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.ObjectUtils;

/**
 * This class represents a subclass of the KeyPair class that is capable of
 * referring to a certain time period. Per the existing design, 
 * "For example, for a simple three-term system, the periods might be stored as 2012-1, 2012-2, 2012-3."
 * 
 * @author Sergey
 * @version 1.0
 */
@Entity
@DiscriminatorValue(KeyPairType.PERIOD_KEY_PAIR_CODE)
public class PeriodKeyPair extends KeyPair {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Due to the need to store different values by period, this is a key
	 * pair value that also has a period indicator. Set at instantiation.
	 */
	private PeriodType period;

	
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "KSSA_KYPR_PERIOD_TYPE",
			joinColumns = {
        			@JoinColumn(name = "KYPR_ID_FK")
			},
			inverseJoinColumns = {
        			@JoinColumn(name = "PERIOD_TYPE_ID_FK")
			}
    )
	public PeriodType getPeriod() {
		return period;
	}

	public void setPeriod(PeriodType period) {
		this.period = period;
	}
	
	public boolean equals(Object o) {
		if (o instanceof PeriodKeyPair) {
			PeriodKeyPair pkpAnother = (PeriodKeyPair)o;
			
			return super.equals(o) && ObjectUtils.equals(pkpAnother.period, this.period);
		}
		
		return false;
	}
	
	public int hashCode() {
		return 31 * (super.hashCode() + ObjectUtils.hashCode(period));
	}
}
