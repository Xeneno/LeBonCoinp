package com.example.LeBonCoinp.Matches;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByUserId(Long userId);
    List<Match> findByUserIdAndStatus(Long userId, String status);

    
}
