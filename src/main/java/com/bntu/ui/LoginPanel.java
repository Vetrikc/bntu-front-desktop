package com.bntu.ui;



import com.bntu.dto.AuthResponse;
import com.bntu.session.SessionManager;
import com.bntu.socket.AuthSocketClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final MainFrame frame;
    private final AuthSocketClient authClient = new AuthSocketClient();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel errorLabel = new JLabel(" ");

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 50, 40, 50));
        setBackground(Color.WHITE);

        add(buildTitle(),  BorderLayout.NORTH);
        add(buildForm(),   BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("Вход в систему", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        return title;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 10));
        form.setBackground(Color.WHITE);

        form.add(label("Логин"));
        usernameField.setPreferredSize(new Dimension(0, 36));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(usernameField);

        form.add(label("Пароль"));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(passwordField);

        form.add(Box.createVerticalStrut(5));

        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        form.add(errorLabel);

        JButton loginBtn = new JButton("Войти");
        styleButton(loginBtn, new Color(52, 120, 246));
        loginBtn.addActionListener(e -> doLogin());
        form.add(loginBtn);

        // Enter по нажатию
        passwordField.addActionListener(e -> doLogin());

        return form;
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel hint = new JLabel("Нет аккаунта?");
        hint.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton toRegister = new JButton("Зарегистрироваться");
        toRegister.setBorderPainted(false);
        toRegister.setContentAreaFilled(false);
        toRegister.setForeground(new Color(52, 120, 246));
        toRegister.setFont(new Font("Arial", Font.BOLD, 13));
        toRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toRegister.addActionListener(e -> frame.showPanel(MainFrame.REGISTER));

        bottom.add(hint);
        bottom.add(toRegister);
        return bottom;
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Заполните все поля");
            return;
        }

        errorLabel.setText("Подключение...");

        SwingWorker<AuthResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected AuthResponse doInBackground() throws Exception {
                return authClient.login(username, password);
            }

            @Override
            protected void done() {
                try {
                    AuthResponse resp = get();
                    if (resp.isSuccess()) {
                        SessionManager.getInstance().save(
                            resp.getAccessToken(), resp.getRefreshToken(),
                            resp.getUsername(), resp.getRole());
                        passwordField.setText("");
                        errorLabel.setText(" ");
                        frame.showPanel(MainFrame.PROFILE);
                    } else {
                        errorLabel.setText(resp.getMessage());
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Ошибка подключения к серверу");
                }
            }
        };
        worker.execute();
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        return l;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}