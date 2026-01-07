package com.barloyalty.gateway.repository;

import com.barloyalty.gateway.model.Bar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarRepository extends JpaRepository<Bar, Long> {
}