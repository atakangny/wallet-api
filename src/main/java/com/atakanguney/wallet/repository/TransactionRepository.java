package com.atakanguney.wallet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.atakanguney.wallet.dao.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);
}
