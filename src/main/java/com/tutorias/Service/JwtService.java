package com.tutorias.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;

/**
 * Servicio para gestionar tokens JWT
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretString;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${jwt.password-reset-expiration:1800000}")
    private Long passwordResetExpiration;

    @Value("${jwt.activation-expiration:172800000}") // 48 horas
    private Long activationExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (secretString == null || secretString.trim().isEmpty()) {
            throw new IllegalStateException("Debe configurar JWT_SECRET para iniciar la aplicación.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    /**
     * Genera un token JWT de acceso
     */
    public String generateToken(Integer userId, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("role", role);
        claims.put("purpose", "ACCESS");

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Genera un token JWT de refresh
     */
    public String generateRefreshToken(Integer userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "REFRESH");

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Token de recuperación de contraseña: de un solo uso (el jti se guarda en
     * la cuenta y se verifica al consumirlo).
     */
    public String generatePasswordResetToken(String email, String tokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "PASSWORD_RESET");

        return Jwts.builder()
                .claims(claims)
                .id(tokenId)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + passwordResetExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Token de activación de cuenta: el usuario define su contraseña con él.
     * De un solo uso y con expiración propia (48 h por defecto).
     */
    public String generateActivationToken(String email, String tokenId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "ACCOUNT_ACTIVATION");

        return Jwts.builder()
                .claims(claims)
                .id(tokenId)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + activationExpiration))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Valida un token JWT
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el email del token
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    /**
     * Extrae el userId del token
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("userId", Integer.class);
    }

    /**
     * Extrae el role del token
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }

    public String getPurposeFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("purpose", String.class);
    }

    public String getTokenIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getId();
    }

    /**
     * Verifica si el token está cerca de expirar (menos de 5 minutos)
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Date expiration = claims.getExpiration();
            long timeLeft = expiration.getTime() - System.currentTimeMillis();
            return timeLeft < (5 * 60 * 1000); // 5 minutos
        } catch (Exception e) {
            return true;
        }
    }
}
