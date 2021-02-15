/**
 * 
 */
package com.dvk.jwtSecurity;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dvk.jwtSecurity.filter.JwtRequestFilter;

/**
 * @author User
 *
 */
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter{
	
	Log log = LogFactory.getLog(MySecurityConfig.class);
	
	@Autowired
	private MyUserDetailsService userService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("in Websecurity configure auth");
		auth.userDetailsService(userService);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("in Websecurity configure http");
		http.csrf().disable()
			.authorizeRequests().antMatchers("/authenticate").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		log.info("in Websecurity passwordencoder");
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		log.info("in Websecurity auth bean");
		return super.authenticationManager();
	}

}
