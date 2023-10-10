/**
 * 
 */
package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import lombok.Data;

/**
 * @author MECAPS
 *
 */
@Data
@Embeddable
public class VisaDetail {

	@Column(name = "visa_type")
	private String VisaType;
	@Column(name = "emirates_id")
	private String workVisaEmirateId;

	@Column(name = "category_Of_Visa")
	private String categoryOfVisa;
	@Column(name = "si_global_company")
	private String siGlobalWorkVisaCompany;
	@Column(name = "visa_issue_date")
	private String visaIssueyDate;
	@Column(name = "visa_expiry_date")
	private String visaExpiryDate;

	private boolean firstVisaEmailSend;
	private boolean secondVisaEmailSend;

	private boolean firstContinuouslyVisaEmailSend;
	private boolean secondContinuouslyVisaEmailSend;
	private boolean thirdContinuouslyVisaEmailSend;
	private boolean fourContinuouslyVisaEmailSend;

	@Column(name = "visa_docs", length = 2097152)
	@Lob
	private byte[] visaDocs;

}
