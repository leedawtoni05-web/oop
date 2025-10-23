package com.rental.billing;

public class CashPayment implements Payment {
    @Override
    public boolean charge(double amount) {
        // Demo: luôn thành công
        return amount >= 0;
    }
}
