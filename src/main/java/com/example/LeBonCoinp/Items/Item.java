package com.example.LeBonCoinp.Items;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getTitle()          { return title; }
    public void setTitle(String title){ this.title = title; }

    public String getDescription()    { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice()          { return price; }
    public void setPrice(BigDecimal price){ this.price = price; }

    public String getImageUrl()       { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Timestamp getCreatedAt()   { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Boolean getIsAvailable()   { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public Long getSellerId()         { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId;

}
}