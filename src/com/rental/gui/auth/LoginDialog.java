package com.rental.gui.auth;

import com.rental.auth.AuthService;
import com.rental.auth.User;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private final AuthService authService;
    private User result;

    private final JTextField usernameField = new JTextField(16);
    private final JPasswordField passwordField = new JPasswordField(16);

    public LoginDialog(Frame owner, AuthService authService) {
        super(owner, "Login", true);
        this.authService = authService;
        setSize(360, 180);
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
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        JButton cancel = new JButton("Cancel");
        actions.add(register);
        actions.add(login);
        actions.add(cancel);
        add(actions, BorderLayout.SOUTH);

        login.addActionListener(e -> onLogin());
        register.addActionListener(e -> onRegister());
        cancel.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(login);
    }

    private void onLogin() {
        try {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword());
            result = authService.login(u, p);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegister() {
        RegisterDialog dlg = new RegisterDialog((Frame) getOwner(), authService);
        dlg.setVisible(true);
    }

    public User getResult() { return result; }
}
