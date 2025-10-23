package com.rental.gui;

import com.rental.model.Customer;
import com.rental.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomersPanel extends JPanel {
    private final RentalService rentalService;
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Phone", "Email"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public CustomersPanel(RentalService rentalService) {
        this.rentalService = rentalService;
        setLayout(new BorderLayout());

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton addDemo = new JButton("Add Demo Customer");
        actions.add(refresh);
        actions.add(addDemo);
        add(actions, BorderLayout.NORTH);

        refresh.addActionListener(e -> reload());
        addDemo.addActionListener(e -> addCustomer(new Customer("CUS-" + (int)(Math.random()*1000), "Demo User", "0909", "demo@example.com")));

        reload();
    }

    public void addCustomer(Customer c) {
        rentalService.addCustomer(c);
        reload();
    }

    public void reload() {
        model.setRowCount(0);
        for (Customer c : rentalService.listCustomers()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
        }
    }
}
