package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.repository.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bars") // Toate cererile care încep cu /api/bars vor ajunge aici
public class BarController {

    @Autowired // Spring va injecta automat o instanță de BarRepository aici
    private BarRepository barRepository;

    // Metoda pentru a adăuga un bar nou (HTTP POST)
    @PostMapping
    public Bar createBar(@RequestBody Bar bar) {
        // Salvăm barul primit în baza de date și îl returnăm
        return barRepository.save(bar);
    }

    // Metoda pentru a vedea toate barurile (HTTP GET)
    @GetMapping
    public List<Bar> getAllBars() {
        // Căutăm toate barurile în baza de date și le returnăm ca listă
        return barRepository.findAll();
    }
}