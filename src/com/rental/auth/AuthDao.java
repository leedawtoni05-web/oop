package com.rental.auth;

import com.rental.db.Db;

import java.sql.*;

public class AuthDao {
    public void upsertUser(User u) throws SQLException {
        String sql = "INSERT INTO users(username, password_hash, salt, role) VALUES(?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE password_hash=VALUES(password_hash), salt=VALUES(salt), role=VALUES(role)";
        try (PreparedStatement ps = Db.get().prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getSalt());
            ps.setString(4, u.getRole().name());
            ps.executeUpdate();
        }
    }

    public User findByUsername(String username) throws SQLException {
        try (PreparedStatement ps = Db.get().prepareStatement("SELECT username,password_hash,salt,role FROM users WHERE username=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            Role.valueOf(rs.getString(4))
                    );
                }
            }
        }
        return null;
    }
}
