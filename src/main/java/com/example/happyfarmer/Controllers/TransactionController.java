package com.example.happyfarmer.Controllers;


import com.example.happyfarmer.Models.Account;
import com.example.happyfarmer.Repositories.DepotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class TransactionController {

    private final DepotRepository depotRepository;

    @Autowired
    public TransactionController(DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
    }

    @PostMapping("/coinTransaction")
    public int verifyTransactionSuccessAndAddCoins(@RequestBody String boc, @RequestHeader("id") long id) {
        if (!boc.isEmpty()) {
            //msg_hash = Cell.one_from_boc(result["boc"]).hash.hex()
            //    print(f"Transaction info -> https://toncenter.com/api/v3/transactionsByMessage?direction=out&msg_hash={msg_hash}&limit=128&offset=0")
            //    print("Done")
            Account account = depotRepository.findDepotByUserId(id);
            account.setCoins(account.getCoins() + 1000);
            depotRepository.save(account);
            return 0;
        } else {
            return 1;
        }
    }
}
