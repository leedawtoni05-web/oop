package com.rental.service;

import com.rental.model.Vehicle;
import com.rental.model.Customer;
import com.rental.pricing.PricingStrategy;

import java.time.LocalDateTime;
import java.util.*;

public class RentalService {
    private final Inventory inventory;
    private final PricingStrategy pricingStrategy;
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, RentalContract> contracts = new HashMap<>();

    public RentalService(Inventory inventory, PricingStrategy pricingStrategy) {
        this.inventory = inventory;
        this.pricingStrategy = pricingStrategy;
    }

    public void addCustomer(Customer c) { customers.put(c.getId(), c); }
    public Customer getCustomer(String id) { return customers.get(id); }
    public Collection<Customer> listCustomers() { return customers.values(); }

    public RentalContract rentVehicle(String contractId, String customerId, String vehicleId, LocalDateTime start) {
        Customer c = Objects.requireNonNull(customers.get(customerId), "Customer not found");
        Vehicle v = Objects.requireNonNull(inventory.getById(vehicleId), "Vehicle not found");
        if (!v.isAvailable()) throw new IllegalStateException("Vehicle not available");

        v.setStatus(Vehicle.Status.RENTED);
        RentalContract rc = new RentalContract(contractId, c, v, start);
        contracts.put(contractId, rc);
        return rc;
    }

    public RentalContract returnVehicle(String contractId, LocalDateTime end) {
        RentalContract rc = Objects.requireNonNull(contracts.get(contractId), "Contract not found");
        if (rc.getStatus() != RentalContract.Status.ACTIVE) throw new IllegalStateException("Contract not active");

        long days = rc.getRentalDays();
        long hours = rc.getRentalHoursRemainder();
        double price = pricingStrategy.calculatePrice(rc.getVehicle(), days, hours);

        rc.complete(end, price);
        rc.getVehicle().setStatus(Vehicle.Status.AVAILABLE);
        return rc;
    }

    public Collection<RentalContract> listContracts() { return contracts.values(); }
}
