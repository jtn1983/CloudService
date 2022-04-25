package ru.tenilin.cloudservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.tenilin.cloudservice.model.User;
import ru.tenilin.cloudservice.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.findByLogin(userName);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
