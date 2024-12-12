package com.oop.ticket_backend.repository;

import com.oop.ticket_backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, Long> {
}
