package br.com.one.mappers;

import br.com.one.domain.User;
import br.com.one.records.LoginResponse;
import br.com.one.records.UserResponse;

public class UserMapper {
	
	private UserMapper() {
	
	}
	
	public static UserResponse toUserResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles(), user.getCreationDate());
	}
	
	public static LoginResponse toLoginResponse(User user, String token) {
		return new LoginResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles(), user.getCreationDate(), token);
	}
	
}