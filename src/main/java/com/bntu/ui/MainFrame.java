package com.bntu.ui;

import com.bntu.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class MainFrame extends JFrame {

    private final LoginPanel    loginPanel;
    private final RegisterPanel registerPanel;
    private final ProfilePanel  profilePanel;
    private final SessionManager session;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    public static final String LOGIN    = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String PROFILE  = "PROFILE";

    public void init() {
        setTitle("BNTU Auth Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        container.add(loginPanel,    LOGIN);
        container.add(registerPanel, REGISTER);
        container.add(profilePanel,  PROFILE);

        add(container);
        showPanel(LOGIN);
        setVisible(true);
    }

    public void showPanel(String name) {
        if (PROFILE.equals(name)) {
            profilePanel.loadProfile();
        }
        cardLayout.show(container, name);
        updateTitle(name);
    }

    private void updateTitle(String panel) {
        String suffix = switch (panel) {
            case LOGIN    -> "Вход";
            case REGISTER -> "Регистрация";
            case PROFILE  -> "Профиль — " + session.getUsername();
            default       -> "";
        };
        setTitle("BNTU Auth Client — " + suffix);
    }
}