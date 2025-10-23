package com.rental.billing;

import com.rental.service.RentalContract;

public class Receipt {
    private final RentalContract contract;
    private final double paidAmount;

    public Receipt(RentalContract contract, double paidAmount) {
        this.contract = contract;
        this.paidAmount = paidAmount;
    }

    public String format() {
        return new StringBuilder()
                .append("==== RECEIPT ====\n")
                .append("Contract: ").append(contract.getId()).append('\n')
                .append("Customer: ").append(contract.getCustomer().getName()).append('\n')
                .append("Vehicle: ").append(contract.getVehicle().toString()).append('\n')
                .append("Start: ").append(contract.getStart()).append('\n')
                .append("End: ").append(contract.getEnd()).append('\n')
                .append("Status: ").append(contract.getStatus()).append('\n')
                .append(String.format("Total: %.2f\n", contract.getTotalCost()))
                .append(String.format("Paid: %.2f\n", paidAmount))
                .append("==================\n")
                .toString();
    }
}
