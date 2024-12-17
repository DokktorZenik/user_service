package com.task_manager.user_service.utils;

import com.task_manager.user_service.entity.User;
import com.task_manager.user_service.repository.UserRepository;
import com.task_manager.user_service.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtLifetime;
    private final UserRepository userRepository;
    private Key key(){return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));}

    public String generateToken(UserDetails userDetails, String encodedPassword){
        Map<String, Object> claims = new HashMap<>();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).toList();

        claims.put("password", encodedPassword);

        claims.put("userid", userRepository.findByUsername(userDetails.getUsername()).get().getId());

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(key())
                .compact();
    }

    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getEncodedPassword(String token){
        return getAllClaimsFromToken(token).get("password", String.class);
    }

//    public List<String> getRoles(String token){
//        return getAllClaimsFromToken(token).get("roles", List.class);
//    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void validateToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expirationDate = claims.getExpiration();
        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Token has expired");
        }
    }

}
