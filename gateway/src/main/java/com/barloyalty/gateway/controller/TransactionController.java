package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.model.Transaction;
import com.barloyalty.gateway.model.User;
import com.barloyalty.gateway.repository.BarRepository;
import com.barloyalty.gateway.repository.TransactionRepository;
import com.barloyalty.gateway.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BarRepository barRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Counter transactionsConfirmedCounter;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository,
                                 UserRepository userRepository,
                                 BarRepository barRepository,
                                 SimpMessagingTemplate messagingTemplate,
                                 MeterRegistry meterRegistry) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.barRepository = barRepository;
        this.messagingTemplate = messagingTemplate;

        this.transactionsConfirmedCounter = Counter.builder("barloyalty.transactions.confirmed")
                .description("Numarul total de tranzactii confirmate cu succes")
                .register(meterRegistry);
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmTransaction(@RequestBody Map<String, Object> payload) {
        Long clientId = Long.valueOf(String.valueOf(payload.get("clientId")));
        Long barId = Long.valueOf(String.valueOf(payload.get("barId")));
        Integer points = Integer.valueOf(String.valueOf(payload.get("points")));

        Optional<User> userOptional = userRepository.findById(clientId);
        Optional<Bar> barOptional = barRepository.findById(barId);

        if (userOptional.isEmpty() || barOptional.isEmpty()) {
            return new ResponseEntity<>("Client or Bar not found", HttpStatus.BAD_REQUEST);
        }

        User user = userOptional.get();
        Bar bar = barOptional.get();

        Transaction transaction = new Transaction();
        transaction.setClient(user);
        transaction.setBar(bar);
        transaction.setPoints(points);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
        userRepository.save(user);

        this.transactionsConfirmedCounter.increment();

        String username = user.getUsername();
        String message = "Tranzactie noua! Ai primit " + points + " puncte. Total: " + user.getLoyaltyPoints();
        messagingTemplate.convertAndSend("/topic/points/" + username, message);

        System.out.println("Tranzactie confirmata pentru " + username + ". Notificare WebSocket trimisa.");

        return new ResponseEntity<>("Transaction confirmed successfully", HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Transaction> transactions = transactionRepository.findByClient(userOptional.get());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @Autowired
    private RestTemplate restTemplate;

    // Adaugă această metodă nouă în interiorul clasei TransactionController
    @PostMapping("/generate-qr")
    public ResponseEntity<String> generateQrCode(@RequestBody Map<String, Object> payload) {
        try {
            // Apelăm microserviciul Python
            String qrServiceUrl = "http://qr-service:8000/qr/generate";
            ResponseEntity<String> response = restTemplate.postForEntity(qrServiceUrl, payload, String.class);
            return response;
        } catch (RestClientException e) {
            return new ResponseEntity<>("QR Service is unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
