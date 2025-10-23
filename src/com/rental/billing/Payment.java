package com.rental.billing;

public interface Payment {
    boolean charge(double amount);
}
