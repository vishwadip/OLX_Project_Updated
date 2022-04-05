package com.zensar.olx.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
	private long expirationTime = 60000*10;
	
    //Secret key is very sensitive,should be Complex should not be shared with anyone
	private final String SECRET_KEY="Zensar@12345";
	
	public String generateToken(String username) {
		return JWT.create()
				.withClaim("username", username)
				.withExpiresAt(new Date(System.currentTimeMillis()+expirationTime))
				.sign(Algorithm.HMAC512(SECRET_KEY));
	}

	public String validateToken(String token) {
		// TODO we need to validate token
		return JWT.require(Algorithm.HMAC512(SECRET_KEY))
		.build()
		.verify(token)
		.getPayload();
	}

}
