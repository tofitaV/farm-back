package com.example.happyfarmer.Controllers;


import blockchain.ton.MyTransaction;
import blockchain.ton.MyWallet;
import com.example.happyfarmer.Models.WalletInfo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.ton.java.tonlib.types.RawTransaction;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class WalletController {

    @SneakyThrows
    @RequestMapping(value = "/walletInfo", method = RequestMethod.POST)
    public @ResponseBody String walletInfo(@RequestBody WalletInfo mnemonic) {
        return new MyWallet().getWalletBalance(mnemonic);
    }

    @RequestMapping(value = "/lastTransactionShortInfo", method = RequestMethod.POST)
    public @ResponseBody List<String> lastTransactionShortInfo(@RequestBody WalletInfo mnemonic) {
        return new MyTransaction().getLastTenTransactionsShortInfo(mnemonic);
    }

    @RequestMapping(value = "/lastTransaction", method = RequestMethod.POST)
    public @ResponseBody List<RawTransaction> lastTransaction(@RequestBody WalletInfo mnemonic) {
        return new MyTransaction().getLastTenTransaction(mnemonic);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public @ResponseBody void createTransaction(@RequestBody WalletInfo mnemonic) {
        new MyTransaction().createTransaction(mnemonic);
    }
}
