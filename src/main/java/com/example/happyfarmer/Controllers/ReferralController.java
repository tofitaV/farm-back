package com.example.happyfarmer.Controllers;

import com.example.happyfarmer.Models.Friend;
import com.example.happyfarmer.Models.ReferralLink;
import com.example.happyfarmer.Models.Users;
import com.example.happyfarmer.Services.ReferralService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@Controller
@RestController
public class ReferralController {

    private ReferralService referralService;

    ReferralController(ReferralService referralService){
        this.referralService = referralService;
    }
    @GetMapping("/friends")
    public List<Friend> getFriends(@RequestHeader("id") long id){
        return referralService.getMyFriends(id);
    }
}
