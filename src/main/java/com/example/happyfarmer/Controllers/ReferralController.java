package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.ReferralLink;
import com.example.happyfarmer.Services.ReferralService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class ReferralController {

    private ReferralService referralService;

    ReferralController(ReferralService referralService){
        this.referralService = referralService;
    }

    @GetMapping("/referralLink")
    public ReferralLink getReferralLink(@RequestHeader("id") long id){
        return referralService.getMyReferralLink(id);
    }
}
