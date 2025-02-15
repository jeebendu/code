package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class WhispirService {

    @Value("${whispir.api.url}")
    private String apiUrl;

    @Value("${whispir.api.key}")
    private String apiKey;

    @Value("${whispir.template.sms}")
    private String smsTemplateId;

    @Value("${whispir.template.otp}")
    private String otpTemplateId;

    private final RestTemplate restTemplate;

    public WhispirService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sendSms(String to, String subject, String body) {
        String url = apiUrl + "/workspaces/12345678/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = String.format("{\"to\": \"%s\", \"subject\": \"%s\", \"body\": \"%s\", \"templateId\": \"%s\"}", to, subject, body, smsTemplateId);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    public String sendOtp(String to, String transactionId, String companyName) {
        String url = apiUrl + "/workspaces/12345678/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = String.format(
            "{\"to\": \"%s\", \"messageTemplateId\": \"%s\", \"messageattributes\": { \"attribute\": [ {\"name\": \"TransactionID\", \"value\": \"%s\"}, {\"name\": \"CompanyName\", \"value\": \"%s\"} ] } }",
            to, otpTemplateId, transactionId, companyName
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}