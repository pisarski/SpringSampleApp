package com.lumesse.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lumesse.entity.enums.UserRight;
import com.lumesse.service.UserAuthService;

public class UserAuthServiceSecurityIntegrationTest extends
		AbstractSecurityIntegrationTest {

	@Autowired
	@Qualifier("userAuthServiceAdapter")
	private UserAuthService userAuthService;

	@Test
	public void notAuthenticatedUserShouldInvokeLoadUserByUsername() {
		userAuthService.loadUserByUsername("");
	}

	@Test
	public void allUsersShouldInvokeLoadUserByUsername() {
		runWithParams(right -> {
			// given
				loginWithRights((UserRight) right);

				// when
				userAuthService.loadUserByUsername("");
			}, UserRight.values());
	}
}
