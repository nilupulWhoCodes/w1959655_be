package com.oop.ticket_backend.service;

import com.oop.ticket_backend.dto.AddTicketsDto;
import com.oop.ticket_backend.exception.ResourceNotFoundException;
import com.oop.ticket_backend.model.Event;
import com.oop.ticket_backend.model.Ticket;
import com.oop.ticket_backend.repository.EventRepository;
import com.oop.ticket_backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketPoolService ticketPoolService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public void addTickets(AddTicketsDto addTicketsDto) throws InterruptedException {
        Event event = eventRepository.findById(addTicketsDto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.isEventStarted()) {
            throw new IllegalStateException("Cannot purchase tickets as the event has not started.");
        }

        int maximumPoolCapacity = event.getConfiguration().getMaximumPoolCapacity();
        int totalNumberOfTickets = event.getConfiguration().getTotalNumberOfTickets();

        long existingTicketCount = ticketRepository.countByEventId(event.getId());
        if (existingTicketCount >= totalNumberOfTickets) {
            throw new IllegalStateException("Event is sold out. No more tickets can be added.");
        }

        for (int i = 0; i < addTicketsDto.getCount(); i++) {
            if (existingTicketCount + i >= totalNumberOfTickets) {
                throw new IllegalStateException("Adding these tickets would exceed the total ticket limit for the event.");
            }

            Ticket ticket = new Ticket();
            ticket.setEvent(event);
            ticket.setPrice(addTicketsDto.getPrice());
            ticketPoolService.addTicketsToQueue(ticket, maximumPoolCapacity, totalNumberOfTickets);
            ticketRepository.save(ticket);

        }
        messagingTemplate.convertAndSend("/topic/eventStatus", event);

    }
}
