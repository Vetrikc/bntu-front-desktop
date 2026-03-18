package com.bntu;



import com.bntu.ui.MainFrame;

import javax.swing.*;

public class ClientApplication {
    public static void main(String[] args) {
        // Системный Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(MainFrame::new);
    }
}