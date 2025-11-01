package com.example.LeBonCoinp.Swipes;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeBonCoinp.Items.ItemResponse;
import com.example.LeBonCoinp.Users.User;

@RestController
public class SwipeController {

    private final SwipeService swipeService;

    public SwipeController(SwipeService swipeService) {
        this.swipeService = swipeService;
    }
// we have a swipe table , in this swipe table we can actually tell if a user swiped on 
// smtg or not by using swipedat
// 

@GetMapping("/swipe/next")
public Optional<ItemResponse> getNextItemToSwipe(@AuthenticationPrincipal User user) {
    return swipeService.getNextItemToSwipe(user.getId());
}

@PostMapping("/swipe")
public void swipe(@AuthenticationPrincipal User user, SwipeRequestDTO request) {
    swipeService.swipe(user.getId(), request);

}
}
