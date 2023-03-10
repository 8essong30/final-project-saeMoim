package com.saemoim.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saemoim.domain.Admin;
import com.saemoim.domain.enums.UserRoleEnum;
import com.saemoim.dto.request.AdminRequestDto;
import com.saemoim.dto.response.AdminResponseDto;
import com.saemoim.dto.response.AdminTokenResponseDto;
import com.saemoim.exception.ErrorCode;
import com.saemoim.jwt.JwtUtil;
import com.saemoim.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	@Override
	public AdminTokenResponseDto signInByAdmin(AdminRequestDto requestDto) {
		Admin admin = adminRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_USER.getMessage())
		);
		if (!passwordEncoder.matches(requestDto.getPassword(), admin.getPassword())) {
			throw new IllegalArgumentException(ErrorCode.INVALID_PASSWORD.getMessage());
		}

		String accessToken = issueToken(admin.getId(), admin.getUsername(), admin.getRole());
		return new AdminTokenResponseDto(accessToken);
	}

	@Transactional(readOnly = true)
	@Override
	public List<AdminResponseDto> getAdmins() {
		return adminRepository.findAll().stream().map(AdminResponseDto::new).toList();
	}

	@Transactional
	@Override
	public void createAdmin(AdminRequestDto requestDto) {
		if (adminRepository.existsByUsername(requestDto.getUsername())) {
			throw new IllegalArgumentException(ErrorCode.DUPLICATED_ADMIN.getMessage());
		}
		String password = passwordEncoder.encode(requestDto.getPassword());
		Admin admin = new Admin(requestDto.getUsername(), password);
		adminRepository.save(admin);
	}

	@Transactional
	@Override
	public void deleteAdmin(Long adminId) {
		Admin admin = adminRepository.findById(adminId).orElseThrow(
			() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_ADMIN.getMessage())
		);

		if (admin.isRootAdmin()) {
			throw new IllegalArgumentException(ErrorCode.NOT_ALLOWED_ADMIN.getMessage());
		}
		adminRepository.deleteById(adminId);
	}

	@Override
	public String issueToken(Long id, String username, UserRoleEnum role) {
		return jwtUtil.createAccessToken(id, username, role);
	}
}
