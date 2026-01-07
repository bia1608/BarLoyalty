package com.barloyalty.gateway.repository;

import com.barloyalty.gateway.model.Transaction;
import com.barloyalty.gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByClient(User client);
    Optional<Transaction> findByQrCode(String qrCode);
}