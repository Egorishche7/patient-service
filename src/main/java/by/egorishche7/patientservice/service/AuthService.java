package by.egorishche7.patientservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value("${practitioner.username}")
    private String username;

    @Value("${practitioner.password}")
    private String password;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        String tokenUri = issuerUri + "/protocol/openid-connect/token";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("grant_type", "password");

        var response = restTemplate.postForObject(tokenUri, requestBody, Map.class);

        return response != null ? response.get("access_token").toString() : null;
    }
}
