package ru.tenilin.cloudservice.controller;

import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tenilin.cloudservice.service.TokenService;
import ru.tenilin.cloudservice.model.FileEntity;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.service.FileService;
import ru.tenilin.cloudservice.service.UserService;

import java.io.IOException;

@RestController
public class CloudServiceController {

    private final UserService userService;
    private final FileService fileService;
    private final TokenService tokenProvider;

    public CloudServiceController(UserService userService, FileService fileService, TokenService tokenProvider) {
        this.userService = userService;
        this.fileService = fileService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        try {
            UserEntity user = userService.findUserByLoginPassword(request.getLogin(), request.getPassword());
            String token = tokenProvider.generateToken(user.getUserName());
            tokenProvider.addTokenToMap(token, user);
            return new ResponseEntity<>(new Login(token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Error("Bad credentials", 400), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            tokenProvider.removeTokenFromMap(authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listFiles(@RequestParam Integer limit) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String name = authentication.getName();
            return new ResponseEntity<>(fileService.getAllFiles(name, limit), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Error("Error getting file list", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/file")
    public ResponseEntity<?> upload(@RequestParam MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            fileService.upload(file, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            int i = 1;
            return new ResponseEntity<>(new Error("Error input data", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam String filename) {
        try {
            FileEntity foundFile = fileService.getFileByName(filename);
            Resource resource = fileService.download(foundFile.getFileHash());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + foundFile.getFileName())
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(new Error("Error input data", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> delete(@RequestParam String filename) {
        try {
            fileService.delete(filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new Error("Error input data", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<?> update(@RequestParam String filename, @RequestBody String request) {
        JSONObject jsonObject = new JSONObject(request);
        try {
            fileService.update(filename, jsonObject.getString("filename"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new Error("Error input data", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
