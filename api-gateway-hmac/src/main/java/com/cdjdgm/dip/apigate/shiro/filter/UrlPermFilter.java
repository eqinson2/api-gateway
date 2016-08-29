package com.cdjdgm.dip.apigate.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class UrlPermFilter extends AuthorizationFilter {
	
	public UrlPermFilter(){
		System.out.println("UrlPermFilter Constuctor");
	}
	
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
		String requestURI = getPathWithinApplication(request);

        Subject subject = getSubject(request, response);
//        String[] perms = (String[]) mappedValue;

//        boolean isPermitted = true;
//        if (perms != null && perms.length > 0) {
//            if (perms.length == 1) {
//                if (!subject.isPermitted(perms[0])) {
//                    isPermitted = false;
//                }
//            } else {
//                if (!subject.isPermittedAll(perms)) {
//                    isPermitted = false;
//                }
//            }
//        }

        return subject.isPermitted(requestURI);
	}
}
