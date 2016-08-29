package com.cdjdgm.dip.apigate.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyMd5PasswordEncoder implements PasswordEncoder {
	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
	private final String salt = "";
	
	@Override
	public String encode(CharSequence rawPassword) {
		return encoder.encodePassword(rawPassword.toString(), salt);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encoder.isPasswordValid(encodedPassword.toString(), rawPassword.toString(), salt);
	}

}
