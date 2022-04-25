package ru.tenilin.cloudservice.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tenilin.cloudservice.config.jwt.JwtProvider;
import ru.tenilin.cloudservice.model.User;
import ru.tenilin.cloudservice.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log
public class CloudServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthRequest request;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse auth(@RequestBody AuthRequest request){
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getUserName());
        return new AuthResponse(token);
    }

    @GetMapping("/hi")
    public String hi(){
        return "Hi user";
    }

    @GetMapping("/hiadmin")
    public String hiAdmin() {return "Hi admin";}
}
