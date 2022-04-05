package com.zensar.olx.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.zensar.olx.db.TokenStorage;
import com.zensar.olx.util.JwtUtil;


//This is custom filter
//you need to add this filter
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
	
	private final String BEARER = "Bearer ";
	private String authorizationHeader= "Authorization";

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);

	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		JwtUtil jwtUtil=new JwtUtil();
		//1. Check if user has passed token, we do that by, fetching value from Authorization header
		String authorizationHeaderValue= request.getHeader(authorizationHeader);
		//if token is not passed OR if it does not start with Bearer.
		//Don't do anything proceed to next filter in chain
		if(authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(BEARER))
		{
			chain.doFilter(request, response); //invoke next filter in chain
			return ;
		}
		
		String token = authorizationHeaderValue.substring(7);
		//check if the token exists in cache
		String tokenExists = TokenStorage.getToken(token);
		//if token is null means user has logged out
		if(tokenExists==null) {
			chain.doFilter(request, response);
			return;
		}
		
		
		//2 If token not present ask user to login.
		if(authorizationHeaderValue!=null && authorizationHeaderValue.startsWith(BEARER)) {
		
			
			if(token!=null) 
			{
				System.out.println("authorizationHeaderValue " + authorizationHeaderValue);
				System.out.println("Token Value------"+ token);
				try 
				{
				//validate the token
				String encodedPayload=jwtUtil.validateToken(token);
				//if Token is valid
				String actualPayload= new String(Base64.getDecoder().decode(encodedPayload));
				//from payload we need to fetch user-name
				JsonParser jsonParser=JsonParserFactory.getJsonParser();
				Map<String , Object> parseMap= jsonParser.parseMap(actualPayload);
				String username=(String) parseMap.get("username"); 
				//create UsernamePasswordAuthenticationToken
				UsernamePasswordAuthenticationToken authenticationToken;
				authenticationToken=new UsernamePasswordAuthenticationToken(username,null,
																	AuthorityUtils.createAuthorityList("ROLE_USER"));
				//Authenticate user
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		chain.doFilter(request, response);
		//3 If token present fetch it and validates it
	}
}
