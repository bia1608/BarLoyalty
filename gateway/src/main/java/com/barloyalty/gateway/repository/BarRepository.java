package com.barloyalty.gateway.repository;

import com.barloyalty.gateway.model.Bar;
import org.springframework.data.repository.ListCrudRepository;

public interface BarRepository extends ListCrudRepository<Bar, Long> {
}