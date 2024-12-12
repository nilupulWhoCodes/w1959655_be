package com.oop.ticket_backend.repository;

import com.oop.ticket_backend.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}

