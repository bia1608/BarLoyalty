package com.barloyalty.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
public class QRController {

    @Value("${qr.service.url:http://localhost:5000}")
    private String qrServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateQR(@RequestBody Map<String, Object> payload) {
        String qrCode = UUID.randomUUID().toString();

        Map<String, Object> qrRequest = new HashMap<>();
        qrRequest.put("qr_code", qrCode);
        qrRequest.put("client_id", payload.get("clientId"));
        qrRequest.put("bar_id", payload.get("barId"));
        qrRequest.put("points", payload.get("points"));

        try {
            restTemplate.postForEntity(qrServiceUrl + "/generate", qrRequest, String.class);

            Map<String, String> response = new HashMap<>();
            response.put("qr_code", qrCode);
            response.put("message", "QR code generated successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to generate QR code: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}