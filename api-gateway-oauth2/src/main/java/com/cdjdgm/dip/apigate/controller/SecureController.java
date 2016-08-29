package com.cdjdgm.dip.apigate.controller;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdjdgm.dip.util.JsonUtil;
import com.cdjdgm.dip.util.ReflectionUtil;
import com.cdjdgm.dip.util.SpringUtil;

@Controller
@RequestMapping("/api")
public class SecureController {
	private static final Logger log = LoggerFactory.getLogger(SecureController.class);
	
	@Value("${spring.profiles.active:STANDALONE}")
	private String runMode;
    
    @ResponseBody
	@RequestMapping(value = "/{ifname}/{method}", method = RequestMethod.POST)
    public Object api(@PathVariable String ifname, @PathVariable String method, @RequestBody String json) throws Exception {    	
        log.debug("Access-API : {}#{}", ifname, method);

        Object result = null;
        
        if (runMode.equals("STANDALONE")){
        	result = callSpring(ifname, method, json);
        } else if (runMode.equals("CONTAINER")){
        	result = callDubbo(ifname, method, json);
        }
		
		return result;
    }
    
    private Object callSpring(String ifname, String method, String jsonParam) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(ifname);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Could not find interface [" + ifname + "]", e);
		}
    	Object obj = SpringUtil.getBeanByType(clazz);
    	
    	List<?> params = JsonUtil.toObject(jsonParam, List.class);
    	Object result = ReflectionUtil.invokeMethodByName(obj, method, params==null?null:params.toArray());
    	return result;
    }

	private Object callDubbo(String ifname, String method, String jsonParam) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(ifname);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Could not find interface [" + ifname + "]", e);
		}
		
		List<?> params = JsonUtil.toObject(jsonParam, List.class);
		
		Object obj = Class.forName(ifname).newInstance();
		
		log.debug("api-call:"+ifname+"->"+method+":begin:"+Calendar.getInstance().getTimeInMillis());
		Object result = ReflectionUtil.invokeMethodByName(obj, method, params==null?null:params.toArray());
		log.debug("api-call:"+ifname+"->"+method+":end:"+Calendar.getInstance().getTimeInMillis());
		return result;
	}

    @ResponseBody
    @RequestMapping(value="/echo", method = RequestMethod.POST)
    public String echo(@RequestBody String content) {    	
    	return "ECHO:"+content;
    }
	

}
