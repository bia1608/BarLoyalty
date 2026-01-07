package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.repository.BarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bars")
public class BarController {

    @Autowired
    private BarRepository barRepository;

    @PostMapping
    public Bar createBar(@RequestBody Bar bar) {
        return barRepository.save(bar);
    }

    @GetMapping
    public List<Bar> getAllBars() {
        return barRepository.findAll();
    }
}