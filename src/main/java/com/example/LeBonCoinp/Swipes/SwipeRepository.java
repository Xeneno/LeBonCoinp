package com.example.LeBonCoinp.Swipes;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    Optional<Swipe> findByUserIdAndItemId(Long userId, Long itemId);
    
    
}
