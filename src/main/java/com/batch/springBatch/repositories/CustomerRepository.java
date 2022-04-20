package com.batch.springBatch.repositories;

import com.batch.springBatch.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
