package com.zensar.olx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.LoginResponse;
import com.zensar.olx.bean.OLXLoginUser;
import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.db.TokenStorage;
import com.zensar.olx.service.OLXUserService;
import com.zensar.olx.util.JwtUtil;

@RestController
public class OLXUserController {
	@Autowired
	private JwtUtil util;

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	OLXUserService service;
	/**
	 * This is the rest specification for authentication token user details
	 * @param olxUser
	 * @return
	 */
	@PostMapping("/user/authenticate")
	public LoginResponse login(@RequestBody OLXLoginUser user)
	{
		UsernamePasswordAuthenticationToken authenticationToken;
		authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),
																		user.getPassword());
		try {
			manager.authenticate(authenticationToken);
			//2 If User is authenticated generate token and return it
			String token = util.generateToken(user.getUserName());
				
			//to store token on server
			//Use Cache
			TokenStorage.storeToken(token, token);
			
			LoginResponse userResponse =new LoginResponse();
			userResponse.setJwt(token);
			System.out.println(userResponse);
			return userResponse;	
		} 
		catch (Exception e)
		{
				e.printStackTrace();
				throw e;
		}
		
	}
	
	@PostMapping("/user")
	public OLXUser addOlxUser(@RequestBody OLXUser olxUser) { //when u don't put @Requestbody its return null in table
		return this.service.addOlxUser(olxUser);
	}
	
	@GetMapping("/user/{uid}")
	public OLXUser findOlxUserById(@PathVariable(name="uid") int id) {
		return this.service.findOlxUser(id);
	}
	
	@GetMapping("/user/find/{userName}")
	public OLXUser findOlxUserByName(@PathVariable(name="userName") String name) {
		return this.service.findOlxUserByName(name);
	}
	
	@GetMapping("/token/validate")
	public ResponseEntity<Boolean> validate(@RequestHeader("Authorization")String auth ){
		try {
			String validateToken= util.validateToken(auth.substring(7));
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/user/logout")
	public ResponseEntity<Boolean> logout(@RequestHeader("Authorization")String auth){
		String token= auth.substring(7);
		
		try {
			TokenStorage.removeToken(token);
			ResponseEntity<Boolean> responseEntity= new ResponseEntity<Boolean>(true,HttpStatus.OK);
			return responseEntity;
		}
		catch(Exception e) {
			e.printStackTrace();
			ResponseEntity<Boolean> responseEntity= new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
			return responseEntity;
		}
	}
}
