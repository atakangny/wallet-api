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
import com.atakanguney.wallet.enums.OppositePartyType;
import com.atakanguney.wallet.enums.TransactionStatus;
import com.atakanguney.wallet.enums.TransactionType;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Wallet wallet;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType;
    private String oppositeParty;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
