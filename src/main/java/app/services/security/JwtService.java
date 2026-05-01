package app.services.security;

import app.dtos.AuthUserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Set;

public class JwtService {

    private final Key key = Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes());
//    private final Key key = Keys.hmacShaKeyFor(System.getenv("SECRET_KEY").getBytes());
//    private final String issuer = System.getenv("ISSUER_ENV");

    public String generateToken(AuthUserDTO user) {
        return Jwts.builder()
                .setSubject(user.email())
                .claim("id", user.id())
                .claim("roles", user.roles())
//                .setIssuer(System.getenv("ISSUER_ENV"))
                .setIssuedAt(new Date())
//                .setExpiration(new Date (System.currentTimeMillis() + System.getenv("TOKEN_EXPIRE_TIME"))) // 1 hour expiration
                .setExpiration(new Date(System.currentTimeMillis() + 3600000*10))
                .signWith(key)
                .compact();
    }

    public AuthUserDTO validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new AuthUserDTO(
                claims.get("id", Integer.class),
                claims.getSubject(),
                Set.copyOf((java.util.List<String>) claims.get("roles"))
        );
    }
}