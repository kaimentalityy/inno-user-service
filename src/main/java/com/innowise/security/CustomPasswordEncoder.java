package com.innowise.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class CustomPasswordEncoder {

    @Value("${password.hash-algorithm}")
    private String hashAlgorithm;

    @Value("${password.salt-algorithm}")
    private String saltAlgorithm;

    @Value("${password.salt-length}")
    private int saltLength;

    @Value("${password.strength}")
    private int strength;

    private String generateSalt() throws Exception {
        SecureRandom sr = SecureRandom.getInstance(saltAlgorithm);
        byte[] salt = new byte[saltLength];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashPassword(String password, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
        md.update(salt.getBytes(StandardCharsets.UTF_8));
        byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        for (int i = 1; i < strength; i++) {
            md.update(hashedBytes);
            hashedBytes = md.digest();
        }
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    public PasswordHash encode(String password) throws Exception {
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return new PasswordHash(salt, hashedPassword);
    }

    public boolean matches(String rawPassword, PasswordHash storedPassword) throws Exception {
        String rawHashed = hashPassword(rawPassword, storedPassword.getSalt());
        return storedPassword.getHash().equals(rawHashed);
    }
}
