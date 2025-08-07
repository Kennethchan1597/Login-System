package com.ashimeru.personalfinance.demo_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoPersonalFinanceTrackerApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // loads .env
    System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
    System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		SpringApplication.run(DemoPersonalFinanceTrackerApplication.class, args);
	}

}
