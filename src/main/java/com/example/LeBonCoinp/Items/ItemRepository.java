package com.example.LeBonCoinp.Items;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByIsAvailableTrue();
    Page<Item> findBySellerId(Long sellerId, Pageable pageable);

 @Query("""
  select i from Item i
  where i.isAvailable = true
    and not exists (
      select 1 from Swipe s
      where s.userId = :userId and s.itemId = i.id
    )
  order by i.createdAt desc, i.id desc
""")
List<Item> findUnseenByUserOrderByFreshness(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);


} 
