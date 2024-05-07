package com.example.happyfarmer.Models;

import lombok.Data;
import org.ton.java.smartcontract.types.WalletVersion;

@Data
public class WalletInfo {
    String mnemonic;
    WalletVersion walletVersion;

}
