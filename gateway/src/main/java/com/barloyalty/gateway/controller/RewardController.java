package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Reward;
import com.barloyalty.gateway.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardRepository rewardRepository;

    @Autowired
    public RewardController(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    @GetMapping
    public ResponseEntity<List<Reward>> getAllRewards() {
        List<Reward> rewards = rewardRepository.findAll();
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping("/bar/{barId}")
    public ResponseEntity<List<Reward>> getRewardsByBar(@PathVariable Long barId) {
        List<Reward> rewards = rewardRepository.findByBarId(barId);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reward> createReward(@RequestBody Reward reward) {
        Reward savedReward = rewardRepository.save(reward);
        return new ResponseEntity<>(savedReward, HttpStatus.CREATED);
    }
}