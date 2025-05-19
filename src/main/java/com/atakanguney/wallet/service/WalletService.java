package com.atakanguney.wallet.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.atakanguney.wallet.dao.Customer;
import com.atakanguney.wallet.dao.Transaction;
import com.atakanguney.wallet.dao.Wallet;
import com.atakanguney.wallet.enums.Currency;
import com.atakanguney.wallet.enums.OppositePartyType;
import com.atakanguney.wallet.enums.TransactionStatus;
import com.atakanguney.wallet.enums.TransactionType;
import com.atakanguney.wallet.repository.CustomerRepository;
import com.atakanguney.wallet.repository.TransactionRepository;
import com.atakanguney.wallet.repository.WalletRepository;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepo;
    @Autowired private TransactionRepository txnRepo;
    @Autowired private CustomerRepository customerRepo;

    public Wallet createWallet(Long customerId, String name, Currency currency, boolean shopping, boolean withdraw) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();
        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setWalletName(name);
        wallet.setCurrency(currency);
        wallet.setActiveForShopping(shopping);
        wallet.setActiveForWithdraw(withdraw);
        return walletRepo.save(wallet);
    }

    public List<Wallet> listWallets(Long customerId) {
        return walletRepo.findByCustomerId(customerId);
    }

    public Transaction deposit(Long walletId, BigDecimal amount, OppositePartyType type, String source) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow();
        Transaction txn = new Transaction();
        txn.setWallet(wallet);
        txn.setAmount(amount);
        txn.setType(TransactionType.DEPOSIT);
        txn.setOppositePartyType(type);
        txn.setOppositeParty(source);

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            txn.setStatus(TransactionStatus.PENDING);
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            txn.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
        }
        walletRepo.save(wallet);
        return txnRepo.save(txn);
    }

    public Transaction withdraw(Long walletId, BigDecimal amount, OppositePartyType type, String destination) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow();
        if (!wallet.isActiveForWithdraw()) {
            throw new IllegalStateException("Wallet not active for withdraw");
        }
        Transaction txn = new Transaction();
        txn.setWallet(wallet);
        txn.setAmount(amount);
        txn.setType(TransactionType.WITHDRAW);
        txn.setOppositePartyType(type);
        txn.setOppositeParty(destination);

        if (amount.compareTo(wallet.getUsableBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient usable balance");
        }

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            txn.setStatus(TransactionStatus.PENDING);
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        } else {
            txn.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
        }
        walletRepo.save(wallet);
        return txnRepo.save(txn);
    }

    public List<Transaction> listTransactions(Long walletId) {
        return txnRepo.findByWalletId(walletId);
    }

    public Transaction approveTransaction(Long txnId, TransactionStatus status) {
        Transaction txn = txnRepo.findById(txnId).orElseThrow();
        Wallet wallet = txn.getWallet();

        if (status == TransactionStatus.APPROVED && txn.getStatus() == TransactionStatus.PENDING) {
            if (txn.getType() == TransactionType.DEPOSIT) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(txn.getAmount()));
            } else if (txn.getType() == TransactionType.WITHDRAW) {
                wallet.setBalance(wallet.getBalance().subtract(txn.getAmount()));
            }
        } else if (status == TransactionStatus.DENIED && txn.getStatus() == TransactionStatus.PENDING) {
            if (txn.getType() == TransactionType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance().subtract(txn.getAmount()));
            } else if (txn.getType() == TransactionType.WITHDRAW) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(txn.getAmount()));
            }
        }

        txn.setStatus(status);
        walletRepo.save(wallet);
        return txnRepo.save(txn);
    }
}
