package ru.tenilin.cloudservice.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Component
@Log
public class TokenProvider {

    @Value("$(token.secret)")
    private String tokenSecret;

    @Autowired
    UserRepository userRepository;

    private HashMap<String, String> tokenMap = new HashMap<>();

    public String generateToken(String login) {
        Date date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        return (tokenMap.containsKey(token)) ? true : false;
    }

    public void addTokenToMap(String token, UserEntity user) {
        tokenMap.put(token, user.getUserName());
        System.out.println(tokenMap);
    }

    public void removeTokenFromMap(String userName) {
        tokenMap.values().remove(userName);
    }

    public String getLoginFromToken(String token) {
        String userName = tokenMap.get(token);
        UserEntity user = userRepository.findByUserName(userName);
        return user.getUserName();
    }
}