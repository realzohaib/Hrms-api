/**
 * 
 */
package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

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

	private String visaDocs;

	@Transient
	private byte[] visaDocsData;

	public void setVisaDocsData(byte[] visaDocsData) {
		validateAndSetData(visaDocsData, "Visa Docs Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 2000 * 1024) {
			if ("Visa Docs Data".equals(dataType)) {
				this.visaDocsData = data;
			}
		} else {
			throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
		}
	}

}
