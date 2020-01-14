/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.user.controller;

import com.authorization.common.BaseResponse;
import com.authorization.common.ItemResponse;
import com.authorization.user.model.dto.UsersDTO;
import com.authorization.user.model.dto.UserRolesDTO;
import com.authorization.user.service.UserService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Ahasan Habib
 */

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	public UserService userService;

	@Autowired
	private TokenStore tokenStore;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public List<UserRolesDTO> findLoginUserRoles(@RequestParam String username) {
		List<UserRolesDTO> roles = userService.findUserRolesByUsername(username);
		return roles;
	}

	@RequestMapping(value = "/test/url", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> userRquesttest(@RequestParam String username, @RequestBody UsersDTO usersDTO) {
		System.out.println(usersDTO);
		return null;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<BaseResponse> createUser(@RequestBody UsersDTO userDto) {
		BaseResponse baseResponse = userService.createUser(userDto);
		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<BaseResponse> deleteUser(@RequestParam Long id) {
		BaseResponse baseResponse = userService.deleteUser(id);
		return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);

	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<ItemResponse> userList() {
		ItemResponse itemResponse = userService.userList();
		return new ResponseEntity<>(itemResponse, HttpStatus.OK);

	}

	@RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.GET)
	public ResponseEntity<BaseResponse> logout(HttpServletRequest request) {
		BaseResponse baseResponse = new BaseResponse();
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
			tokenStore.removeRefreshToken(refreshToken);
			tokenStore.removeAccessToken(accessToken);
			baseResponse.setMessage("Success");
			return new ResponseEntity<>(baseResponse, HttpStatus.OK);
		}
		baseResponse.setMessage("Failed");
		return new ResponseEntity<>(baseResponse, HttpStatus.UNAUTHORIZED);
	}

}
