package com.rental;

import com.rental.auth.AuthService;
import com.rental.gui.MainDashboardFrame;
import com.rental.gui.auth.LoginDialog;

import javax.swing.*;

public class MainGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            AuthService auth = new AuthService();
            while (true) {
                LoginDialog dlg = new LoginDialog(null, auth);
                dlg.setVisible(true);
                var user = dlg.getResult();
                if (user == null) {
                    // canceled
                    break;
                }
                MainDashboardFrame frame = new MainDashboardFrame(user.getUsername(), user.getRole().name());
                frame.setVisible(true);
                // block until closed
                while (frame.isDisplayable() && frame.isVisible()) {
                    try { Thread.sleep(200); } catch (InterruptedException ignored2) {}
                }
                // on dispose, loop back to login for Logout flow
            }
        });
    }
}
