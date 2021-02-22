package com.oauth.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Component
public class PropertiesUtils {

    @Value("${authentication.oauth.clientId}")
    private String clientId;

    @Value("${authentication.oauth.secret}")
    private String secret;

    @Value("${authentication.oauth.token}")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
