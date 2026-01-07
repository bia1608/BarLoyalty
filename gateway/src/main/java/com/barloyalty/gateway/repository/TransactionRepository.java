package com.barloyalty.gateway.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.barloyalty.gateway.model.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction,Long>{
}
