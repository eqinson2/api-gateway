/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.cdjdgm.dip.apigate.shiro.realm.text;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdjdgm.dip.apigate.shiro.authc.HmacToken;
import com.cdjdgm.dip.apigate.shiro.realm.HmacRealm;
import com.cdjdgm.dip.apigate.shiro.util.HmacBuilder;
import com.cdjdgm.dip.apigate.shiro.util.SimpleHmacBuilder;

/**
 * @author chris.guan
 */
public class HmacIniRealm extends IniRealm implements HmacRealm {

    protected static final String DEFAULT_RESOURCE_PATH = "classpath:shiro-users.ini";

    private static final Logger log = LoggerFactory.getLogger(HmacIniRealm.class);

    protected HmacBuilder hmacBuilder;

    public HmacIniRealm() {
        setResourcePath(DEFAULT_RESOURCE_PATH);
        setAuthenticationTokenClass(HmacToken.class);
        hmacBuilder = new SimpleHmacBuilder();
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			doClearCache(principals);
			SecurityUtils.getSubject().logout();
			return null;
		}

		Object principal = principals.getPrimaryPrincipal();
		
		Iterator ite = principals.fromRealm(getName()).iterator();
		ite.next();
		String userId = ((Collection<String>)ite.next()).iterator().next();
		System.out.println(userId);

		// 添加角色及权限信息
		SimpleAuthorizationInfo sazi = new SimpleAuthorizationInfo();
		
		try {
			//sazi.addRoles(Arrays.asList("admin"));
			sazi.addStringPermissions(Arrays.asList("/admin"));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}

		return sazi;
		
        //return super.doGetAuthorizationInfo(principals);
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals(); 
    	doClearCache(principals);
    	
    	HmacToken hmacToken = (HmacToken) token;
    	String accessKey = hmacToken.getAccessKeyId();
    	String secretKey = hmacToken.getSecretKey();
    	
    	if (secretKey!=null){
	        SimpleAccount account = new SimpleAccount(accessKey, secretKey, getName());
	        SimplePrincipalCollection spc = (SimplePrincipalCollection)account.getPrincipals();
	        spc.add(Arrays.asList("admin"), getName());
	        return account;
    	}
    	return super.doGetAuthenticationInfo(hmacToken);
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
    	HmacToken hmacToken = (HmacToken) token;
    	HttpServletRequest request = (HttpServletRequest)hmacToken.getRequest();
    	if (!"/login".equalsIgnoreCase(request.getRequestURI())){
    		beforeAssertCredentialsMatch(token, info);
    		super.assertCredentialsMatch(token, info);
    	}
    }

    /**
     * 计算并设置credentialsSalt, 将客户端传入的stringToSign设置为credentials
     * @param token
     * @param info
     */
    protected void beforeAssertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        SimpleAccount account = (SimpleAccount) info;
        if (account != null) {
            ByteSource credentialsSalt = (ByteSource) account.getCredentialsSalt();
            // only set credentials salt on the first time!!!
            if (credentialsSalt == null) {
                Object credentials = account.getCredentials();
                credentialsSalt = ByteSource.Util.bytes(credentials);
                account.setCredentialsSalt(credentialsSalt);
                account.setCredentials(null);
            }

            Object oldCredentials = account.getCredentials();
            Object stringToSign = hmacBuilder.buildStringToSign((HmacToken) token);
            account.setCredentials(stringToSign);

            if (log.isDebugEnabled()) {
                log.debug("oldCredentials: {}", oldCredentials);
                log.debug("curCredentials: {}", account.getCredentials());
                log.debug("credentialsSalt: {}", account.getCredentialsSalt().toHex());
            }
        }
    }

    public HmacBuilder getHmacBuilder() {
        return hmacBuilder;
    }

    public void setHmacBuilder(HmacBuilder hmacBuilder) {
        this.hmacBuilder = hmacBuilder;
    }

}
