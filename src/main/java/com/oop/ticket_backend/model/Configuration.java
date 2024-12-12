package com.oop.ticket_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalNumberOfTickets;
    private int maximumPoolCapacity;
    private int ticketReleaseRate;
    private int customerRetrievalRate;

    public Long getId() {
        return id;
    }

    @OneToOne(mappedBy = "configuration")
    @JsonBackReference
    private Event event;

    public void setId(Long id) {
        this.id = id;
    }

    public int getMaximumPoolCapacity() {
        return maximumPoolCapacity;
    }

    public void setMaximumPoolCapacity(int maximumPoolCapacity) {
        this.maximumPoolCapacity = maximumPoolCapacity;
    }

    public int getTotalNumberOfTickets() {
        return totalNumberOfTickets;
    }

    public void setTotalNumberOfTickets(int totalNumberOfTickets) {
        this.totalNumberOfTickets = totalNumberOfTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
}
