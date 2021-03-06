package com.gmail.sebastian.pisarski.service.impl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.sebastian.pisarski.entity.User;
import com.gmail.sebastian.pisarski.entity.enums.UserRight;
import com.gmail.sebastian.pisarski.repository.UserRepository;

@Component
@Profile({ "dev", "prod" })
public class InitService implements ApplicationListener<ApplicationEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationEvent event) {
		if (event.getSource() instanceof AbstractApplicationContext
				&& ((AbstractApplicationContext) event.getSource())
						.getDisplayName().startsWith("Root")) {
			User user = userRepository.findByUsername("admin");
			if (user == null) {
				User admin = new User();
				admin.setFirstName("admin");
				admin.setLastName("admin");
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setRights(Stream.of(UserRight.values()).collect(
						Collectors.toSet()));

				userRepository.save(admin);
			}
		}
	}

}