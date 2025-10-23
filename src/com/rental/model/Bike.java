package com.rental.model;

public class Bike extends Vehicle {
    private boolean electric;

    public Bike(String id, String brand, String model, int year, double baseRatePerDay, boolean electric) {
        super(id, brand, model, year, baseRatePerDay);
        this.electric = electric;
    }

    public boolean isElectric() { return electric; }
    public void setElectric(boolean electric) { this.electric = electric; }

    @Override
    public String getType() { return "Bike"; }
}
