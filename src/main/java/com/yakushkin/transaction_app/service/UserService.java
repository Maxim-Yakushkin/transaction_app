package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
