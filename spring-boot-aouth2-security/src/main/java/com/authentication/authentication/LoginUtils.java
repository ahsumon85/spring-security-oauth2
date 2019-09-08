package com.authentication.authentication;

public class LoginUtils {
	
	public static final String OAUTH_TOKEN_URL = "http://localhost:8082/oauth/token";
	public static final String OAUTH_CONTENT_TYPE = "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW";
	public static final String OAUTH_AUTHORIZATION_HEADER = "Basic b25saW5lLWhlYWx0aGNhcmUtcmVhZC13cml0ZS1jbGllbnQ6MTIzNA==";

	public static String getOauthBody(String username, String password) {
		return "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data;"
		  		+ " name=\"client_id\"\r\n\r\nonline-healthcare-read-write-client\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data;"
		  		+ " name=\"grant_type\"\r\n\r\npassword\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data;"
		  		+ " name=\"username\"\r\n\r\n"+ username + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data;"
		  		+ " name=\"password\"\r\n\r\n" + password + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
	}
	
}
