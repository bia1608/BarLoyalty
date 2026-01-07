package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.model.Reward;
import com.barloyalty.gateway.repository.BarRepository;
import com.barloyalty.gateway.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private BarRepository barRepository; // Avem nevoie și de BarRepository pentru a lega recompensele de baruri

    // Metoda pentru a adăuga o recompensă nouă la un bar (HTTP POST)
    @PostMapping("/bar/{barId}")
    public ResponseEntity<Reward> createReward(@PathVariable Long barId, @RequestBody Reward reward) {
        Optional<Bar> barOptional = barRepository.findById(barId);
        if (barOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Dacă barul nu există, returnăm 404
        }
        Bar bar = barOptional.get();
        reward.setBar(bar); // Legăm recompensa de barul găsit
        Reward savedReward = rewardRepository.save(reward);
        return new ResponseEntity<>(savedReward, HttpStatus.CREATED); // Returnăm recompensa salvată cu status 201 Created
    }

    // Metoda pentru a vedea toate recompensele pentru un anumit bar (HTTP GET)
    @GetMapping("/bar/{barId}")
    public ResponseEntity<List<Reward>> getRewardsByBar(@PathVariable Long barId) {
        Optional<Bar> barOptional = barRepository.findById(barId);
        if (barOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Dacă barul nu există, returnăm 404
        }
        List<Reward> rewards = rewardRepository.findByBar(barOptional.get());
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    // Metoda pentru a vedea toate recompensele din toate barurile (HTTP GET)
    @GetMapping
    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }
}