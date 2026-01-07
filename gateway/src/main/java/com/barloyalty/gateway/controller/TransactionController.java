package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.model.Transaction;
import com.barloyalty.gateway.model.User;
import com.barloyalty.gateway.repository.BarRepository;
import com.barloyalty.gateway.repository.TransactionRepository;
import com.barloyalty.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BarRepository barRepository;

    // Acest obiectwsl este "poștașul" pentru WebSockets
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint-ul pe care îl va apela microserviciul Python
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmTransaction(@RequestBody Map<String, Long> payload) {
        Long clientId = payload.get("clientId");
        Long barId = payload.get("barId");
        Long points = payload.get("points");

        // Căutăm clientul și barul în baza de date
        Optional<User> userOptional = userRepository.findById(clientId);
        Optional<Bar> barOptional = barRepository.findById(barId);

        if (userOptional.isEmpty() || barOptional.isEmpty()) {
            return new ResponseEntity<>("Client or Bar not found", HttpStatus.NOT_FOUND);
        }

        // Creăm o nouă tranzacție
        Transaction transaction = new Transaction();
        transaction.setClient(userOptional.get());
        transaction.setBar(barOptional.get());
        transaction.setPoints(points.intValue());
        transaction.setTimestamp(LocalDateTime.now());

        // Salvăm tranzacția în baza de date
        transactionRepository.save(transaction);

        // AICI ESTE MAGIA WEBSOCKET
        // Trimitem un mesaj pe un canal specific clientului
        // Canalul este de forma /topic/points/username
        String username = userOptional.get().getUsername();
        String message = "Tranzactie noua! Ai primit " + points + " puncte.";
        messagingTemplate.convertAndSend("/topic/points/" + username, message);

        System.out.println("Tranzactie confirmata pentru " + username + ". Notificare WebSocket trimisa.");

        return new ResponseEntity<>("Transaction confirmed successfully", HttpStatus.OK);
    }
}