package com.barloyalty.gateway.repository;
import com.barloyalty.gateway.model.Bar;
import org.springframework.data.jpa.repository.JpaRepository;
import com.barloyalty.gateway.model.Reward;
import java.util.List;
public interface RewardRepository extends JpaRepository<Reward,Long>{
    List<Reward> findByBar(Bar bar);
}
