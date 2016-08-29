package com.cdjdgm.dip.apigate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cdjdgm.dip.apigate.Oauth2Application;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Oauth2Application.class)
@WebAppConfiguration
public class Oauth2ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
