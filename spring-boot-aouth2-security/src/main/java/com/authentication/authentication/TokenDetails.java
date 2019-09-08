package com.authentication.authentication;

public class TokenDetails {
	
	private String accessToken;
	
	private String refreshToken;
	
	private String scope;
	
	private int tokenLifeTime;
	
	private String username;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getTokenLifeTime() {
		return tokenLifeTime;
	}

	public void setTokenLifeTime(int tokenLifeTime) {
		this.tokenLifeTime = tokenLifeTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
}
