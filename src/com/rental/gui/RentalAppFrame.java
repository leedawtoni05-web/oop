package com.rental.gui;

import com.rental.billing.CashPayment;
import com.rental.billing.Receipt;
import com.rental.model.*;
import com.rental.pricing.DefaultPricingStrategy;
import com.rental.service.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RentalAppFrame extends JFrame {
    private final Inventory inventory = new Inventory();
    private final RentalService rentalService = new RentalService(inventory, new DefaultPricingStrategy());

    private final VehiclesPanel vehiclesPanel = new VehiclesPanel(inventory);
    private final CustomersPanel customersPanel = new CustomersPanel(rentalService);
    private final RentalsPanel rentalsPanel = new RentalsPanel(rentalService, inventory);

    public RentalAppFrame() {
        super("Vehicle Rental Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        seedDemoData();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Vehicles", vehiclesPanel);
        tabs.addTab("Customers", customersPanel);
        tabs.addTab("Rentals", rentalsPanel);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    private void seedDemoData() {
        // vehicles
        inventory.addVehicle(new Car("CAR-001", "Toyota", "Vios", 2020, 500_000, 5, true));
        inventory.addVehicle(new Bike("BIKE-001", "Yamaha", "Janus", 2022, 120_000, false));
        inventory.addVehicle(new Truck("TRUCK-001", "Isuzu", "NQR75", 2019, 1_000_000, 5000));
        // customers
        customersPanel.addCustomer(new Customer("CUS-001", "Nguyen Van A", "0900000000", "a@example.com"));
        customersPanel.addCustomer(new Customer("CUS-002", "Tran Thi B", "0911111111", "b@example.com"));
    }
}
