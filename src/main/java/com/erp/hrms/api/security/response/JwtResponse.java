package com.erp.hrms.api.security.response;

import java.io.Serializable;
import java.util.List;

import com.erp.hrms.EmpDesignation.REQandRES.CurrentRes;
import com.erp.hrms.entity.PersonalInfo;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class JwtResponse implements Serializable {

	
private static final long serialVersionUID = 1L;
private String token;
private String type = "Bearer";
private String expieryTime;
private PersonalInfo info;
private List<CurrentRes>currentDesignationAndTask;
  
  public JwtResponse(String accessToken) {
	  
	  this.token = accessToken;
  }

  public JwtResponse() {
	  
  }
}
