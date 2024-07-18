package com.example.happyfarmer.Services;

import com.example.happyfarmer.Models.Friend;
import com.example.happyfarmer.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferralService {

    private UserRepository userRepository;

    public ReferralService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Friend> getMyFriends(long id) {
        List<Friend> friendList = userRepository.findAllFriendsByReferredBy(id);
        return friendList;
    }
}
