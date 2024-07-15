package com.example.happyfarmer.Controllers;


import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Repositories.DepotRepository;
import com.example.happyfarmer.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/coinTransaction")
    public int verifyTransactionSuccessAndAddCoins(@RequestBody String boc, @RequestHeader("id") long id) {
        if (!boc.isEmpty()) {
            transactionService.updateCoins(id, 1000);
            return 0;
        } else {
            return 1;
        }
    }
}
