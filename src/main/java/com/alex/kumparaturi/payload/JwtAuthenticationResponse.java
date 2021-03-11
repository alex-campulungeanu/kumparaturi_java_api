package com.alex.kumparaturi.payload;

public class JwtAuthenticationResponse {

    private String status;
    private String message;
    private String accessToken;
//    private String tokenType = "Bearer";
//    private String tokenType;

    public JwtAuthenticationResponse(String status, String message, String accessToken) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

//    public String getTokenType() {
//        return tokenType;
//    }

//    public void setTokenType(String tokenType) {
//        this.tokenType = tokenType;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
