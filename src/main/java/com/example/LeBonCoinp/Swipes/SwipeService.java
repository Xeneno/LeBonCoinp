package com.example.LeBonCoinp.Swipes;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.LeBonCoinp.Items.Item;
import com.example.LeBonCoinp.Items.ItemMapper;
import com.example.LeBonCoinp.Items.ItemRepository;
import com.example.LeBonCoinp.Items.ItemResponse;
import com.example.LeBonCoinp.Items.ItemService;
import com.example.LeBonCoinp.Matches.Match;
import com.example.LeBonCoinp.Matches.MatchRepository;
import com.example.LeBonCoinp.Users.User;
import com.example.LeBonCoinp.Users.UserRepository;




import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SwipeService {

    private final SwipeRepository swipeRepository;
    private final ItemRepository itemRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;
   

    @Transactional // to make sure that all DB operations are atomic (either all succeed or all fail)
    public Match swipe(Long userId, SwipeRequestDTO request) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getIsAvailable()) {
            throw new RuntimeException("Item no longer available");
        }

       
        if (swipeRepository.findByUserIdAndItemId(userId, request.itemId()).isPresent()) {
            throw new RuntimeException("Already swiped");
        }

        Swipe swipe = new Swipe();
        swipe.setUserId(userId);
        swipe.setItemId(request.itemId());
        swipe.setLiked(request.liked());
        swipe.setSwipedAt(new Timestamp(System.currentTimeMillis()));
        swipeRepository.save(swipe);

        if (request.liked()) {
            Match match = new Match();
            match.setUserId(userId);
            match.setItemId(request.itemId());
            match.setMatchedAt(new Timestamp(System.currentTimeMillis()));
            match.setStatus("PENDING"); 
            return matchRepository.save(match);
        }

        return null; 
    }

    @Transactional(readOnly = true)
    public Optional<ItemResponse> getNextItemToSwipe(Long userId){

       return itemRepository.findUnseenByUserOrderByFreshness(userId, PageRequest.of(0,1))
                         .stream()
                         .findFirst()
                         .map(mapper::toDto);

}

}



