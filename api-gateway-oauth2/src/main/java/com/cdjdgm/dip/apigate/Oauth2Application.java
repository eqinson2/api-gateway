package com.cdjdgm.dip.apigate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

//@SpringBootApplication
public class Oauth2Application {
    
    @Autowired
    protected Environment env;
    
    public static void main(String[] args) {
        SpringApplication.run(Oauth2Application.class, args);
    }
}
