package com.bntu.socket;

import com.bntu.dto.AuthRequest;
import com.bntu.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthSocketClient {

    @Value("${auth.server.host}")
    private String host;

    @Value("${auth.server.port}")
    private int port;

    private final ObjectMapper mapper;

    private AuthResponse send(AuthRequest request) throws IOException {
        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            out.println(mapper.writeValueAsString(request));
            return mapper.readValue(in.readLine(), AuthResponse.class);
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