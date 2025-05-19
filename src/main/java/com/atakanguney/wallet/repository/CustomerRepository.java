package com.atakanguney.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.atakanguney.wallet.dao.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
