package com.example.LeBonCoinp.Items;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.LeBonCoinp.Users.User;

@RestController
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


// @PostMapping
//     public ItemResponse createItem(ItemCreateRequest req, @AuthenticationPrincipal User user) {

//      return itemService.createItem(req, user.getId());
//     }

   
@PostMapping("/items")
public ItemResponse createItem(@RequestBody ItemCreateRequest req) {
    return itemService.createItem(req, 1L); // temporary hardcoded user ID 
}


}