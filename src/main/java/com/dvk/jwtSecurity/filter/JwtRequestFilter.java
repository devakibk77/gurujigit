/**
 * 
 */
package com.dvk.jwtSecurity.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dvk.jwtSecurity.JwtUtil;
import com.dvk.jwtSecurity.MyUserDetailsService;

/**
 * @author User
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	private MyUserDetailsService userService;
	
	@Autowired
	private JwtUtil jwtutil;
	
	Log log = LogFactory.getLog(JwtRequestFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("authorization");
		
		String username = null;
		String jwt = null;
		log.info("in jwtrequestfilter : dofilter::"+authorizationHeader);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
			jwt = authorizationHeader.substring(7);
			username = jwtutil.extractUsername(jwt);
		}
		
		if( username != null && SecurityContextHolder.getContext().getAuthentication()== null){
			UserDetails userDetails = this.userService.loadUserByUsername(username);
			if(jwtutil.validateToken(jwt, userDetails)){
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}			
			}
		chain.doFilter(request, response);
	}

}
