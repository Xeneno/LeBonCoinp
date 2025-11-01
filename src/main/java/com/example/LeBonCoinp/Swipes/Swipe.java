package com.example.LeBonCoinp.Swipes;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "swipes")
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Boolean liked;

    @Column(name = "swiped_at", nullable = false)
    private Timestamp swipedAt;

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public Long getUserId()           { return userId; }
    public void setUserId(Long userId){ this.userId = userId; }

    public Long getItemId()           { return itemId; }
    public void setItemId(Long itemId){ this.itemId = itemId; }

    public Boolean getLiked()         { return liked; }
    public void setLiked(Boolean liked){ this.liked = liked; }

    public Timestamp getSwipedAt()    { return swipedAt; }
    public void setSwipedAt(Timestamp swipedAt) { this.swipedAt = swipedAt; }
}