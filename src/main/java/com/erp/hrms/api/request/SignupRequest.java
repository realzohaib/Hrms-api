package com.erp.hrms.api.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

  private Set<String> roles;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;


}
