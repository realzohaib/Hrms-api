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

	private boolean visaEmailSend20and60daysBefore;
	private boolean VisaEmailSend10and30daysBefore;

	private boolean visaEmailSend4daysBefore;
	private boolean visaEmailSend3daysBefore;
	private boolean visaEmailSend2daysBefore;
	private boolean visaEmailSend1dayBefore;

	@Column(name = "visa_docs", length = 2147483647)
	@Lob
	private byte[] visaDocs;

}
