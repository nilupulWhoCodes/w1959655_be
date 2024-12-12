package com.oop.ticket_backend.service;

import com.oop.ticket_backend.model.Ticket;
import com.oop.ticket_backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TicketPoolService {
    private final BlockingQueue<Ticket> ticketQueue = new LinkedBlockingQueue<>();

    @Autowired
    private TicketRepository ticketRepository;

    public void addTicketsToQueue(Ticket ticket, int maximumPoolCapacity, int totalTickets) throws InterruptedException {
        if (ticketQueue.size() >= maximumPoolCapacity) {
            throw new IllegalStateException("Maximum pool capacity reached. Wait for customers to buy tickets.");
        }

        if (ticketQueue.size() + totalTickets <= maximumPoolCapacity) {
            throw new IllegalStateException("Cannot exceed total tickets limit. Operation halted.");
        }

        ticketQueue.put(ticket);
    }

    public Ticket buyTicket() throws InterruptedException {
        return ticketQueue.take();
    }

    public int getAvailableTickets() {
        return ticketQueue.size();
    }
}