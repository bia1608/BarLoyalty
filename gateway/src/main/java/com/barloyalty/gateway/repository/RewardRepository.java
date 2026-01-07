package com.barloyalty.gateway.repository;

import com.barloyalty.gateway.model.Reward;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface RewardRepository extends ListCrudRepository<Reward, Long> {
    List<Reward> findByBarId(Long barId);
}