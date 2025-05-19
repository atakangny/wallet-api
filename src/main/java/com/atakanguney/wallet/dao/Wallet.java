package com.atakanguney.wallet.dao;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.atakanguney.wallet.enums.Currency;

@Entity
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;
    private String walletName;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private boolean activeForShopping;
    private boolean activeForWithdraw;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal usableBalance = BigDecimal.ZERO;
    @ManyToOne
    private Customer customer;
}
