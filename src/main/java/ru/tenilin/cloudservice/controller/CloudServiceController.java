package ru.tenilin.cloudservice.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.tenilin.cloudservice.config.jwt.TokenProvider;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Log
public class CloudServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse auth(@RequestBody AuthRequest request){
        UserEntity user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = tokenProvider.generateToken(user.getUserName());
        tokenProvider.addTokenToMap(token, user);
        return new AuthResponse(token);
    }

    @GetMapping("/logout")
    public String getLogoutPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            tokenProvider.removeTokenFromMap(authentication.getName());
        return "ok";
    }

    @GetMapping("/hi")
    public String hi(){
        return "Hi user";
    }

    @GetMapping("/hiadmin")
    public String hiAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }




}
