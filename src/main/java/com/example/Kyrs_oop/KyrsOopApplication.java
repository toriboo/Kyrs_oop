package com.example.Kyrs_oop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages={"com.example.Kyrs_oop.service","com.example.Kyrs_oop.config","com.example.Kyrs_oop.service.TelegramBot"})
public class KyrsOopApplication {

	public static void main(String[] args) {

		SpringApplication.run(KyrsOopApplication.class, args);

	}

}

