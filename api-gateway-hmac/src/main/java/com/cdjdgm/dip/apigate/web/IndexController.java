package com.cdjdgm.dip.apigate.web; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdjdgm.dip.apigate.shiro.authc.HmacToken;
import com.cdjdgm.dip.util.JsonUtil;
import com.cdjdgm.dip.util.Maps;
import com.cdjdgm.dip.util.SecUtils;

/**
 *	提供login等功能
 */
@RestController
@RequestMapping("/")
public class IndexController  {
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
	private static final Map<String,String> userMap = new HashMap<>();
	static {
		userMap.put("admin", "21232f297a57a5a743894a0e4a801fc3"); //admin/admin
		userMap.put("duan", "21232f297a57a5a743894a0e4a801fc3"); //duan/admin
	}
	
	//private static final String RANDOM_CODE_KEY = "1";
	
    @Autowired
    private Environment env;
    

	@RequestMapping("/api/{ifname}/{method}")
    public Object api(@PathVariable String ifname, @PathVariable String method, @RequestBody String json) 
    		throws InstantiationException, IllegalAccessException, ClassNotFoundException {    	
        log.debug("Access API : {}#{}", ifname, method);
		
		List<?> params = JsonUtil.toObject(json, List.class);
		
		Object obj = Class.forName(ifname).newInstance();
		Object result = ReflectionUtil.invokeMethodByName(obj, method, params==null?null:params.toArray());
		
		return result;
    }
    
	@RequestMapping("/login")
    public ResponseEntity<Map<String,String>> login(String account, String password, Integer timeout, HttpServletRequest request) {				
		if (!StringUtils.hasText(account) || !StringUtils.hasText(password)) {
			log.warn("客户端("+account+")提交的帐号/密码不能为空!");
			return new ResponseEntity<Map<String,String>>(Maps.init("message","帐号/密码不能为空").build(), HttpStatus.BAD_REQUEST);
		}
		
		// 用户校验
		String pwd = userMap.get(account);
		
		String message = null;
		if (pwd==null){
			message = "账号不存在";
		} else if (!pwd.equalsIgnoreCase(SecUtils.encoderByMd5With32Bit(password))){
			message = "密码错误";
		}
				
		if (message!=null){
			log.warn(message);
			return new ResponseEntity<Map<String,String>>(Maps.init("message",message).build(), HttpStatus.UNAUTHORIZED);
		}
		
		SecurityUtils.getSubject().logout();
		// 生成accessKey、secretKey
		String[] keys = genKeys(); // [accessKey, secretKey]
		
		HmacToken token = new HmacToken(keys[0], keys[1]);
		token.setRequest(request);
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		
		Map<String, String> map = Maps.init("message","登录成功")
				.put("accessKey", keys[0])
				.put("secretKey", keys[1]).build();
		
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);
    }
	
	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
    public void logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        //request.getSession().invalidate();
    }
	
    
	@RequestMapping("/admin")
    public String sample(@RequestBody String text) throws IOException {
		// format: AccessKeyId = SecretAccessKey,
    	return text+"!!!!!!!!!!";
    }
	
    private String[] genKeys() {
//        int accessKeyLength = 20;
//        int secretKeyLength = 40;
//
//        SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator(accessKeyLength, secretKeyLength);
//        keyGenerator.generateKeys();
//        String[] keys = keyGenerator.getKeys();
//        
//        return keys;
    	return new String[]{"11111111111111111111111111111", "2222222222222222222222222222222222"};
        //return new String[]{"DDKGPVB9OEDXQV2OFSKDRJ0HBG4=", "xzp2+0JhdJ2Le1wkybRW4e+TuW2bJYveaIHxLrXn56x6VNeqmBU1Lg=="};
    }
	
}