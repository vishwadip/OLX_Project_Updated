package com.zensar.olx.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.zensar.olx.bean.OLXUser;
import com.zensar.olx.db.OLXUserDAO;

@Service
public class OLXUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private OLXUserDAO repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//ToDo talk to database and fetch the UserDetails
		OLXUser foundUser=repo.findByUserName(username);
		if(foundUser==null) {
			throw new UsernameNotFoundException(username);
		}
		//UserDetails is an interface given by spring security
		//we are free to implement this interface but for simplicity spring security has given a class its
		//- implements UserDetails information
		//name of the class UserDetails
		//In this method we have to create object of user and return it
		//if(username.equals("zensar")) {
		String roles = foundUser.getRoles();
		User authenticatedUser= new User(foundUser.getUserName(),foundUser.getPassword(),
				AuthorityUtils.createAuthorityList(roles));
		System.out.println("In loadUserByUsername ");
		return authenticatedUser;
		//}
		//throw new UsernameNotFoundException("zensar");
	}
}
