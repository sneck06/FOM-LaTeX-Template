package de.telekom.bonienrichmentprototype;

import de.telekom.bonienrichmentprototype.configuration.ApplicationInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "de.telekom")
@EnableScheduling
@Slf4j
public class MainApp {

    public static void main(String[] args) {

        // Load System Environment variables before Spring Boot run.
        ApplicationInit.init();

        SpringApplication.run(MainApp.class, args);
    }
}
