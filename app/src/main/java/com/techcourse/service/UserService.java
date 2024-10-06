package com.techcourse.service;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import java.util.NoSuchElementException;

public class UserService {

    public User register(User user) {
        InMemoryUserRepository.save(user);
        return findByAccount(user.getAccount());
    }

    public User findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }
}
