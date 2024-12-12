package com.oop.ticket_backend.repository;

import com.oop.ticket_backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEventName(String eventName);

    @Query("SELECT t FROM Ticket t WHERE t.isSold = false AND t.event.id = :eventId")
    List<Ticket> findAvailableTicketsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.event.id = :eventId")
    long countByEventId(@Param("eventId")Long eventId);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.event.id = :eventId AND t.isSold = true")
    long countSoldTicketsByEventId(@Param("eventId") Long eventId);

}
