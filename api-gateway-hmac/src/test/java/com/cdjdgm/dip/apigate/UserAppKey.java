package com.cdjdgm.dip.apigate;

import com.cdjdgm.dip.framework.entity.IdEntity;

/**
 * 参数
 * @author GJH
 *
 */
//@Table(name="sys_user_appkey")
public class UserAppKey extends IdEntity {
	private static final long serialVersionUID = 5764926181425081006L;

	private String account;		// 用户帐号
	private String accessKey;		// 访问Key
	private String secretKey;		// 用户密钥
	private int timeout;			// 过期时间
		
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	
}