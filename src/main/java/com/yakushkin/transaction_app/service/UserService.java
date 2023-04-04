package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
