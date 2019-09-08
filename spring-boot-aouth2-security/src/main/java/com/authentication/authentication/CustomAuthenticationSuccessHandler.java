package com.authentication.authentication;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import com.softapple.online.healthcare.client.common.util.ApiConsumer;
import com.softapple.online.healthcare.client.common.util.ApplicationUtils;
import com.softapple.online.healthcare.client.common.util.StaticValueProvider;
import com.softapple.online.healthcare.client.hospital.model.dto.DoctorVisitingScheduleDTO;
import com.softapple.online.healthcare.client.patient.service.NonRegPatientService;
import com.softapple.online.healthcare.client.users.model.dto.UserDetailsDTO;

@Controller
@ManagedBean
@Scope("session")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private AccessTokenProviderService accessTokenProviderService;

	@Autowired
	private NonRegPatientService nonRegPatientService;

	private DoctorVisitingScheduleDTO doctorVisitingScheduleDTO;

	private List<DoctorVisitingScheduleDTO> doctorVisitingScheduleDTOs;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {

		HttpSession httpSession = httpServletRequest.getSession();
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		this.doctorVisitingScheduleDTOs = null;
		userDetailsDTO = findUserDetailsOfLoginUser(accessTokenProviderService.provideUsername(),
				accessTokenProviderService.provideAccessToken());
		if (userDetailsDTO != null) {
			if (userDetailsDTO.getHospital().getHospitalId() != null) {
				userDetailsDTO.setProfileName(userDetailsDTO.getHospital().getHospitalName());
				userDetailsDTO.setPhotoWithBase64(
						ApplicationUtils.provideBase64Image(userDetailsDTO.getHospital().getLogoName()));
			} else if (userDetailsDTO.getDoctor().getDoctorId() != null) {
				userDetailsDTO.setProfileName(userDetailsDTO.getDoctor().getDoctorName());
				userDetailsDTO.setPhotoWithBase64(
						ApplicationUtils.provideBase64Image(userDetailsDTO.getDoctor().getPhotoName()));
			} else if (userDetailsDTO.getParentPatient().getParentPatientId() != null) {
				userDetailsDTO.setProfileName(userDetailsDTO.getParentPatient().getPatientName());
				userDetailsDTO.setPhotoWithBase64(
						ApplicationUtils.provideBase64Image(userDetailsDTO.getParentPatient().getImageName()));

				if (doctorVisitingScheduleDTO != null && doctorVisitingScheduleDTO.getDoctorConfig() != null) {
					doctorVisitingScheduleDTOs = nonRegPatientService.findViSchedulesByDctrId(
							doctorVisitingScheduleDTO.getDoctorConfig().getDoctorInfo().getDoctorId(),
							doctorVisitingScheduleDTO.getDoctorConfig().getHospital().getHospitalId());
					this.doctorVisitingScheduleDTO = null;
				}
			}
			httpSession.setAttribute("userDetails", userDetailsDTO);
		}

		if (userDetailsDTO.getHospital().getHospitalEmail() != null) {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/template/dashboard.xhtml");
		} else if (userDetailsDTO.getDoctor().getDoctorEmail() != null) {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/template/template.xhtml");
		} else if (userDetailsDTO.getParentPatient().getContactNo() != null && doctorVisitingScheduleDTOs != null) {
			this.doctorVisitingScheduleDTO = null;
			httpServletResponse
					.sendRedirect(httpServletRequest.getContextPath() + "/patient/doctor-all-schedules.xhtml");
		} else if (userDetailsDTO.getParentPatient().getContactNo() != null && doctorVisitingScheduleDTOs == null) {
			this.doctorVisitingScheduleDTO = null;
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/template/template.xhtml");
		} else {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/template/template.xhtml");
		}

	}

	public UserDetailsDTO findUserDetailsOfLoginUser(String username, String accessToken) {
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		try {
			userDetailsDTO = ApiConsumer.findUserDetailsOfLoginUser(StaticValueProvider.LOGIN_USER_URI,
					"/details?username=" + username + "&access_token=" + accessToken);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userDetailsDTO;

	}

	public DoctorVisitingScheduleDTO getDoctorVisitingScheduleDTO() {
		if (doctorVisitingScheduleDTO == null)
			doctorVisitingScheduleDTO = new DoctorVisitingScheduleDTO();
		return doctorVisitingScheduleDTO;
	}

	public void setDoctorVisitingScheduleDTO(DoctorVisitingScheduleDTO doctorVisitingScheduleDTO) {
		this.doctorVisitingScheduleDTO = doctorVisitingScheduleDTO;
	}

	public List<DoctorVisitingScheduleDTO> getDoctorVisitingScheduleDTOs() {
		return doctorVisitingScheduleDTOs;
	}

	public void setDoctorVisitingScheduleDTOs(List<DoctorVisitingScheduleDTO> doctorVisitingScheduleDTOs) {
		this.doctorVisitingScheduleDTOs = doctorVisitingScheduleDTOs;
	}
}
