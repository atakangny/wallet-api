package com.atakanguney.wallet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.atakanguney.wallet.dao.Customer;
import com.atakanguney.wallet.dao.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByCustomerId(Long customerId);
}
