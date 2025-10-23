package com.rental.gui;

import com.rental.model.Bike;
import com.rental.model.Car;
import com.rental.model.Customer;
import com.rental.model.Truck;
import com.rental.pricing.DefaultPricingStrategy;
import com.rental.service.Inventory;
import com.rental.service.RentalService;

import javax.swing.*;
import java.awt.*;

public class MainDashboardFrame extends JFrame {
    private final Inventory inventory = new Inventory();
    private final RentalService rentalService = new RentalService(inventory, new DefaultPricingStrategy());

    private final VehiclesPanel vehiclesPanel = new VehiclesPanel(inventory);
    private final CustomersPanel customersPanel = new CustomersPanel(rentalService);
    private final RentalsPanel rentalsPanel = new RentalsPanel(rentalService, inventory);

    private final JLabel statusBar = new JLabel(" Ready");
    private final JTabbedPane tabs = new JTabbedPane();

    private final String currentUsername;
    private final String currentRole;

    public MainDashboardFrame(String username, String role) {
        super("Vehicle Rental Dashboard");
        this.currentUsername = username;
        this.currentRole = role;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        seedDemoData();
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout());

        // Tabs reuse existing panels
        tabs.addTab("Vehicles", vehiclesPanel);
        tabs.addTab("Customers", customersPanel);
        tabs.addTab("Rentals", rentalsPanel);

        add(tabs, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        statusBar.setText(" Logged in as: " + currentUsername + " (" + currentRole + ")");

        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> {
            dispose();
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        file.add(logout);
        file.add(exit);

        JMenu view = new JMenu("View");
        JMenuItem vVehicles = new JMenuItem("Vehicles");
        vVehicles.addActionListener(e -> selectTab(0, "Vehicles"));
        JMenuItem vCustomers = new JMenuItem("Customers");
        vCustomers.addActionListener(e -> selectTab(1, "Customers"));
        JMenuItem vRentals = new JMenuItem("Rentals");
        vRentals.addActionListener(e -> selectTab(2, "Rentals"));
        view.add(vVehicles);
        view.add(vCustomers);
        view.add(vRentals);

        JMenu actions = new JMenu("Actions");
        JMenuItem refresh = new JMenuItem("Refresh All");
        refresh.addActionListener(e -> refreshAll());
        actions.add(refresh);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Vehicle Rental Dashboard\nSimple Swing UI demo.",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file);
        bar.add(view);
        bar.add(actions);
        bar.add(help);
        return bar;
    }

    private JToolBar createToolBar() {
        JToolBar tb = new JToolBar();
        JButton btnVehicles = new JButton("Vehicles");
        JButton btnCustomers = new JButton("Customers");
        JButton btnRentals = new JButton("Rentals");
        JButton btnRefresh = new JButton("Refresh");

        btnVehicles.addActionListener(e -> selectTab(0, "Vehicles"));
        btnCustomers.addActionListener(e -> selectTab(1, "Customers"));
        btnRentals.addActionListener(e -> selectTab(2, "Rentals"));
        btnRefresh.addActionListener(e -> refreshAll());

        tb.add(btnVehicles);
        tb.add(btnCustomers);
        tb.add(btnRentals);
        tb.addSeparator();
        tb.add(btnRefresh);
        return tb;
    }

    private void selectTab(int idx, String name) {
        tabs.setSelectedIndex(idx);
        setStatus("Switched to " + name);
    }

    private void refreshAll() {
        vehiclesPanel.reload();
        customersPanel.reload();
        // rentalsPanel has its own reload method
        try {
            var m = RentalsPanel.class.getDeclaredMethod("reloadContracts");
            m.setAccessible(true);
            m.invoke(rentalsPanel);
        } catch (Exception ignored) {}
        setStatus("Refreshed");
    }

    private void setStatus(String msg) { statusBar.setText(" " + msg); }

    private void seedDemoData() {
        inventory.addVehicle(new Car("CAR-001", "Toyota", "Vios", 2020, 500_000, 5, true));
        inventory.addVehicle(new Bike("BIKE-001", "Yamaha", "Janus", 2022, 120_000, false));
        inventory.addVehicle(new Truck("TRUCK-001", "Isuzu", "NQR75", 2019, 1_000_000, 5000));
        customersPanel.addCustomer(new Customer("CUS-001", "Nguyen Van A", "0900000000", "a@example.com"));
        customersPanel.addCustomer(new Customer("CUS-002", "Tran Thi B", "0911111111", "b@example.com"));
    }
}
