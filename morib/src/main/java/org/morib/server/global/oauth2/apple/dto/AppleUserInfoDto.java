package org.morib.server.global.oauth2.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleUserInfoDto {
	private AppleResponseName name;
	private String email;

	public static String fullName(AppleUserInfoDto dto) {
		return dto.getName().getLastName()  + dto.getName().getFirstName();
	}
}
