package com.yakushkin.transaction_app.service;

import com.yakushkin.transaction_app.entity.User;
import com.yakushkin.transaction_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User findById(Integer userId) {
        return userRepository.findById(userId)
                .orElse(new User());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> update(User userForUpdate) {
        User dbUser = userRepository.findById(userForUpdate.getId())
                .orElse(new User());

        User updatedUser = null;
        if (dbUser.getId() != null) {
            dbUser.setName(userForUpdate.getName());
            dbUser.setAddress(userForUpdate.getAddress());
            updatedUser = userRepository.saveAndFlush(dbUser);
        }

        return Optional.ofNullable(updatedUser);
    }

    public boolean delete(Integer userId) {
        return userRepository.findById(userId)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
