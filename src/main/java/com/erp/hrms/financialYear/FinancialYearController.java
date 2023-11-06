package com.erp.hrms.financialYear;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinancialYearController {
	
	@Autowired
	private IFinancialYearDaoImpl service;
	
	//Date should be in String
	@PostMapping("/save-year")
	public ResponseEntity<?> saveFinancialYear(@RequestBody  FinancialYear year){
		try {
			service.saveYear(year);
			return ResponseEntity.status(HttpStatus.CREATED).body(new Boolean(true));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Boolean(false + e.getMessage()));
		}
	}
	
	@GetMapping("/get-year")
	public ResponseEntity<?> getFinancialYear(){
		try {
			List<FinancialYear> list = service.getFinancialYear();
			if(list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.accepted().body(list);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Boolean(false + e.getMessage()));
		}
	}
	
	@DeleteMapping("/delete-year")
	public ResponseEntity<?> deleteFinancialYear(@RequestBody  FinancialYear year){
		try {
			service.deleteYear(year.getFinancialYearId());
			return ResponseEntity.status(HttpStatus.CREATED).body(new Boolean(true));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Boolean(false + e.getMessage()));
		}
	}

}
