package br.edu.unisinos.rededepetri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedeDePetriApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedeDePetriApplication.class, args);
    }

}
