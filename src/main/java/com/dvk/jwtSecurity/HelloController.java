/**
 * 
 */
package com.dvk.jwtSecurity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dvk.jwtSecurity.model.AuthenticationRequest;
import com.dvk.jwtSecurity.model.AuthenticationResponse;

/**
 * @author User
 *
 */
@RestController
public class HelloController {
	
	Log log = LogFactory.getLog(HelloController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtutil;
	
	@Autowired
	private MyUserDetailsService myuserservice;
	
	@RequestMapping("/hello")	
	public String getHello(){
		return "Hello world from Jwt";
	}
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request)throws Exception{
		log.info("in Controller in authenticate");
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password",e);
		}
		final UserDetails userDetails = myuserservice.loadUserByUsername(request.getUsername());
		
		final String jwt = jwtutil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
}
