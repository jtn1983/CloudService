package ru.tenilin.cloudservice.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.tenilin.cloudservice.Exeptions.InvalidCredentials;
import ru.tenilin.cloudservice.config.token.TokenProvider;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.FileRepository;
import ru.tenilin.cloudservice.service.FileService;
import ru.tenilin.cloudservice.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CloudServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

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

    @PostMapping("/logout")
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

    @GetMapping("/list")
    public List<FileNameSizeProjection> getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return fileService.getAllFiles(name);
    }

//    @GetMapping("/list")
//    public String getAll(){
//        JSONObject o1 = new JSONObject();
//        o1.put("filename", "111.ru");
//        o1.put("size", 5242880);
//
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(o1);
//
//        return jsonArray.toString();
//
//
//    }


    @PostMapping("/file")
    public String file(){
        return "file";
    }

    @ExceptionHandler(InvalidCredentials.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidCredentials (InvalidCredentials e) {
        return e.getMessage();
    }




}
