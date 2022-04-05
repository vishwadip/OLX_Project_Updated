package com.zensar.olx.db;

import java.util.HashMap;
import java.util.Map;

public class TokenStorage {
	
	private static Map<String, String> tokenCache;
	
	static
	{
		tokenCache= new HashMap<>();
	}

	//this method responsible to storing token in Cache on server
	public static void storeToken(String key, String token)
	{
		tokenCache.put(key, token);
	}

	//This method is responsible for removing token from server Cache
	public static String removeToken(String key) {
		return tokenCache.remove(key);
	}
	
	public static String getToken(String key) {
		return tokenCache.get(key);
	}
}

