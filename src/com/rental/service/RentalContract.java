package com.rental.service;

import com.rental.model.Vehicle;
import com.rental.model.Customer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class RentalContract {
    public enum Status { ACTIVE, COMPLETED, CANCELED }

    private final String id; // unique
    private final Customer customer;
    private final Vehicle vehicle;
    private final LocalDateTime start;
    private LocalDateTime end; // nullable until completed
    private Status status = Status.ACTIVE;
    private double totalCost;

    public RentalContract(String id, Customer customer, Vehicle vehicle, LocalDateTime start) {
        this.id = Objects.requireNonNull(id);
        this.customer = Objects.requireNonNull(customer);
        this.vehicle = Objects.requireNonNull(vehicle);
        this.start = Objects.requireNonNull(start);
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public Status getStatus() { return status; }
    public double getTotalCost() { return totalCost; }

    public void complete(LocalDateTime end, double totalCost) {
        if (status != Status.ACTIVE) throw new IllegalStateException("Contract not active");
        this.end = Objects.requireNonNull(end);
        this.totalCost = totalCost;
        this.status = Status.COMPLETED;
    }

    public void cancel() {
        if (status != Status.ACTIVE) throw new IllegalStateException("Contract not active");
        this.status = Status.CANCELED;
    }

    public long getRentalDays() {
        LocalDateTime e = (end != null) ? end : LocalDateTime.now();
        Duration d = Duration.between(start, e);
        return d.toDays();
    }

    public long getRentalHoursRemainder() {
        LocalDateTime e = (end != null) ? end : LocalDateTime.now();
        Duration d = Duration.between(start, e);
        long hours = d.toHours();
        return hours % 24;
    }

    @Override
    public String toString() {
        return String.format("RentalContract{id='%s', customer=%s, vehicle=%s, start=%s, end=%s, status=%s, total=%.2f}",
                id, customer.getId(), vehicle.getId(), start, end, status, totalCost);
    }
}
