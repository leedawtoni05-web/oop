package com.rental.gui.auth;

import com.rental.auth.AuthService;
import com.rental.auth.Role;

import javax.swing.*;
import java.awt.*;

public class RegisterDialog extends JDialog {
    private final AuthService authService;
    private final JTextField usernameField = new JTextField(16);
    private final JPasswordField passwordField = new JPasswordField(16);

    public RegisterDialog(Frame owner, AuthService authService) {
        super(owner, "Register", true);
        this.authService = authService;
        setSize(360, 200);
        setLocationRelativeTo(owner);
        buildUi();
    }

    private void buildUi() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(2,2,6,6));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton create = new JButton("Create");
        JButton cancel = new JButton("Cancel");
        actions.add(create);
        actions.add(cancel);
        add(actions, BorderLayout.SOUTH);

        create.addActionListener(e -> onCreate());
        cancel.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(create);
    }

    private void onCreate() {
        try {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());
            authService.register(u, p, Role.USER);
            JOptionPane.showMessageDialog(this, "User created. You can login now.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Register Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
