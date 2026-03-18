package com.bntu.ui;


import com.bntu.dto.ProfileResponse;
import com.bntu.session.SessionManager;
import com.bntu.socket.AuthSocketClient;
import com.bntu.socket.UserSocketClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class ProfilePanel extends JPanel {

    private final UserSocketClient userClient;
    private final AuthSocketClient authClient;
    private final SessionManager session;
    @Setter
    private MainFrame frame;

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField birthDateField = new JTextField();
    private final JTextArea bioField = new JTextArea(3, 0);
    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel userInfoLabel = new JLabel(" ");

    private boolean profileExists = false;

    @PostConstruct
    public void init() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel title = new JLabel("Мой профиль", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userInfoLabel.setForeground(Color.GRAY);
        userInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(title, BorderLayout.CENTER);
        header.add(userInfoLabel, BorderLayout.SOUTH);
        return header;
    }

    private JScrollPane buildForm() {
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.setBackground(Color.WHITE);

        form.add(label("Имя"));
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(firstNameField);

        form.add(label("Фамилия"));
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(lastNameField);

        form.add(label("Email"));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(emailField);

        form.add(label("Телефон"));
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(phoneField);

        form.add(label("Дата рождения (yyyy-MM-dd)"));
        birthDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        form.add(birthDateField);

        form.add(label("О себе"));
        bioField.setFont(new Font("Arial", Font.PLAIN, 14));
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        bioField.setBorder(firstNameField.getBorder());
        form.add(new JScrollPane(bioField));

        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        form.add(statusLabel);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        return scroll;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 2, 10, 0));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton saveBtn = new JButton("Сохранить");
        styleButton(saveBtn, new Color(52, 120, 246));
        saveBtn.addActionListener(e -> doSave());

        JButton logoutBtn = new JButton("Выйти");
        styleButton(logoutBtn, new Color(200, 60, 60));
        logoutBtn.addActionListener(e -> doLogout());

        footer.add(saveBtn);
        footer.add(logoutBtn);
        return footer;
    }

    public void loadProfile() {
        userInfoLabel.setText("@" + session.getUsername() + "  |  " + session.getRole());
        statusLabel.setText("Загрузка...");
        statusLabel.setForeground(Color.GRAY);

        new SwingWorker<ProfileResponse, Void>() {
            @Override
            protected ProfileResponse doInBackground() throws Exception {
                return userClient.getProfile(session.getAccessToken());
            }

            @Override
            protected void done() {
                try {
                    ProfileResponse resp = get();
                    if (resp.isSuccess()) {
                        profileExists = true;
                        fillFields(resp);
                        statusLabel.setText("✓ Профиль загружен");
                        statusLabel.setForeground(new Color(0, 150, 0));
                    } else {
                        profileExists = false;
                        statusLabel.setText("Профиль не найден — заполни и нажми Сохранить");
                        statusLabel.setForeground(Color.GRAY);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Ошибка подключения к user-service");
                    statusLabel.setForeground(Color.RED);
                }
            }
        }.execute();
    }

    private void doSave() {
        String token = session.getAccessToken();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String birthDate = birthDateField.getText().trim();
        String bio = bioField.getText().trim();

        statusLabel.setText("Сохранение...");
        statusLabel.setForeground(Color.GRAY);

        new SwingWorker<ProfileResponse, Void>() {
            @Override
            protected ProfileResponse doInBackground() throws Exception {
                return profileExists
                        ? userClient.updateProfile(token, firstName, lastName, email, phone, birthDate, bio)
                        : userClient.createProfile(token, firstName, lastName, email, phone, birthDate, bio);
            }

            @Override
            protected void done() {
                try {
                    ProfileResponse resp = get();
                    if (resp.isSuccess()) {
                        profileExists = true;
                        fillFields(resp);
                        statusLabel.setText("✓ " + resp.getMessage());
                        statusLabel.setForeground(new Color(0, 150, 0));
                    } else {
                        statusLabel.setText(resp.getMessage());
                        statusLabel.setForeground(Color.RED);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Ошибка подключения к user-service");
                    statusLabel.setForeground(Color.RED);
                }
            }
        }.execute();
    }

    private void doLogout() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                authClient.logout(session.getRefreshToken());
                return null;
            }

            @Override
            protected void done() {
                session.clear();
                clearFields();
                frame.showPanel(MainFrame.LOGIN);
            }
        }.execute();
    }

    private void fillFields(ProfileResponse p) {
        firstNameField.setText(p.getFirstName() != null ? p.getFirstName() : "");
        lastNameField.setText(p.getLastName() != null ? p.getLastName() : "");
        emailField.setText(p.getEmail() != null ? p.getEmail() : "");
        phoneField.setText(p.getPhone() != null ? p.getPhone() : "");
        birthDateField.setText(p.getBirthDate() != null ? p.getBirthDate() : "");
        bioField.setText(p.getBio() != null ? p.getBio() : "");
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        birthDateField.setText("");
        bioField.setText("");
        statusLabel.setText(" ");
        profileExists = false;
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