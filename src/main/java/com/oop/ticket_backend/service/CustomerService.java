package com.oop.ticket_backend.service;

import com.oop.ticket_backend.dto.BuyTicketsDto;
import com.oop.ticket_backend.exception.ResourceNotFoundException;
import com.oop.ticket_backend.model.Event;
import com.oop.ticket_backend.model.Ticket;
import com.oop.ticket_backend.repository.CustomerRepository;
import com.oop.ticket_backend.repository.EventRepository;
import com.oop.ticket_backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketPoolService ticketPoolService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Ticket> buyTicket(BuyTicketsDto buyTicketsDto) throws InterruptedException {
        Event event = eventRepository.findById(buyTicketsDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.isEventStarted()) {
            throw new IllegalStateException("Cannot purchase tickets as the event has not started.");
        }

        int maximumPoolCapacity = event.getConfiguration().getMaximumPoolCapacity();
        int totalNumberOfTickets = event.getConfiguration().getTotalNumberOfTickets();

        long soldTicketsCount = ticketRepository.countSoldTicketsByEventId(event.getId());
        int availableTicketsInPool = ticketPoolService.getAvailableTickets();

        if (soldTicketsCount + availableTicketsInPool > maximumPoolCapacity) {
            throw new IllegalStateException("Waiting for vendors to add tickets.");
        }

        if (soldTicketsCount + availableTicketsInPool >= totalNumberOfTickets) {
            throw new IllegalStateException("Event is sold out. No more tickets available.");
        }

        List<Ticket> purchasedTickets = new ArrayList<>();

        for (int i = 0; i < buyTicketsDto.getCount(); i++) {
            Ticket ticket = ticketPoolService.buyTicket();
            ticket.setSold(true);
            ticketRepository.save(ticket);
            purchasedTickets.add(ticket);
        }

        messagingTemplate.convertAndSend("/topic/eventStatus", event);

        return purchasedTickets;
    }


}
