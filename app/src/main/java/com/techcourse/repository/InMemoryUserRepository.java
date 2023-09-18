package com.techcourse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.techcourse.domain.User;

public class InMemoryUserRepository {

	private static final Map<String, User> database = new ConcurrentHashMap<>();

	static {
		final var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
		database.put(user.getAccount(), user);
	}

	private InMemoryUserRepository() {
	}

	public static void save(User user) {
		database.put(user.getAccount(), user);
	}

	public static Optional<User> findByAccount(String account) {
		return Optional.ofNullable(database.get(account));
	}
}
