package ru.tenilin.cloudservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tenilin.cloudservice.Exeptions.InvalidCredentials;
import ru.tenilin.cloudservice.config.token.TokenProvider;
import ru.tenilin.cloudservice.model.FileNameSizeProjection;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.service.FileService;
import ru.tenilin.cloudservice.service.UserService;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/list")
    public List<FileNameSizeProjection> getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return fileService.getAllFiles(name);
    }

    @PostMapping("/file")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            fileService.upload(file, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(InvalidCredentials.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidCredentials (InvalidCredentials e) {
        return e.getMessage();
    }




}
