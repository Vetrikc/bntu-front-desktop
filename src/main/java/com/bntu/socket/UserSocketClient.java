package com.bntu.socket;

import com.bntu.dto.ProfileRequest;
import com.bntu.dto.ProfileResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class UserSocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9091;

    private final ObjectMapper mapper = new ObjectMapper();

    private ProfileResponse send(ProfileRequest request) throws IOException {
        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8))
        ) {
            out.println(mapper.writeValueAsString(request));
            String response = in.readLine();
            return mapper.readValue(response, ProfileResponse.class);
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