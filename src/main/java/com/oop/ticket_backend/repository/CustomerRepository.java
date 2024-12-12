package com.oop.ticket_backend.repository;

import com.oop.ticket_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
