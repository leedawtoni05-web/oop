package com.rental.model;

public class Truck extends Vehicle {
    private double payloadCapacityKg;

    public Truck(String id, String brand, String model, int year, double baseRatePerDay, double payloadCapacityKg) {
        super(id, brand, model, year, baseRatePerDay);
        this.payloadCapacityKg = payloadCapacityKg;
    }

    public double getPayloadCapacityKg() { return payloadCapacityKg; }
    public void setPayloadCapacityKg(double payloadCapacityKg) { this.payloadCapacityKg = payloadCapacityKg; }

    @Override
    public String getType() { return "Truck"; }
}
