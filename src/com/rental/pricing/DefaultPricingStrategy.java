package com.rental.pricing;

import com.rental.model.*;

public class DefaultPricingStrategy implements PricingStrategy {
    private final double hourlyFactor = 0.25; // 1 giờ ~ 1/4 ngày

    @Override
    public double calculatePrice(Vehicle vehicle, long rentalDays, long rentalHours) {
        double dayCost = vehicle.getBaseRatePerDay() * rentalDays;
        double hourCost = vehicle.getBaseRatePerDay() * hourlyFactor * rentalHours;

        double typeMultiplier = 1.0;
        if (vehicle instanceof Car) typeMultiplier = 1.1; // ô tô phụ phí
        else if (vehicle instanceof Truck) typeMultiplier = 1.3; // xe tải phụ phí
        else if (vehicle instanceof Bike) typeMultiplier = 0.8; // xe đạp/xe máy giảm

        return Math.max(0, (dayCost + hourCost) * typeMultiplier);
    }
}
