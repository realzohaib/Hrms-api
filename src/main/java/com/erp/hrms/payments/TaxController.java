package com.erp.hrms.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;

@RestController
public class TaxController {

	@Autowired
	private TaxService service;
	
	@PostMapping("/save-Taxpay")
	public ResponseEntity<?>saveTaxAmount(@RequestBody Tax tax){
		try {
			service.savetax(tax);
			return ResponseEntity.ok(new MessageResponse("Tax saved"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to save " + e.getMessage()));
		}
		
	}
	
	@GetMapping("/get-TaxDetails")
	public ResponseEntity<?>getovertimeAmount(){
		try {
			return ResponseEntity.ok().body(service.getTax());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to get " + e.getMessage()));
		}
		
	}
	
	@DeleteMapping("/delete-taxDetails/{id}")
	public ResponseEntity<?>deletetax(@PathVariable long id){
		try {
			service.deleteTax(id);
			return ResponseEntity.ok().body(new MessageResponse("Tax Details deleted"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to dekete " + e.getMessage()));
		}
		
	}
	
}
