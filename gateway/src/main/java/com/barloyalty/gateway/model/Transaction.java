package com.barloyalty.gateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int points;
    private LocalDateTime timestamp;
    @ManyToOne
    private User client;
    @ManyToOne
    private Bar bar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public int getPoints() {
        return points;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getClient() {
        return client;
    }

    public Bar getBar() {
        return bar;
    }
// Adaugă Getters și Setters pentru toate câmpurile
}