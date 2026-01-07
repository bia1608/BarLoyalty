package com.barloyalty.gateway;

import com.barloyalty.gateway.model.Bar;
import com.barloyalty.gateway.model.User;
import com.barloyalty.gateway.model.Reward; // Adaugă acest import
import com.barloyalty.gateway.repository.BarRepository;
import com.barloyalty.gateway.repository.UserRepository;
import com.barloyalty.gateway.repository.RewardRepository; // Adaugă acest import
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    private final UserRepository userRepository;
    private final BarRepository barRepository;
    private final RewardRepository rewardRepository; // Adaugă acest camp
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, BarRepository barRepository, RewardRepository rewardRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.barRepository = barRepository;
        this.rewardRepository = rewardRepository; // Initializeaza-l
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            // 1. Creează utilizatori (cu parole criptate)
            User admin = new User();
            admin.setUsername("bar_admin1");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole("BAR_OWNER");
            userRepository.save(admin);

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

            // 3. Adaugă recompense pentru barul 1
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

            // Adaugă recompense pentru barul 2
            Reward r3 = new Reward();
            r3.setName("Ceai cald");
            r3.setPointsRequired(30);
            r3.setBar(bar2);
            rewardRepository.save(r3);

            System.out.println("Date inițiale adăugate!");
        };
    }
}