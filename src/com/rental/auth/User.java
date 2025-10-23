package com.rental.auth;

public class User {
    private final String username;
    private final String passwordHash; // hex
    private final String salt; // hex
    private final Role role;

    public User(String username, String passwordHash, String salt, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getSalt() { return salt; }
    public Role getRole() { return role; }
}
