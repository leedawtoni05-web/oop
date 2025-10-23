package com.rental.gui;

import com.rental.model.*;
import com.rental.service.Inventory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehiclesPanel extends JPanel {
    private final Inventory inventory;
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Type", "Brand", "Model", "Year", "BaseRate", "Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public VehiclesPanel(Inventory inventory) {
        this.inventory = inventory;
        setLayout(new BorderLayout());

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        JButton addDemo = new JButton("Add Demo Vehicle");
        actions.add(refresh);
        actions.add(addDemo);
        add(actions, BorderLayout.NORTH);

        refresh.addActionListener(e -> reload());
        addDemo.addActionListener(e -> {
            String id = "CAR-" + (int)(Math.random()*1000);
            inventory.addVehicle(new Car(id, "Hyundai", "i10", 2021, 450_000, 5, true));
            reload();
        });

        reload();
    }

    public void reload() {
        model.setRowCount(0);
        List<Vehicle> list = inventory.listAll();
        for (Vehicle v : list) {
            model.addRow(new Object[]{v.getId(), v.getType(), v.getBrand(), v.getModel(), v.getYear(), v.getBaseRatePerDay(), v.getStatus()});
        }
    }
}
