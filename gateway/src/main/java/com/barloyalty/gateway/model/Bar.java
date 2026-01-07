package com.barloyalty.gateway.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bars")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String ownerEmail;

    @OneToMany(mappedBy = "bar")
    private List<Reward> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "bar")
    private List<Transaction> transactions = new ArrayList<>();

    public Bar() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public List<Reward> getRewards() { return rewards; }
    public void setRewards(List<Reward> rewards) { this.rewards = rewards; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}