package com.rental.gui;

import com.rental.billing.CashPayment;
import com.rental.billing.Receipt;
import com.rental.model.Vehicle;
import com.rental.service.Inventory;
import com.rental.service.RentalContract;
import com.rental.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RentalsPanel extends JPanel {
    private final RentalService rentalService;
    private final Inventory inventory;

    private final DefaultTableModel contractsModel = new DefaultTableModel(
            new Object[]{"ID", "Customer", "Vehicle", "Start", "End", "Status", "Total"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    private final JComboBox<String> customerBox = new JComboBox<>();
    private final JComboBox<String> vehicleBox = new JComboBox<>();
    private final JTextField startField = new JTextField(16);
    private final JTextField returnField = new JTextField(16);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RentalsPanel(RentalService rentalService, Inventory inventory) {
        this.rentalService = rentalService;
        this.inventory = inventory;
        setLayout(new BorderLayout());

        JTable table = new JTable(contractsModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Customer:"));
        form.add(customerBox);
        form.add(new JLabel("Vehicle:"));
        form.add(vehicleBox);
        form.add(new JLabel("Start (yyyy-MM-dd HH:mm):"));
        form.add(startField);
        form.add(new JLabel("Return (yyyy-MM-dd HH:mm):"));
        form.add(returnField);

        JButton rentBtn = new JButton("Rent");
        JButton returnBtn = new JButton("Return & Pay");
        JButton refreshBtn = new JButton("Refresh");
        form.add(rentBtn);
        form.add(returnBtn);
        form.add(refreshBtn);

        add(form, BorderLayout.NORTH);

        refreshCombos();
        startField.setText(LocalDateTime.now().format(FMT));
        returnField.setText(LocalDateTime.now().plusDays(1).format(FMT));

        rentBtn.addActionListener(e -> onRent());
        returnBtn.addActionListener(e -> onReturn(table.getSelectedRow()));
        refreshBtn.addActionListener(e -> { reloadContracts(); refreshCombos(); });

        reloadContracts();
    }

    private void refreshCombos() {
        customerBox.removeAllItems();
        rentalService.listCustomers().forEach(c -> customerBox.addItem(c.getId()));
        vehicleBox.removeAllItems();
        inventory.listAvailable().forEach(v -> vehicleBox.addItem(v.getId()));
    }

    private void onRent() {
        try {
            String customerId = (String) customerBox.getSelectedItem();
            String vehicleId = (String) vehicleBox.getSelectedItem();
            if (customerId == null || vehicleId == null) {
                JOptionPane.showMessageDialog(this, "Select customer and vehicle");
                return;
            }
            LocalDateTime start = LocalDateTime.parse(startField.getText().trim(), FMT);
            LocalDateTime expected = LocalDateTime.parse(returnField.getText().trim(), FMT);
            String id = "CT-" + (int)(Math.random()*10000);
            rentalService.rentVehicle(id, customerId, vehicleId, start);
            JOptionPane.showMessageDialog(this, "Created contract: " + id);
            reloadContracts();
            refreshCombos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onReturn(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a contract row first");
            return;
        }
        String id = (String) contractsModel.getValueAt(selectedRow, 0);
        try {
            RentalContract rc = rentalService.returnVehicle(id, LocalDateTime.now());
            CashPayment payment = new CashPayment();
            boolean ok = payment.charge(rc.getTotalCost());
            if (ok) {
                Receipt receipt = new Receipt(rc, rc.getTotalCost());
                JTextArea area = new JTextArea(receipt.format(), 20, 60);
                area.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Receipt", JOptionPane.INFORMATION_MESSAGE);
            }
            reloadContracts();
            refreshCombos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void reloadContracts() {
        contractsModel.setRowCount(0);
        var list = new ArrayList<>(rentalService.listContracts());
        for (RentalContract rc : list) {
            contractsModel.addRow(new Object[]{
                    rc.getId(),
                    rc.getCustomer().getId(),
                    rc.getVehicle().getId(),
                    rc.getStart(),
                    rc.getEnd(),
                    rc.getStatus(),
                    rc.getTotalCost()
            });
        }
    }
}
