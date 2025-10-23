package com.rental.model;

import java.util.Objects;

public class Customer {
    private final String id; // unique
    private String name;
    private String phone;
    private String email;

    public Customer(String id, String name, String phone, String email) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', phone='%s', email='%s'}", id, name, phone, email);
    }
}
