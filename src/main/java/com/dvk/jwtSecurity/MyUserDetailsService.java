/**
 * 
 */
package com.dvk.jwtSecurity;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author User
 *
 */
@Service
public class MyUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
		
		return new User("devakibk","guruji",new ArrayList<>());
	}

	
}
