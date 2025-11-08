package com.example.LeBonCoinp.Items;

import java.security.Principal;
import java.util.List;
import com.example.LeBonCoinp.Users.User;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@RestController
public class ItemController {

    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


@PostMapping
    public ItemResponse createItem(ItemCreateRequest req, @AuthenticationPrincipal User user) {

     return itemService.createItem(req, user.getId());
    }

@GetMapping("/mine")
    public Page<ItemResponse> getMyItems(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 20, sort = {"createdAt","id"}, direction = Sort.Direction.DESC)
            Pageable pageable) {
        return itemService.getMyItems(user.getId(), pageable);
    }

   



}