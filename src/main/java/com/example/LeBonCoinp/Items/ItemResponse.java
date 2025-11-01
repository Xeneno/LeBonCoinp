package com.example.LeBonCoinp.Items;
import java.math.BigDecimal;
import java.time.Instant;

public record ItemResponse(
    Long id,
    String title,
    String description,
    BigDecimal price,
    String imageUrl,
    boolean isAvailable,
    Instant createdAt
) {}