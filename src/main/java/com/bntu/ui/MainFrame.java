package com.bntu.ui;


import com.bntu.session.SessionManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    private final LoginPanel    loginPanel;
    private final RegisterPanel registerPanel;
    private final ProfilePanel  profilePanel;

    public static final String LOGIN    = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String PROFILE  = "PROFILE";

    public MainFrame() {
        setTitle("Auth Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 580);
        setLocationRelativeTo(null);
        setResizable(false);

        loginPanel    = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        profilePanel  = new ProfilePanel(this);

        container.add(loginPanel,    LOGIN);
        container.add(registerPanel, REGISTER);
        container.add(profilePanel,  PROFILE);

        add(container);
        showPanel(LOGIN);
        setVisible(true);
    }

    public void showPanel(String name) {
        if (name.equals(PROFILE)) {
            profilePanel.loadProfile();
        }
        cardLayout.show(container, name);
        updateTitle(name);
    }

    private void updateTitle(String panel) {
        String suffix = switch (panel) {
            case LOGIN    -> "Вход";
            case REGISTER -> "Регистрация";
            case PROFILE  -> "Профиль — " + SessionManager.getInstance().getUsername();
            default       -> "";
        };
        setTitle("Auth Client — " + suffix);
    }
}