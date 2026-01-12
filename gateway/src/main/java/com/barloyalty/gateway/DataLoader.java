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
            // Verificăm dacă baza de date este goală folosind userRepository (e mai sigur)
            if (userRepository.count() == 0) {
                System.out.println(">>> [SEED] Baza de date este goala. Se adauga datele cerute in barem...");

                // 1. Creează utilizatorul pentru Microserviciul Python (Cerința Comunicare HTTP)
                // Acesta va fi folosit de scriptul Python pentru a se autentifica la Gateway
                User qrServiceUser = new User();
                qrServiceUser.setUsername("qr_service");
                qrServiceUser.setPassword(passwordEncoder.encode("qr_secret_pass"));
                qrServiceUser.setRole("SERVICE");
                userRepository.save(qrServiceUser);

                // 2. Creează Clienți (Barem: minim 2 clienți)
                User client1 = new User();
                client1.setUsername("client1");
                client1.setPassword(passwordEncoder.encode("password123"));
                client1.setRole("CLIENT");
                userRepository.save(client1);

                User client2 = new User();
                client2.setUsername("client2");
                client2.setPassword(passwordEncoder.encode("password123"));
                client2.setRole("CLIENT");
                userRepository.save(client2);

                // 3. Creează Baruri (Barem: minim 2 baruri)
                Bar bar1 = new Bar();
                bar1.setName("The Beer Garden");
                barRepository.save(bar1);

                Bar bar2 = new Bar();
                bar2.setName("Cocktail Haven");
                barRepository.save(bar2);

                // 4. Adaugă Recompense
                Reward r1 = new Reward();
                r1.setName("Bere Gratis");
                r1.setPointsRequired(50);
                r1.setBar(bar1);
                rewardRepository.save(r1);

                Reward r2 = new Reward();
                r2.setName("Voucher 20 RON");
                r2.setPointsRequired(100);
                r2.setBar(bar2);
                rewardRepository.save(r2);

                System.out.println(">>> [SEED] Datele initiale au fost adaugate cu succes!");
            } else {
                System.out.println(">>> [SEED] Baza de date contine deja date. Se sare peste populare.");
            }
        };
    }
}