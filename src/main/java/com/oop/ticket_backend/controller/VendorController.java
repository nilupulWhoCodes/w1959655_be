package com.oop.ticket_backend.controller;

import com.oop.ticket_backend.dto.AddTicketsDto;
import com.oop.ticket_backend.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/add-tickets")
    public ResponseEntity<String> addTickets(@RequestBody AddTicketsDto addTicketsDto) {
        try {
            vendorService.addTickets(addTicketsDto);
            return ResponseEntity.ok("Tickets added successfully to the queue!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation interrupted. Try again.");
        }
    }

}