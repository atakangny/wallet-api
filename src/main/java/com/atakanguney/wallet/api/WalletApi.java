package com.atakanguney.wallet.api;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.atakanguney.wallet.api.request.CreateWalletRequest;
import com.atakanguney.wallet.dao.Transaction;
import com.atakanguney.wallet.dao.Wallet;
import com.atakanguney.wallet.enums.Currency;
import com.atakanguney.wallet.enums.OppositePartyType;
import com.atakanguney.wallet.enums.TransactionStatus;
import com.atakanguney.wallet.service.WalletService;

@RestController
@RequestMapping("/api/wallets")
public class WalletApi {
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public Wallet create(@RequestBody CreateWalletRequest request) {
        try{

            return walletService.createWallet(request.getCustomerId(), request.getWalletName(), request.getCurrency(), request.isActiveForShopping(), request.isActiveForWithdraw());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/list/{customerId}")
    public List<Wallet> list(@PathVariable Long customerId) {
        return walletService.listWallets(customerId);
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestParam Long walletId, @RequestParam BigDecimal amount, @RequestParam OppositePartyType type, @RequestParam String source) {
        return walletService.deposit(walletId, amount, type, source);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestParam Long walletId, @RequestParam BigDecimal amount, @RequestParam OppositePartyType type, @RequestParam String destination) {
        return walletService.withdraw(walletId, amount, type, destination);
    }

    @GetMapping("/transactions/{walletId}")
    public List<Transaction> transactions(@PathVariable Long walletId) {
        return walletService.listTransactions(walletId);
    }

    @PostMapping("/approve")
    public Transaction approve(@RequestParam Long txnId, @RequestParam TransactionStatus status) {
        return walletService.approveTransaction(txnId, status);
    }
}

