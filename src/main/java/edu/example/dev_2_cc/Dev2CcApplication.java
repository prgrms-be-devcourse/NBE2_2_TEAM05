package edu.example.dev_2_cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
public class Dev2CcApplication {

    public static void main(String[] args) {
        SpringApplication.run(Dev2CcApplication.class, args);
    }

}
