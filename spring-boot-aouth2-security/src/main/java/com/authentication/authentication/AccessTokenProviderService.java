package com.authentication.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenProviderService {

	public String provideAccessToken() {
		String usernameWithAccessToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if(! usernameWithAccessToken.contentEquals("anonymousUser")) {
			String[] accessToken = usernameWithAccessToken.split(",");	
			return accessToken[1];
		}else {
			return null;
		}		
	}
	
	public String provideUsername() {
		String usernameWithAccessToken = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		
		if(! usernameWithAccessToken.contentEquals("anonymousUser")) {
			String[] accessToken = usernameWithAccessToken.split(",");	
			return accessToken[0];
		}else {
			return null;
		}
	}

}
