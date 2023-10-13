package com.erp.hrms.entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.erp.hrms.entity.form.LeaveApproval;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class PersonalInfo {
	@Id
	@Column(name = "employee_Id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeIdGenerator")
	@SequenceGenerator(name = "employeeIdGenerator", sequenceName = "employee_id_seq", allocationSize = 1, initialValue = 1001)
	private Long employeeId;
	@Column(name = "name_prefix")
	private String namePrefix;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "father_first_name")
	private String fathersFirstName;

	@Column(name = "father_middle_name")
	private String fathersMiddleName;

	@Column(name = "father_last_name")
	private String fathersLastName;

	@Column(name = "dob")
	private String dateOfBirth;

	private Integer age;

	@Column(name = "marital_status ")
	private String maritalStatus;

	@Column(name = "country_Code")
	private String phoneCode;

	@Column(name = "personal_contact_no")
	private String personalContactNo;

	@Lob
	@Column(name = "passport_size_photo ", length = 2147483647)
	private byte[] passportSizePhoto;

	@Column(name = "email_id")
	private String email;

	@Column(name = "citizenship")
	private String citizenship;

	@Column(name = "id_Scan", length = 2147483647)
	@Lob
	private byte[] OtherIdProofDoc;

	@Column(name = "permanent_residence_country")
	private String permanentResidenceCountry;

	@Column(name = "permanent_residence_address")
	private String permanentResidentialAddress;

	@Column(name = "blood_group")
	private String bloodGroup;

	@Column(name = "worked_in_uae_earlier ")
	private String workedInUAE;

	@Column(name = "old_emirates_id")
	private String EmiratesID;

	@Column(name = "degree_attestation")
	private String DegreeAttestation;

	@Column(name = "hobbies")
	private String hobbies;

	private String status;

	private String empStatus;

	private PassportDetails psDetail;

	private DrivingLicense license;

	private BloodRelative relative;

	private VisaDetail visainfo;

	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	@JsonManagedReference
	private List<Education> educations;

	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	@JsonManagedReference
	private List<OthersQualification> othersQualifications;

	@JsonManagedReference
	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	private List<ProfessionalQualification> professionalQualifications;

	@JsonManagedReference
	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	private List<PreviousEmployee> oldEmployee;

	@JsonManagedReference
	@OneToOne(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	private BackgroundCheck bgcheck;

	@JsonManagedReference
	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	private List<Trainingdetails> training;

	@OneToMany(mappedBy = "personalinfo")
	@Cascade(CascadeType.ALL)
	@JsonManagedReference
	private List<JobDetails> jobDetails;

	@PostLoad
	private void calculateAge() {
		if (this.dateOfBirth != null && !this.dateOfBirth.isEmpty()) {

			LocalDate dob = LocalDate.parse(this.dateOfBirth, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			LocalDate currentDate = LocalDate.now();
			Period agePeriod = Period.between(dob, currentDate);
			this.age = agePeriod.getYears();
		}
	}


}
