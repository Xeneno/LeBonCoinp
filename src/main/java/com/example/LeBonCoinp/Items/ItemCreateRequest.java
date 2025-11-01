package com.example.LeBonCoinp.Items;
import java.math.BigDecimal;

public record ItemCreateRequest(
     String title,
     String description,
     BigDecimal price,
     String imageUrl
) {}