package com.rental.auth;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class AuthService {
    private final AuthDao dao = new AuthDao();
    private final SecureRandom random = new SecureRandom();

    public AuthService() { }

    public synchronized User register(String username, String plainPassword, Role role) {
        try {
            if (username == null || username.isBlank()) throw new IllegalArgumentException("Username required");
            if (plainPassword == null || plainPassword.length() < 3) throw new IllegalArgumentException("Password too short");
            if (dao.findByUsername(username) != null) throw new IllegalArgumentException("Username already exists");
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String saltHex = toHex(salt);
            String hash = hashPassword(plainPassword, saltHex);
            User u = new User(username, hash, saltHex, role);
            dao.upsertUser(u);
            return u;
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
            throw new RuntimeException(e);
        }
    }

    public synchronized User login(String username, String plainPassword) {
        try {
            User u = dao.findByUsername(username);
            if (u == null) throw new IllegalArgumentException("Invalid username/password");
            String hash = hashPassword(plainPassword, u.getSalt());
            if (!hash.equals(u.getPasswordHash())) throw new IllegalArgumentException("Invalid username/password");
            return u;
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
            throw new RuntimeException(e);
        }
    }

    private static String hashPassword(String plain, String saltHex) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(fromHex(saltHex));
            byte[] digest = md.digest(plain.getBytes("UTF-8"));
            return toHex(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}
