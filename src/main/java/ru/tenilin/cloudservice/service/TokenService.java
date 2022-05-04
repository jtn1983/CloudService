package ru.tenilin.cloudservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tenilin.cloudservice.model.UserEntity;
import ru.tenilin.cloudservice.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenService {

    @Value("$(token.secret)")
    private String tokenSecret;

    final
    UserRepository userRepository;

    private ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();

    public TokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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