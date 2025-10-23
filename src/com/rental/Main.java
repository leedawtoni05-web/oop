package com.rental;

import com.rental.billing.CashPayment;
import com.rental.billing.Receipt;
import com.rental.model.*;
import com.rental.pricing.DefaultPricingStrategy;
import com.rental.service.*;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo hạ tầng
        Inventory inventory = new Inventory();
        inventory.addVehicle(new Car("CAR-001", "Toyota", "Vios", 2020, 500_000, 5, true));
        inventory.addVehicle(new Bike("BIKE-001", "Yamaha", "Janus", 2022, 120_000, false));
        inventory.addVehicle(new Truck("TRUCK-001", "Isuzu", "NQR75", 2019, 1_000_000, 5000));

        RentalService rentalService = new RentalService(inventory, new DefaultPricingStrategy());
        rentalService.addCustomer(new Customer("CUS-001", "Nguyen Van A", "0900000000", "a@example.com"));

        // Tạo hợp đồng thuê
        LocalDateTime start = LocalDateTime.now().minusDays(1).minusHours(6); // 1 ngày 6 giờ
        RentalContract contract = rentalService.rentVehicle("CT-001", "CUS-001", "CAR-001", start);

        // Trả xe và tính tiền
        RentalContract completed = rentalService.returnVehicle("CT-001", LocalDateTime.now());

        // Thanh toán và in biên nhận
        CashPayment payment = new CashPayment();
        boolean success = payment.charge(completed.getTotalCost());
        if (success) {
            Receipt receipt = new Receipt(completed, completed.getTotalCost());
            System.out.println(receipt.format());
        } else {
            System.out.println("Payment failed!");
        }

        // Báo cáo nhanh
        System.out.println("Vehicles available: " + inventory.listAvailable().size());
        System.out.println("All contracts: ");
        for (RentalContract rc : rentalService.listContracts()) {
            System.out.println(rc);
        }
    }
}
