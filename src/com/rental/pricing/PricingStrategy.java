package com.rental.pricing;

import com.rental.model.Vehicle;

public interface PricingStrategy {
    double calculatePrice(Vehicle vehicle, long rentalDays, long rentalHours);
}
