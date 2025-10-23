package com.rental.model;

import java.util.Objects;

public abstract class Vehicle {
    public enum Status { AVAILABLE, RENTED, MAINTENANCE }

    private final String id; // unique
    private String brand;
    private String model;
    private int year;
    private double baseRatePerDay; // cơ bản theo ngày
    private Status status = Status.AVAILABLE;

    protected Vehicle(String id, String brand, String model, int year, double baseRatePerDay) {
        this.id = Objects.requireNonNull(id, "id");
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.baseRatePerDay = baseRatePerDay;
    }

    public String getId() { return id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public double getBaseRatePerDay() { return baseRatePerDay; }
    public void setBaseRatePerDay(double baseRatePerDay) { this.baseRatePerDay = baseRatePerDay; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public boolean isAvailable() { return status == Status.AVAILABLE; }

    public abstract String getType();

    @Override
    public String toString() {
        return String.format("%s{id='%s', brand='%s', model='%s', year=%d, baseRatePerDay=%.2f, status=%s}",
                getType(), id, brand, model, year, baseRatePerDay, status);
    }
}
