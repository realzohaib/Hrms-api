/**
 * 
 */
package com.erp.hrms.api.security.utll;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.erp.hrms.api.security.response.JwtResponse;
import com.erp.hrms.api.service.impl.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author TA Admin
 *
 * 
 */

@Component
public class JwtTokenUtill {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtill.class);

	@Value("${hrms.api.jwtSecret}")
	private String jwtSecret;

	@Value("${hrms.api.jwtExpirationMs}")
	private int jwtExpirationMs;

//	public JwtResponse generateJwtToken(Authentication authentication) {
//		JwtResponse jwtResponse = new JwtResponse();
//
//		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//
//	    long expirationTime = System.currentTimeMillis() + jwtExpirationMs;
//
//		String token = Jwts.builder()
//				.setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
//				//.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//		        .setExpiration(new Date(expirationTime))
//				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
//
//		jwtResponse.setToken(token);	
//		jwtResponse
//				.setExpieryTime(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration());
//		jwtResponse.setType("Bearer");
//
//		return jwtResponse;
//	}
	
	public JwtResponse generateJwtToken(Authentication authentication) {
	    JwtResponse jwtResponse = new JwtResponse();

	    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

	    long expirationTime = System.currentTimeMillis() + jwtExpirationMs;

	    String token = Jwts.builder()
	        .setSubject(userPrincipal.getUsername())
	        .claim("email", userPrincipal.getEmail())
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(expirationTime))
	        .signWith(SignatureAlgorithm.HS512, jwtSecret)
	        .compact();

	    jwtResponse.setToken(token);
//	    jwtResponse.setExpieryTime(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration());
	    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	    Date et = claims.getExpiration();

	    // Assuming you have a DateFormat or SimpleDateFormat
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	    String formattedExpirationTime = dateFormat.format(et);

	    jwtResponse.setExpieryTime(formattedExpirationTime);

	    jwtResponse.setType("Bearer");

	    return jwtResponse;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
