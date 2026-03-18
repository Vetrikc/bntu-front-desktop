package com.bntu.session;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SessionManager {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;

    public void save(String accessToken, String refreshToken,
                     String username, String role) {
        this.accessToken  = accessToken;
        this.refreshToken = refreshToken;
        this.username     = username;
        this.role         = role;
    }

    public void clear() {
        this.accessToken  = null;
        this.refreshToken = null;
        this.username     = null;
        this.role         = null;
    }

    public boolean isLoggedIn() {
        return accessToken != null;
    }
}