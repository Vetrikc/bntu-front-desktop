package com.bntu;


import com.bntu.ui.LoginPanel;
import com.bntu.ui.MainFrame;
import com.bntu.ui.ProfilePanel;
import com.bntu.ui.RegisterPanel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        // headless=false — обязательно для Swing
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ClientApplication.class)
                .headless(false)
                .run(args);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame    = context.getBean(MainFrame.class);
            LoginPanel login    = context.getBean(LoginPanel.class);
            RegisterPanel register = context.getBean(RegisterPanel.class);
            ProfilePanel profile  = context.getBean(ProfilePanel.class);

            // Передаём ссылку на MainFrame в каждую панель
            login.setFrame(frame);
            register.setFrame(frame);
            profile.setFrame(frame);

            frame.init();
        });
    }
}