package ru.tenilin.cloudservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tenilin.cloudservice.Exeptions.InvalidCredentials;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.UserRepository;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    public UserEntity findByLogin(String userName) {
        return userRepository.findByUserName(userName);
    }

    public UserEntity findByLoginAndPassword(String userName, String password) {
        UserEntity user = findByLogin(userName);
        if (user == null) {
            throw new InvalidCredentials("User not found!");
        }
        if (!password.equals(user.getPassword())) {
            throw new InvalidCredentials("Password error");
        }
        return user;

    }



}
