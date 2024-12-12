package com.oop.ticket_backend.controller;

import com.oop.ticket_backend.dto.AddTicketsDto;
import com.oop.ticket_backend.dto.BuyTicketsDto;
import com.oop.ticket_backend.exception.ResourceNotFoundException;
import com.oop.ticket_backend.model.Ticket;
import com.oop.ticket_backend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/buy-ticket")
    public ResponseEntity<List<Ticket>> buyTicket(@RequestBody BuyTicketsDto buyTicketsDto) {
        try {
            List<Ticket> tickets = customerService.buyTicket(buyTicketsDto);
            return ResponseEntity.ok(tickets);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
