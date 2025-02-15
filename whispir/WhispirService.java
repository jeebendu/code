package com.example.service;

import com.example.dto.WhispirMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

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

        WhispirMessageRequest request = new WhispirMessageRequest();
        request.setTo(to);
        request.setMessageTemplateId(smsTemplateId);
        WhispirMessageRequest.MessageAttributes attributes = new WhispirMessageRequest.MessageAttributes();
        attributes.setAttribute(Arrays.asList(
            new WhispirMessageRequest.Attribute("Subject", subject),
            new WhispirMessageRequest.Attribute("Body", body)
        ));
        request.setMessageattributes(attributes);

        HttpEntity<WhispirMessageRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    public String sendOtp(String to, String transactionId, String companyName) {
        String url = apiUrl + "/workspaces/12345678/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        WhispirMessageRequest request = new WhispirMessageRequest();
        request.setTo(to);
        request.setMessageTemplateId(otpTemplateId);
        WhispirMessageRequest.MessageAttributes attributes = new WhispirMessageRequest.MessageAttributes();
        attributes.setAttribute(Arrays.asList(
            new WhispirMessageRequest.Attribute("TransactionID", transactionId),
            new WhispirMessageRequest.Attribute("CompanyName", companyName)
        ));
        request.setMessageattributes(attributes);

        HttpEntity<WhispirMessageRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}