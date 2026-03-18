package com.bntu.socket;


import com.bntu.dto.AuthRequest;
import com.bntu.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class AuthSocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    private final ObjectMapper mapper = new ObjectMapper();

    private AuthResponse send(AuthRequest request) throws IOException {
        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            out.println(mapper.writeValueAsString(request));
            String response = in.readLine();
            return mapper.readValue(response, AuthResponse.class);
        }
    }

    public AuthResponse register(String username, String password, String role) throws IOException {
        AuthRequest req = new AuthRequest();
        req.setAction("REGISTER");
        req.setUsername(username);
        req.setPassword(password);
        req.setRole(role);
        return send(req);
    }

    public AuthResponse login(String username, String password) throws IOException {
        AuthRequest req = new AuthRequest();
        req.setAction("LOGIN");
        req.setUsername(username);
        req.setPassword(password);
        return send(req);
    }

    public AuthResponse logout(String refreshToken) throws IOException {
        AuthRequest req = new AuthRequest();
        req.setAction("LOGOUT");
        req.setRefreshToken(refreshToken);
        return send(req);
    }
}