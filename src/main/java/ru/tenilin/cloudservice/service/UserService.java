package ru.tenilin.cloudservice.service;

import org.springframework.stereotype.Service;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.UserRepository;

@Service
public class UserService{

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findUserByLoginPassword(String userName, String password) {
        if (userRepository.findByUserNameAndPassword(userName, password) != null) {
            return userRepository.findByUserNameAndPassword(userName, password);
        }else {
            throw new NullPointerException();
        }
    }



}
