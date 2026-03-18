package com.bntu.socket;

import com.bntu.dto.ProfileRequest;
import com.bntu.dto.ProfileResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class UserSocketClient {

    @Value("${user.server.host}")
    private String host;

    @Value("${user.server.port}")
    private int port;

    private final ObjectMapper mapper;

    private ProfileResponse send(ProfileRequest request) throws IOException {
        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(
                        socket.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            out.println(mapper.writeValueAsString(request));
            return mapper.readValue(in.readLine(), ProfileResponse.class);
        }
    }

    public ProfileResponse getProfile(String token) throws IOException {
        ProfileRequest req = new ProfileRequest();
        req.setAction("GET_PROFILE");
        req.setToken(token);
        return send(req);
    }

    public ProfileResponse createProfile(String token, String firstName, String lastName,
                                         String email, String phone,
                                         String birthDate, String bio) throws IOException {
        ProfileRequest req = new ProfileRequest();
        req.setAction("CREATE_PROFILE");
        req.setToken(token);
        req.setFirstName(firstName);
        req.setLastName(lastName);
        req.setEmail(email);
        req.setPhone(phone);
        req.setBirthDate(birthDate);
        req.setBio(bio);
        return send(req);
    }

    public ProfileResponse updateProfile(String token, String firstName, String lastName,
                                         String email, String phone,
                                         String birthDate, String bio) throws IOException {
        ProfileRequest req = new ProfileRequest();
        req.setAction("UPDATE_PROFILE");
        req.setToken(token);
        req.setFirstName(firstName.isBlank() ? null : firstName);
        req.setLastName(lastName.isBlank() ? null : lastName);
        req.setEmail(email.isBlank() ? null : email);
        req.setPhone(phone.isBlank() ? null : phone);
        req.setBirthDate(birthDate.isBlank() ? null : birthDate);
        req.setBio(bio.isBlank() ? null : bio);
        return send(req);
    }
}