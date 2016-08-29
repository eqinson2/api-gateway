package com.cdjdgm.dip.apigate.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cdjdgm.dip.util.SecUtils;
import com.cdjdgm.dip.util.StringUtil;


@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Value("${spring.profiles.active:STANDALONE}")
	private String runMode;
	
	
	private static final Map<String,String> userMap = new HashMap<>();
	static {
		userMap.put("admin", "21232f297a57a5a743894a0e4a801fc3"); //admin/admin
		userMap.put("duan", "21232f297a57a5a743894a0e4a801fc3"); //duan/admin
	}
	
    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        // 校验用户
        String pwd = userMap.get(lowercaseLogin);
        
        String message = null;
		if (pwd==null){
			message = "账号不存在";
		} else if (!pwd.equalsIgnoreCase(SecUtils.encoderByMd5With32Bit(pwd))){
			message = "密码错误";
		}
		
		if (message!=null){
			log.warn(message);
			throw new RuntimeException(message);
		}
		
        List<String> menus = listAllMenus(lowercaseLogin);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : menus) {
        	if (StringUtil.isNotBlank(authority)){
        		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        		grantedAuthorities.add(grantedAuthority);
        	}
        }

        // spring-security默认使用的passwd的编码不是MD5，这个需要重写设置为MD5编码
        return new org.springframework.security.core.userdetails.User(lowercaseLogin, 
        		SecUtils.encoderByMd5With32Bit(pwd), grantedAuthorities);

    }


	private List<String> listAllMenus(String account) {
		List<String> menus = new ArrayList<>();
		return menus;
	}

}
