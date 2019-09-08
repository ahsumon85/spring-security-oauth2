package com.authorization.authentication;

import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.softapple.online.healthcare.client.common.enums.UserRole;
import com.softapple.online.healthcare.client.users.model.dto.UserDetailsDTO;

@Controller
@ManagedBean
@Scope("session")
public class ApplicationUtilsMB {
	public Collection<SimpleGrantedAuthority> authorities;

	public boolean userRoleAsSuperAdmin;
	public boolean userRoleAsUser;
	public boolean userRoleAsGuest;
	public boolean userRoleHospitalAdmin;
	public boolean userRolePatient;
	public boolean userRoleDoctor;
	
	@Autowired
	private LoginService loginService;

	@PostConstruct
	public void init() {
		authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
	}

	public String provideCurrentUser() {
		return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static UserDetailsDTO provideCurrentUserDetils() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		UserDetailsDTO userDetailsDTO = (UserDetailsDTO) session.getAttribute("userDetails");
		return userDetailsDTO;
	}
	
	public boolean checkUserRole(String roleName) {
		if (authorities.toString().contains(roleName))
			return true;
		else
			return false;
	}

	public void setUserRoleAsSuperAdmin(boolean userRoleAsAdmin) {
		this.userRoleAsSuperAdmin = userRoleAsAdmin;
	}

	public void setUserRoleAsUser(boolean userRoleAsUser) {
		this.userRoleAsUser = userRoleAsUser;
	}

	public boolean isUserRoleAsGuest() {
		if (checkUserRole(UserRole.GUEST.getRole().toString()))
			userRoleAsGuest = true;
		return userRoleAsGuest;
	}

	public void setUserRoleAsGuest(boolean userRoleAsGuest) {
		this.userRoleAsGuest = userRoleAsGuest;
	}

	public boolean isUserRoleHospitalAdmin() {
		if (checkUserRole(UserRole.HOSPITAL_ADMIN.getRole().toString()))
			userRoleHospitalAdmin = true;
		return userRoleHospitalAdmin;
	}

	public void setUserRoleHospitalAdmin(boolean userRoleHospitalAdmin) {
		this.userRoleHospitalAdmin = userRoleHospitalAdmin;
	}

	public boolean isUserRolePatient() {
		if (checkUserRole(UserRole.PATIENT.getRole().toString()))
			userRolePatient = true;
		return userRolePatient;
	}

	public void setUserRolePatient(boolean userRolePatient) {
		this.userRolePatient = userRolePatient;
	}

	public boolean isUserRoleDoctor() {
		if (checkUserRole(UserRole.DOCTOR.getRole().toString()))
			userRoleDoctor = true;
		return userRoleDoctor;
	}

	public void setUserRoleDoctor(boolean userRoleDoctor) {
		this.userRoleDoctor = userRoleDoctor;
	}

	public boolean isUserRoleAsUser() {
		return userRoleAsUser;
	}

	public boolean isUserRoleAsSuperAdmin() {
		if (checkUserRole(UserRole.SUPER_ADMIN.getRole().toString()))
			userRoleAsSuperAdmin = true;
		return userRoleAsSuperAdmin;
	}
	
	/*
	 * public String logout() { loginService.logout(); }
	 */

}
