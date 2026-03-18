package com.bntu.ui;


import com.bntu.dto.AuthResponse;
import com.bntu.socket.AuthSocketClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class RegisterPanel extends JPanel {

    private final AuthSocketClient authClient;
    private MainFrame frame;

    private final JTextField     usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField  = new JPasswordField();
    private final JComboBox<String> roleBox    = new JComboBox<>(new String[]{"USER", "ADMIN"});
    private final JLabel errorLabel            = new JLabel(" ");
    private final JLabel successLabel          = new JLabel(" ");

    @PostConstruct
    public void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(40, 50, 40, 50));
        setBackground(Color.WHITE);

        add(buildTitle(),  BorderLayout.NORTH);
        add(buildForm(),   BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    private JLabel buildTitle() {
        JLabel title = new JLabel("Регистрация", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        return title;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 8));
        form.setBackground(Color.WHITE);

        form.add(label("Логин"));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(usernameField);

        form.add(label("Пароль (минимум 6 символов)"));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(passwordField);

        form.add(label("Подтвердите пароль"));
        confirmField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(confirmField);

        form.add(label("Роль"));
        roleBox.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(roleBox);

        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        form.add(errorLabel);

        successLabel.setForeground(new Color(0, 150, 0));
        successLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        form.add(successLabel);

        JButton registerBtn = new JButton("Зарегистрироваться");
        styleButton(registerBtn, new Color(52, 120, 246));
        registerBtn.addActionListener(e -> doRegister());
        form.add(registerBtn);

        return form;
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel hint = new JLabel("Уже есть аккаунт?");
        hint.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton toLogin = new JButton("Войти");
        toLogin.setBorderPainted(false);
        toLogin.setContentAreaFilled(false);
        toLogin.setForeground(new Color(52, 120, 246));
        toLogin.setFont(new Font("Arial", Font.BOLD, 13));
        toLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toLogin.addActionListener(e -> frame.showPanel(MainFrame.LOGIN));

        bottom.add(hint);
        bottom.add(toLogin);
        return bottom;
    }

    private void doRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmField.getPassword());
        String role     = (String) roleBox.getSelectedItem();

        errorLabel.setText(" ");
        successLabel.setText(" ");

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Заполните все поля");
            return;
        }
        if (password.length() < 6) {
            errorLabel.setText("Пароль минимум 6 символов");
            return;
        }
        if (!password.equals(confirm)) {
            errorLabel.setText("Пароли не совпадают");
            return;
        }

        errorLabel.setText("Регистрация...");

        new SwingWorker<AuthResponse, Void>() {
            @Override
            protected AuthResponse doInBackground() throws Exception {
                return authClient.register(username, password, role);
            }

            @Override
            protected void done() {
                try {
                    AuthResponse resp = get();
                    if (resp.isSuccess()) {
                        successLabel.setText("✓ " + resp.getMessage());
                        errorLabel.setText(" ");
                        usernameField.setText("");
                        passwordField.setText("");
                        confirmField.setText("");
                        Timer timer = new Timer(1500, ev -> frame.showPanel(MainFrame.LOGIN));
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        errorLabel.setText(resp.getMessage());
                    }
                } catch (Exception ex) {
                    errorLabel.setText("Ошибка подключения к серверу");
                }
            }
        }.execute();
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