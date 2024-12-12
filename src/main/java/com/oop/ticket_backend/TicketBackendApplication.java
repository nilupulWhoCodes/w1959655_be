package com.oop.ticket_backend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAsync
public class TicketBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TicketBackendApplication.class, args);
	}
}
