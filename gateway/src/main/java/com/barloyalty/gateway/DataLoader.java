package com.barloyalty.gateway;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.model.Reward;
import com.barloyalty.gateway.model.User;
import com.barloyalty.gateway.repository.BarRepository;
import com.barloyalty.gateway.repository.RewardRepository;
import com.barloyalty.gateway.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   BarRepository barRepository,
                                   RewardRepository rewardRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // VERIFICARE: Rulăm acest cod DOAR dacă nu există niciun bar în baza de date
            if (barRepository.count() == 0) {
                System.out.println("Baza de date este goala. Se adauga date initiale...");

                // 4. Creeaza un utilizator tehnic pentru serviciul QR
                User qrServiceUser = new User();
                qrServiceUser.setUsername("qr-service-user");
                qrServiceUser.setPassword(passwordEncoder.encode("secret-password"));
                qrServiceUser.setRole("QR_SERVICE"); // Ii dam un rol special
                userRepository.save(qrServiceUser);

                // 1. Creează utilizatori
                User client1 = new User();
                client1.setUsername("client1");
                client1.setPassword(passwordEncoder.encode("password"));
                client1.setRole("CLIENT");
                userRepository.save(client1);

                User client2 = new User();
                client2.setUsername("client2");
                client2.setPassword(passwordEncoder.encode("password"));
                client2.setRole("CLIENT");
                userRepository.save(client2);

                // 2. Creează baruri
                Bar bar1 = new Bar();
                bar1.setName("Barul Vesel");
                barRepository.save(bar1);

                Bar bar2 = new Bar();
                bar2.setName("Cafeneaua Liniștită");
                barRepository.save(bar2);

                // 3. Adaugă recompense
                Reward r1 = new Reward();
                r1.setName("Cafea Gratis");
                r1.setPointsRequired(50);
                r1.setBar(bar1);
                rewardRepository.save(r1);

                Reward r2 = new Reward();
                r2.setName("Bere la Jumătate de Preț");
                r2.setPointsRequired(100);
                r2.setBar(bar1);
                rewardRepository.save(r2);

                System.out.println("Date initiale adaugate cu succes!");
            } else {
                System.out.println("Baza de date contine deja date. Nu se adauga nimic nou.");
            }
        };
    }
}