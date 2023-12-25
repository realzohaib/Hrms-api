package com.erp.hrms.excel.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.BackgroundCheck;
import com.erp.hrms.entity.BloodRelative;
import com.erp.hrms.entity.DrivingLicense;
import com.erp.hrms.entity.Education;
import com.erp.hrms.entity.EmpAchievement;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.OthersQualification;
import com.erp.hrms.entity.PassportDetails;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.PreviousEmployee;
import com.erp.hrms.entity.ProfessionalQualification;
import com.erp.hrms.entity.Trainingdetails;
import com.erp.hrms.entity.VisaDetail;

public class Helper {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static boolean hasExcelFormat(MultipartFile file) {
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<PersonalInfo> excelpersonalinfolist(InputStream is) throws IOException {
		List<PersonalInfo> personalInfoList = new ArrayList<>();
		try {
			try (Workbook workbook = new XSSFWorkbook(is)) {

				Sheet sheet = workbook.getSheet("Sheet1");
				System.out.println("got file");
				Iterator<Row> rows = sheet.iterator();

				int rowNumber = 0;
				while (rows.hasNext()) {
					Row currentRow = rows.next();

					// skip header
					if (rowNumber == 0) {
						rowNumber++;
						continue;
					}

					Iterator<Cell> cellsInRow = currentRow.iterator();

					PersonalInfo personalInfo = new PersonalInfo();
					PassportDetails psDetail = new PassportDetails();
					DrivingLicense license = new DrivingLicense();
					BloodRelative relative = new BloodRelative();
					VisaDetail visaDetail = new VisaDetail();

					Education education = new Education();
					List<Education> edc = new ArrayList<>();

					OthersQualification othersQualification = new OthersQualification();
					List<OthersQualification> otherqual = new ArrayList<>();

					ProfessionalQualification professionalQualification = new ProfessionalQualification();
					List<ProfessionalQualification> professionalqual = new ArrayList<>();

					PreviousEmployee previousEmployee = new PreviousEmployee();
					List<PreviousEmployee> previousemp = new ArrayList<>();

					EmpAchievement empAchievement = new EmpAchievement();
					List<EmpAchievement> empachivmnt = new ArrayList<>();

					BackgroundCheck backgroundCheck = new BackgroundCheck();

					Trainingdetails trainingdetails = new Trainingdetails();
					List<Trainingdetails> training = new ArrayList<>();

					JobDetails jobDetails = new JobDetails();
					List<JobDetails> details = new ArrayList<>();

					int cellIdx = 0;

					while (cellsInRow.hasNext()) {

						Cell currentCell = cellsInRow.next();

						String cellValue = "";
						if (currentCell.getCellType() == CellType.STRING) {
							cellValue = currentCell.getStringCellValue().trim();
						} else if (currentCell.getCellType() == CellType.NUMERIC) {
							cellValue = String.valueOf(currentCell.getNumericCellValue());
						} else if (currentCell.getCellType() == CellType.NUMERIC) {
							cellValue = String.valueOf(currentCell.getNumericCellValue());
						}
						System.out.println(cellValue);

						if (cellValue.isEmpty()) {
							cellIdx++;
							continue;
						}

						switch (cellIdx) {
						case 0:

							personalInfo.setNamePrefix(currentCell.getStringCellValue());
							break;

						case 1:

							personalInfo.setFirstName(currentCell.getStringCellValue());
							break;

						case 2:
							personalInfo.setMiddleName(currentCell.getStringCellValue());
							break;

						case 3:
							personalInfo.setLastName(currentCell.getStringCellValue());
							break;

						case 4:
							personalInfo.setFathersFirstName(currentCell.getStringCellValue());
							break;

						case 5:
							personalInfo.setFathersMiddleName(currentCell.getStringCellValue());
							break;

						case 6:
							personalInfo.setFathersLastName(currentCell.getStringCellValue());
							break;

						case 7:
							personalInfo.setDateOfBirth(currentCell.getStringCellValue());
							break;

						case 8:
							personalInfo.setAge((int) currentCell.getNumericCellValue());
							break;

						case 9:
							personalInfo.setMaritalStatus(currentCell.getStringCellValue());
							break;

						case 10:
							personalInfo.setPhoneCode(currentCell.getStringCellValue());
							break;

						case 11:
							personalInfo.setPersonalContactNo(currentCell.getStringCellValue());
							break;

						case 12:
							personalInfo.setEmail(currentCell.getStringCellValue());
							break;

						case 13:
							personalInfo.setCitizenship(currentCell.getStringCellValue());
							break;

						case 14:
							personalInfo.setPermanentResidenceCountry(currentCell.getStringCellValue());
							break;

						case 15:
							personalInfo.setPermanentResidentialAddress(currentCell.getStringCellValue());
							break;

						case 16:
							personalInfo.setBloodGroup(currentCell.getStringCellValue());
							break;

						case 17:
							personalInfo.setWorkedInUAE(currentCell.getStringCellValue());
							break;

						case 18:
							personalInfo.setEmiratesID(currentCell.getStringCellValue());
							break;

						case 19:
							personalInfo.setDegreeAttestation(currentCell.getStringCellValue());
							break;

						case 20:
							personalInfo.setHobbies(currentCell.getStringCellValue());
							break;

						case 21:
							personalInfo.setStatus(currentCell.getStringCellValue());
							break;
						case 22:
							personalInfo.setEmpStatus(currentCell.getStringCellValue());
							break;
						case 23:
							psDetail.setPassportIssuingCountry(currentCell.getStringCellValue());
							break;
						case 24:
							psDetail.setPassportNumber(currentCell.getStringCellValue());
							break;
						case 25:
							psDetail.setPassportExpiryDate(currentCell.getStringCellValue());
							break;
						case 26:
							license.setDrivinglicense(currentCell.getStringCellValue());
							break;
						case 27:
							license.setLicenseType(currentCell.getStringCellValue());
							break;
						case 28:
							license.setOwnvehicle(currentCell.getStringCellValue());
							break;
						case 29:
							relative.setRelativenamePrefix(currentCell.getStringCellValue());
							break;

						case 30:
							relative.setRFirstname(currentCell.getStringCellValue());
							break;

						case 31:
							relative.setRmiddlename(currentCell.getStringCellValue());
							break;

						case 32:
							relative.setRlastname(currentCell.getStringCellValue());
							break;

						case 33:
							relative.setRelationship(currentCell.getStringCellValue());
							break;

						case 34:
							relative.setRphoneCode(currentCell.getStringCellValue());
							break;

						case 35:
							relative.setRcontactno(currentCell.getStringCellValue());
							break;

						case 36:
							relative.setRaddress(currentCell.getStringCellValue());
							break;

						case 37:
							visaDetail.setVisaType(currentCell.getStringCellValue());
							break;

						case 38:
							visaDetail.setWorkVisaEmirateId(currentCell.getStringCellValue());
							break;

						case 39:
							visaDetail.setCategoryOfVisa(currentCell.getStringCellValue());
							break;

						case 40:
							visaDetail.setSiGlobalWorkVisaCompany(currentCell.getStringCellValue());
							break;

						case 41:
							visaDetail.setVisaIssueyDate(currentCell.getStringCellValue());
							break;

						case 42:
							visaDetail.setVisaExpiryDate(currentCell.getStringCellValue());
							break;

						case 43:
							education.setSecondaryIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 44:
							education.setSecondarymarksOrGrade(currentCell.getStringCellValue());
							break;

						case 45:
							education.setSecondaryyear(currentCell.getStringCellValue());
							break;

						case 46:
							education.setSeniorSecondaryIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 47:
							education.setSeniorSecondaryMarksOrGrade(currentCell.getStringCellValue());
							break;

						case 48:
							education.setSeniorSecondaryYear(currentCell.getStringCellValue());
							break;

						case 49:
							education.setGraduationIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 50:
							education.setGraduationMarksOrGrade(currentCell.getStringCellValue());
							break;

						case 51:
							education.setGraduationYear(currentCell.getStringCellValue());
							break;

						case 52:
							education.setPostGraduationIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 53:
							education.setPostGraduationMarksOrGrade(currentCell.getStringCellValue());
							break;

						case 54:
							education.setPostGraduationYear(currentCell.getStringCellValue());
							break;

						case 55:
							education.setDiplomaIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 56:
							education.setDiplomaMarksOrGrade(currentCell.getStringCellValue());
							break;

						case 57:
							education.setDiplomaYear(currentCell.getStringCellValue());
							break;

						case 58:
							othersQualification.setOthers(currentCell.getStringCellValue());
							break;

						case 59:
							othersQualification.setOthersIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 60:
							othersQualification.setOthersMarksOrGrade(currentCell.getStringCellValue());
							break;

						case 61:
							othersQualification.setOthersYear(currentCell.getStringCellValue());
							break;

						case 62:
							professionalQualification.setQualification(currentCell.getStringCellValue());
							break;

						case 63:
							professionalQualification.setIssuingAuthority(currentCell.getStringCellValue());
							break;

						case 64:
							professionalQualification.setGradingSystem(currentCell.getStringCellValue());
							break;

						case 65:
							professionalQualification.setYearOfQualification(currentCell.getStringCellValue());
							break;

						case 66:
							professionalQualification.setGrade(currentCell.getStringCellValue());
							break;

						case 67:
							previousEmployee.setCompanyName(currentCell.getStringCellValue());
							break;

						case 68:
							previousEmployee.setCompanyAddress(currentCell.getStringCellValue());
							break;

						case 69:
							previousEmployee.setDesignation(currentCell.getStringCellValue());
							break;

						case 70:
							previousEmployee.setDescription(currentCell.getStringCellValue());
							break;

						case 71:
							previousEmployee.setDateFrom(currentCell.getStringCellValue());
							break;

						case 72:
							previousEmployee.setDateTo(currentCell.getStringCellValue());
							break;

						case 73:
							previousEmployee.setPreviousManagerName(currentCell.getStringCellValue());
							break;

						case 74:
							previousEmployee.setPreviousManagerPhoneCode(currentCell.getStringCellValue());
							break;

						case 75:
							previousEmployee.setPreviousManagerContact(currentCell.getStringCellValue());
							break;

						case 76:
							previousEmployee.setPreviousHRName(currentCell.getStringCellValue());
							break;

						case 77:
							previousEmployee.setPreviousHRPhoneCode(currentCell.getStringCellValue());
							break;

						case 78:
							previousEmployee.setPreviousHRContact(currentCell.getStringCellValue());
							break;

						case 79:
							previousEmployee.setLastWithdrawnSalary((double) currentCell.getNumericCellValue());
							break;

						case 80:
							empAchievement.setAchievementRewardsName(currentCell.getStringCellValue());
							break;

						case 81:
							backgroundCheck.setStatus(currentCell.getStringCellValue());
							break;

						case 82:
							backgroundCheck.setDoneBy(currentCell.getStringCellValue());
							break;

						case 83:
							backgroundCheck.setInternalConcernedManager(currentCell.getStringCellValue());
							break;

						case 84:
							backgroundCheck.setExternalName(currentCell.getStringCellValue());
							break;

						case 85:
							backgroundCheck.setExternalPost(currentCell.getStringCellValue());
							break;

						case 86:
							backgroundCheck.setExternalCompanyName(currentCell.getStringCellValue());
							break;

						case 87:
							backgroundCheck.setExternalPhoneCode(currentCell.getStringCellValue());
							break;

						case 88:
							backgroundCheck.setExternalPhoneNo(currentCell.getStringCellValue());
							break;

						case 89:
							backgroundCheck.setManagerApproval(currentCell.getStringCellValue());
							break;

						case 90:
							backgroundCheck.setManagerName(currentCell.getStringCellValue());
							break;

						case 91:
							backgroundCheck.setRemark(currentCell.getStringCellValue());
							break;

						case 92:
							backgroundCheck.setAddressVerified(currentCell.getStringCellValue());
							break;

						case 93:
							backgroundCheck.setPreviousEmploymentStatusVerified(currentCell.getStringCellValue());
							break;

						case 94:
							backgroundCheck
									.setPreviousDesignationAndResponsibilityVerified(currentCell.getStringCellValue());
							break;

						case 95:
							backgroundCheck.setIdProofDocumentVerified(currentCell.getStringCellValue());
							break;

						case 96:
							backgroundCheck.setEducationalQualificationVerified(currentCell.getStringCellValue());
							break;

						case 97:
							backgroundCheck.setCriminalRecords(currentCell.getStringCellValue());
							break;

						case 98:
							backgroundCheck.setPunishmentForImprisonmentApproval(currentCell.getStringCellValue());
							break;

						case 99:
							trainingdetails.setTrainingType(currentCell.getStringCellValue());
							break;

						case 100:
							trainingdetails.setInHouseOutsource(currentCell.getStringCellValue());
							break;

						case 101:
							trainingdetails.setPaidUnpaid(currentCell.getStringCellValue());
							break;

						case 102:
							trainingdetails.setTrainingStartDate(currentCell.getStringCellValue());
							break;

						case 103:
							trainingdetails.setTrainingEndDate(currentCell.getStringCellValue());
							break;

						case 104:
							trainingdetails.setTrainerFeedback(currentCell.getStringCellValue());
							break;

						case 105:
							trainingdetails.setTrainerName(currentCell.getStringCellValue());
							break;

						case 106:
							trainingdetails.setTrainerPost(currentCell.getStringCellValue());
							break;

						case 107:
							trainingdetails.setTrainerDepartment(currentCell.getStringCellValue());
							break;

						case 108:
							trainingdetails.setTrainerPhoneCode(currentCell.getStringCellValue());
							break;

						case 109:
							trainingdetails.setTrainerPhoneNo(currentCell.getStringCellValue());
							break;

						case 110:
							jobDetails.setJobPostDesignation(currentCell.getStringCellValue());
							break;

						case 111:
							jobDetails.setCompanyEmailIdProvided(currentCell.getStringCellValue());
							break;

						case 112:
							jobDetails.setCompanyEmailId(currentCell.getStringCellValue());
							break;

						case 113:
							jobDetails.setJobLevel(currentCell.getStringCellValue());
							break;

						case 114:
							jobDetails.setPostedLocation(currentCell.getStringCellValue());
							break;

						case 115:
							jobDetails.setBasicPay(currentCell.getStringCellValue());
							break;

						case 116:
							jobDetails.setHouseRentAllowance(currentCell.getStringCellValue());
							break;

						case 117:
							jobDetails.setHouseRentAmount(currentCell.getStringCellValue());
							break;

						case 118:
							jobDetails.setFoodAllowance(currentCell.getStringCellValue());
							break;

						case 119:
							jobDetails.setFoodAllowanceAmount(currentCell.getStringCellValue());
							break;

						case 120:
							jobDetails.setVehicleAllowance(currentCell.getStringCellValue());
							break;

						case 121:
							jobDetails.setVehicleAllowanceAmount(currentCell.getStringCellValue());
							break;

						case 122:
							jobDetails.setUniformAllowance(currentCell.getStringCellValue());
							break;

						case 123:
							jobDetails.setUniformAllowanceAmount(currentCell.getStringCellValue());
							break;

						case 124:
							jobDetails.setTravellingAllowances(currentCell.getStringCellValue());
							break;

						case 125:
							jobDetails.setTravellingAllowancesAmount(currentCell.getStringCellValue());
							break;

						case 126:
							jobDetails.setEducationalAllowance(currentCell.getStringCellValue());
							break;

						case 127:
							jobDetails.setEducationalAllowanceAmount(currentCell.getStringCellValue());
							break;

						case 128:
							jobDetails.setOtherAllowance(currentCell.getStringCellValue());
							break;

						case 129:
							jobDetails.setOtherAllowanceAmount(currentCell.getStringCellValue());
							break;

						case 130:
							jobDetails.setVehicle(currentCell.getStringCellValue());
							break;

						case 131:
							jobDetails.setVehicleNumber(currentCell.getStringCellValue());
							break;

						case 132:
							jobDetails.setVehicleModelName(currentCell.getStringCellValue());
							break;

						case 133:
							jobDetails.setVehicleModelYear(currentCell.getStringCellValue());
							break;

						case 134:
							jobDetails.setIsVehicleNewOrPreowned(currentCell.getStringCellValue());
							break;

						case 135:
							jobDetails.setCashOrChipFacility(currentCell.getStringCellValue());
							break;

						case 136:
							jobDetails.setChipNumber(currentCell.getStringCellValue());
							break;

						case 137:
							jobDetails.setAccommodationYesOrNo(currentCell.getStringCellValue());
							break;

						case 138:
							jobDetails.setIsShredOrSeparate(currentCell.getStringCellValue());
							break;

						case 139:
							jobDetails.setIsExeutiveOrLabourFacility(currentCell.getStringCellValue());
							break;

						case 140:
							jobDetails.setElectricityAllocationYesOrNo(currentCell.getStringCellValue());
							break;

						case 141:
							jobDetails.setElectricityAllocationAmount(currentCell.getStringCellValue());
							break;

						case 142:
							jobDetails.setRentAllocationYesOrNo(currentCell.getStringCellValue());
							break;

						case 143:
							jobDetails.setRentAllocationAmount(currentCell.getStringCellValue());
							break;

						case 144:
							jobDetails.setMobileIssuedOrNot(currentCell.getStringCellValue());
							break;

						case 145:
							jobDetails.setSimIssuedOrNot(currentCell.getStringCellValue());
							break;

						case 146:
							jobDetails.setFlightFacilities(currentCell.getStringCellValue());
							break;

						case 147:
							jobDetails.setHowMuchTime(currentCell.getStringCellValue());
							break;

						case 148:
							jobDetails.setFamilyTicketsAlsoProvidedOrNot(currentCell.getStringCellValue());
							break;

						case 149:
							jobDetails.setOthersAccomandation(currentCell.getStringCellValue());
							break;

						case 150:
							jobDetails.setHealthInsuranceCoverage(currentCell.getStringCellValue());
							break;

						case 151:
							jobDetails.setMaximumAmountGiven(currentCell.getStringCellValue());
							break;

						case 152:
							jobDetails.setFamilyHealthInsuranceGivenOrNot(currentCell.getStringCellValue());
							break;

						case 153:
							jobDetails.setFamilyHealthInsuranceType(currentCell.getStringCellValue());
							break;

						case 154:
							jobDetails.setPunchingMachineYesOrNo(currentCell.getStringCellValue());
							break;

						case 155:
							jobDetails.setJoiningDate(currentCell.getStringCellValue());
							break;

						case 156:
//							jobDetails.setJobdepartment(currentCell.getStringCellValue());
							break;

						case 157:
							jobDetails.setReferredBy(currentCell.getStringCellValue());
							break;

						case 158:
							jobDetails.setByWhom(currentCell.getStringCellValue());
							break;
						default:
							break;
						}

						cellIdx++;
					}
					personalInfo.setPsDetail(psDetail);
					personalInfo.setLicense(license);
					personalInfo.setRelative(relative);
					personalInfo.setVisainfo(visaDetail);

					education.setPersonalinfo(personalInfo);
					edc.add(education);
					personalInfo.setEducations(edc);

					othersQualification.setPersonalinfo(personalInfo);
					otherqual.add(othersQualification);
					personalInfo.setOthersQualifications(otherqual);

					professionalQualification.setPersonalinfo(personalInfo);
					professionalqual.add(professionalQualification);
					personalInfo.setProfessionalQualifications(professionalqual);

//					empAchievement.setPreviousEmployee(previousEmployee);
//					empachivmnt.add(empAchievement);
//					previousEmployee.setEmpAchievements(empachivmnt);

					previousEmployee.setPersonalinfo(personalInfo);
					previousemp.add(previousEmployee);
					personalInfo.setOldEmployee(previousemp);

					backgroundCheck.setPersonalinfo(personalInfo);
					personalInfo.setBgcheck(backgroundCheck);

					trainingdetails.setPersonalinfo(personalInfo);
					training.add(trainingdetails);
					personalInfo.setTraining(training);

					jobDetails.setPersonalinfo(personalInfo);
					details.add(jobDetails);
					personalInfo.setJobDetails(details);

					personalInfoList.add(personalInfo);
					rowNumber++;
				}

			} // Workbook will be closed automatically here

			System.out.println("helper completed");
			return personalInfoList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}

}

