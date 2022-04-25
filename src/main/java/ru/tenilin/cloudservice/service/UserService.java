package ru.tenilin.cloudservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tenilin.cloudservice.model.User;
import ru.tenilin.cloudservice.repository.UserRepository;

import java.util.ArrayList;
@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RoleEntityRepository roleEntityRepository;

    public User findByLogin(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findByLoginAndPassword(String userName, String password) {
        User user = findByLogin(userName);
        if (user != null) {
            if (password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }


}
