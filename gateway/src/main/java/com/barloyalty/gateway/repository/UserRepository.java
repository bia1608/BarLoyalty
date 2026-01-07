package com.barloyalty.gateway.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.barloyalty.gateway.model.User;
public interface UserRepository extends JpaRepository<User,Long>{
}
