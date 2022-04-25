package ru.tenilin.cloudservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.tenilin.cloudservice.model.UserEntity;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userService.findByLogin(userName);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
