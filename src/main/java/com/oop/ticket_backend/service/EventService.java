package com.oop.ticket_backend.service;

import com.oop.ticket_backend.exception.ResourceNotFoundException;
import com.oop.ticket_backend.model.Event;
import com.oop.ticket_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with ID " + id + " not found"));
    }

    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        messagingTemplate.convertAndSend("/topic/eventStatus", savedEvent);
        return savedEvent;

    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);
        event.setName(eventDetails.getName());
        event.setLocation(eventDetails.getLocation());
        event.setStartTime(eventDetails.getStartTime());
        event.setPhotoUrl(eventDetails.getPhotoUrl());
        event.setEventStarted(eventDetails.isEventStarted());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
        messagingTemplate.convertAndSend("/topic/eventDeleted", id);
    }

    public void changeEventStatusWithThread(Long id, Boolean status) {

        Thread thread = new Thread(() -> {
            try {

                Event event = getEventById(id);
                event.setEventStarted(status);
                Event updatedEvent = eventRepository.save(event);
                messagingTemplate.convertAndSend("/topic/eventStatus", updatedEvent);

                System.out.println("Event status updated successfully in thread: " + Thread.currentThread().getName());
            } catch (Exception e) {
                System.err.println("Error updating event status: " + e.getMessage());
            }
        });
        thread.start();
    }
}
