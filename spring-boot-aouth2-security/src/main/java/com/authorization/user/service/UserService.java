/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.authorization.user.service;

import com.authorization.common.BaseResponse;
import com.authorization.common.ItemResponse;
import com.authorization.common.UserInfoUtils;
import com.authorization.user.model.dto.UsersDTO;
import com.authorization.user.model.dto.UserRolesDTO;
import com.authorization.user.model.entity.UserRoles;
import com.authorization.user.model.entity.Users;
import com.authorization.user.repository.UserRolesRepository;
import com.authorization.user.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ahasan Habib
 */
@Service
public class UserService {

	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	public UserRolesRepository userRolesRepository;

	@Transactional
	public List<UserRolesDTO> findUserRolesByUsername(String username) {
		List<UserRoles> roles = new ArrayList<>();
		roles = userRolesRepository.findByUsername(username);
		if (roles != null) {
			return roles.stream().map(role -> copyUserRoleToUserRoleDTO(role)).collect(Collectors.toList());
		}
		return null;
	}

	public UserRolesDTO copyUserRoleToUserRoleDTO(UserRoles userRole) {

		UserRolesDTO usersRoleDTO = new UserRolesDTO();
		BeanUtils.copyProperties(userRole, usersRoleDTO);
		return usersRoleDTO;
	}

	@Transactional
	public BaseResponse createUser(UsersDTO userDto) {

		BaseResponse baseResponse = new BaseResponse();
		Users users = new Users();
		String encryptedPassword = UserInfoUtils.getHashPassword(userDto.getPassword());
		users.setEnabled(true);
		users.setPassword(encryptedPassword);
		users.setUsername(userDto.getUsername());
		List<UserRoles> userRoles = new ArrayList<>();
		UserRoles roles;

		for (String r : userDto.getUserRoles()) {
			roles = new UserRoles();
			roles.setUsername(userDto.getUsername());
			roles.setRoleName(r);
			userRoles.add(roles);
		}

		usersRepository.save(users);
		userRolesRepository.save(userRoles);

		baseResponse.setMessage("User has been created succesfully");
		return baseResponse;

	}

	@Transactional
	public BaseResponse deleteUser(Long id) {

		BaseResponse baseResponse = new BaseResponse();
		Users users = usersRepository.findOne(id);
		List<UserRoles> roleses = userRolesRepository.findByUsername(users.getUsername());

		usersRepository.delete(users);
		userRolesRepository.delete(roleses);

		baseResponse.setMessage("User has been deleted succesfully");
		return baseResponse;

	}

	public List<String> userroles(List<UserRoles> roles, String username) {

		List<String> userroles = new ArrayList<>();
		for (UserRoles r : roles) {
			if (r.getUsername().equals(username)) {
				userroles.add(r.getRoleName());
			}
		}

		return userroles;
	}

	public ItemResponse userList() {
		ItemResponse itemResponse = new ItemResponse();
		List<Users> users = usersRepository.findAll();
		List<UserRoles> roles = userRolesRepository.findAll();

		List<UsersDTO> userDtos = new ArrayList<>();
		UsersDTO userDto;
		for (Users u : users) {
			userDto = new UsersDTO();
			userDto.setUserId(u.getId());
			userDto.setUsername(u.getUsername());
			userDto.setUserRoles(userroles(roles, u.getUsername()));
			userDto.setPassword(u.getPassword());
			userDtos.add(userDto);
		}

		itemResponse.setItem(userDtos);
		itemResponse.setMessage("OK");
		return itemResponse;

	}

}
