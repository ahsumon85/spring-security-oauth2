package com.authentication.authentication;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.softapple.online.healthcare.client.common.util.ApiConsumer;
import com.softapple.online.healthcare.client.common.util.ApplicationUtils;
import com.softapple.online.healthcare.client.common.util.StaticValueProvider;
import com.softapple.online.healthcare.client.users.model.dto.UsersDTO;
import com.softapple.online.healthcare.client.users.model.dto.UsersRoleDTO;

@Controller
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private LoginService loginService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();

		String password = authentication.getCredentials().toString();

		String accessToken = loginService.porvideAccessToken(username, password);

		if (accessToken != null) {
			List<UsersRoleDTO> userRoleList = findLoginUserRoles(username, accessToken);
			if (userRoleList == null) {
				userRoleList = new ArrayList<>();
				UsersRoleDTO userRoleDTO = new UsersRoleDTO();
				userRoleDTO.setRoleName("ROLE_GUEST");
				userRoleDTO.setUsername(username);
				userRoleList.add(userRoleDTO);
			}
			List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			for (UsersRoleDTO ur : userRoleList) {
				grantedAuthorities.add(new SimpleGrantedAuthority(ur.getRoleName()));
			}

			return new UsernamePasswordAuthenticationToken(username + "," + accessToken, password, grantedAuthorities);

		} else {
			throw new UsernameNotFoundException("Sorry! Username or Password is invalid");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	/*
	 * public boolean checkUserValidity(String username, String password) {
	 * boolean userValidity = false;
	 * 
	 * UsersDTO userDTO =
	 * ApiConsumer.consumeLoginSuccessUser(StaticValueProvider.LOGIN_USER_URI,
	 * "user/by-username?username=" + username);
	 * 
	 * if (userDTO != null) {
	 * 
	 * if (BCrypt.checkpw(password, userDTO.getPassword())) { userValidity =
	 * true; } } return userValidity;
	 * 
	 * }
	 */

	public List<UsersRoleDTO> findLoginUserRoles(String username, String accessToken) {
		List<UsersRoleDTO> userRolesDTOs = ApiConsumer.consumeLoginUserRoles(StaticValueProvider.LOGIN_USER_URI,
				"/roles?username=" + username + "&access_token=" + accessToken);
		return userRolesDTOs;

	}

}
