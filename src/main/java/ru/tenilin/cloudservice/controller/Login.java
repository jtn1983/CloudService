package ru.tenilin.cloudservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    @JsonProperty("auth-token")
    private String authToken;
}
