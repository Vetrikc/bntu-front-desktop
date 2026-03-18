package com.bntu.session;

public class SessionManager {

    private static SessionManager instance;

    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

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

    public boolean isLoggedIn() { return accessToken != null; }

    public String getAccessToken()  { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUsername()     { return username; }
    public String getRole()         { return role; }
}