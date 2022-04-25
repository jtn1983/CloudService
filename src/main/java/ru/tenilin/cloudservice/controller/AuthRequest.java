package ru.tenilin.cloudservice.controller;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthRequest {
    private String login;
    private String password;
}
