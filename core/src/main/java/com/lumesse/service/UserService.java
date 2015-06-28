package com.lumesse.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.lumesse.entity.User;

@PreAuthorize("hasAuthority('USER_MANAGEMENT')")
public interface UserService {

	long MAX_USERS_COUNT = 20L;

	/**
	 * @return all users
	 */
	List<User> findAll();

	/**
	 * @return number of all users
	 */
	long getNumberOfUsers();

	/**
	 * Saves new user or updates existing one.
	 * 
	 * @param user
	 *            user to save or edit
	 * @return saved user
	 */
	User save(User user);

	/**
	 * @return user with given username or null if such user doesn't exist
	 */
	User findByUsername(String username);
}
