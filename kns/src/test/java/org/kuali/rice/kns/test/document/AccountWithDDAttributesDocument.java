/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.test.document;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.kuali.rice.kns.document.SessionDocument;
import org.kuali.rice.kns.document.TransactionalDocumentBase;
import org.kuali.rice.kns.util.KualiDecimal;

/**
 * This is a test transactional document for use with testing data dictionary searchable attributes on the doc search. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="ACCT_DD_ATTR_DOC")
@org.hibernate.annotations.TypeDef(
			name="rice_decimal",
			typeClass=org.kuali.rice.kns.util.HibernateKualiDecimalFieldType.class
	)
public class AccountWithDDAttributesDocument extends TransactionalDocumentBase implements SessionDocument {
	private static final long serialVersionUID = 174220131121010870L;
	
	@Column(name="ACCT_NUM")
	private Integer accountNumber;
	@Column(name="ACCT_OWNR")
	private String accountOwner;
	@Type(type="rice_decimal")
	@Column(name="ACCT_BAL")
	private KualiDecimal accountBalance;
	@Column(name="ACCT_OPN_DAT")
	private Date accountOpenDate;
	@Column(name="ACCT_UPDATE_DT_TM")
	private Timestamp accountUpdateDateTime;
	@Column(name="ACCT_STAT")
	private String accountState;
	@Type(type="yes_no")
	@Column(name="ACCT_AWAKE")
	private boolean accountAwake;

    @Override
    protected LinkedHashMap<?,?> toStringMapper() {
        LinkedHashMap<String, String> meMap = new LinkedHashMap<String, String>();
        meMap.put("accountOwner", getAccountOwner());
        meMap.put("accountOpenDate", getAccountOpenDate().toString());
        meMap.put("accountState", getAccountState());
        return meMap;
    }
	
	public Integer getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountOwner() {
		return this.accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public KualiDecimal getAccountBalance() {
		return this.accountBalance;
	}

	public void setAccountBalance(KualiDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Date getAccountOpenDate() {
		return this.accountOpenDate;
	}

	public void setAccountOpenDate(Date accountOpenDate) {
		this.accountOpenDate = accountOpenDate;
	}

	public String getAccountState() {
		return this.accountState;
	}

	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	public String getAccountStateMultiselect() {
		return this.accountState;
	}
	
	/**
	 * @return the accountUpdateDateTime
	 */
	public Timestamp getAccountUpdateDateTime() {
		return this.accountUpdateDateTime;
	}

	/**
	 * @param accountUpdateDateTime the accountUpdateDateTime to set
	 */
	public void setAccountUpdateDateTime(Timestamp accountUpdateDateTime) {
		this.accountUpdateDateTime = accountUpdateDateTime;
	}

	/**
	 * @return the accountAwake
	 */
	public boolean isAccountAwake() {
		return this.accountAwake;
	}

	/**
	 * @param accountAwake the accountAwake to set
	 */
	public void setAccountAwake(boolean accountAwake) {
		this.accountAwake = accountAwake;
	}
	
}
