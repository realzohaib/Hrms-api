package com.erp.hrms.entity.notificationhelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationHelper {

	private Long employeeId;
	private String namePrefix;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String VisaType;
	private String visaIssueyDate;
	private String visaExpiryDate;
	private String notificationType;
}
