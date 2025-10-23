package com.rental.service;

import com.rental.model.Vehicle;

import java.util.*;

public class Inventory {
    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public void addVehicle(Vehicle v) { vehicles.put(v.getId(), v); }
    public Vehicle getById(String id) { return vehicles.get(id); }

    public List<Vehicle> listAll() { return new ArrayList<>(vehicles.values()); }

    public List<Vehicle> listAvailable() {
        List<Vehicle> res = new ArrayList<>();
        for (Vehicle v : vehicles.values()) if (v.isAvailable()) res.add(v);
        return res;
    }
}
