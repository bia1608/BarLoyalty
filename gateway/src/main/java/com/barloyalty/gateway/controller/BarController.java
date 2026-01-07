package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.repository.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bars")
public class BarController {

    private final BarRepository barRepository;

    @Autowired
    public BarController(BarRepository barRepository) {
        this.barRepository = barRepository;
    }

    @GetMapping
    public ResponseEntity<List<Bar>> getAllBars() {
        List<Bar> bars = barRepository.findAll();
        return new ResponseEntity<>(bars, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bar> getBarById(@PathVariable Long id) {
        return barRepository.findById(id)
                .map(bar -> new ResponseEntity<>(bar, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Bar> createBar(@RequestBody Bar bar) {
        Bar savedBar = barRepository.save(bar);
        return new ResponseEntity<>(savedBar, HttpStatus.CREATED);
    }
}