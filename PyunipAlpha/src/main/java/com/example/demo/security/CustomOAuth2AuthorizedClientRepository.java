package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

    private final Map<String, OAuth2AuthorizedClient> authorizedClients = new HashMap<>();

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request) {
        String principalName = (principal != null) ? principal.getName() : clientRegistrationId;
//        System.out.println("Loading client for: " + clientRegistrationId + ":" + principalName);
        return (T) authorizedClients.get(clientRegistrationId + ":" + principalName);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
        String principalName = (principal != null) ? principal.getName() : authorizedClient.getClientRegistration().getRegistrationId();
//        System.out.println("Saving client for: " + authorizedClient.getClientRegistration().getRegistrationId() + ":" + principalName);
        authorizedClients.put(authorizedClient.getClientRegistration().getRegistrationId() + ":" + principalName, authorizedClient);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request, HttpServletResponse response) {
        String principalName = (principal != null) ? principal.getName() : clientRegistrationId;
//        System.out.println("Removing client for: " + clientRegistrationId + ":" + principalName);
        authorizedClients.remove(clientRegistrationId + ":" + principalName);
    }

    public OAuth2RefreshToken getRefreshToken(String clientRegistrationId, String principalName) {
        OAuth2AuthorizedClient client = authorizedClients.get(clientRegistrationId + ":" + principalName);
//        System.out.println("client: " + client);
        return client != null ? client.getRefreshToken() : null;
    }
}