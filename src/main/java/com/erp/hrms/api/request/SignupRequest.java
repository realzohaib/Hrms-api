package com.erp.hrms.api.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.*;

import com.erp.hrms.api.security.entity.JobLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author TA Admin
 *
 * 
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest  implements Serializable {
	
 private static final long serialVersionUID = 1L;

  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<JobLevel> jobLevel;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;


}
