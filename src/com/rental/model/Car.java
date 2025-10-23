package com.rental.model;

public class Car extends Vehicle {
    private int seats;
    private boolean automatic;

    public Car(String id, String brand, String model, int year, double baseRatePerDay, int seats, boolean automatic) {
        super(id, brand, model, year, baseRatePerDay);
        this.seats = seats;
        this.automatic = automatic;
    }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public boolean isAutomatic() { return automatic; }
    public void setAutomatic(boolean automatic) { this.automatic = automatic; }

    @Override
    public String getType() { return "Car"; }
}
