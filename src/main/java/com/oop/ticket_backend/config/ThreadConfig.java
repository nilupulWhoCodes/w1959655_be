package com.oop.ticket_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {
    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(10);
    }
}
