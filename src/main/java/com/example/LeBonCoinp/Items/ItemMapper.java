package com.example.LeBonCoinp.Items;


import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemResponse toDto(Item item) {
        return new ItemResponse(
            item.getId(),
            item.getTitle(),
            item.getDescription(),
            item.getPrice(),
            item.getImageUrl(),
            Boolean.TRUE.equals(item.getIsAvailable()),
            item.getCreatedAt().toInstant()
        );
    }

    // Optional: DTO -> entity
    public Item toEntity(ItemCreateRequest req, Long sellerId) {
        Item item = new Item();
        item.setTitle(req.title());
        item.setDescription(req.description());
        item.setPrice(req.price());
        item.setImageUrl(req.imageUrl());
        item.setIsAvailable(true);
        item.setCreatedAt(java.sql.Timestamp.from(java.time.Instant.now()));
        item.setSellerId(sellerId);
        return item;
    }
}
