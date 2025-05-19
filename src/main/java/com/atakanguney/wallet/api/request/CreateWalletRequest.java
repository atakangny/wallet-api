package com.atakanguney.wallet.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.atakanguney.wallet.enums.Currency;

@Getter
@Setter
@NoArgsConstructor
public class CreateWalletRequest {
    Long customerId;
    String walletName;
    Currency currency;
    boolean activeForShopping;
    boolean activeForWithdraw;
}
