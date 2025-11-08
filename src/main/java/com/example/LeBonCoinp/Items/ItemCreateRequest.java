package com.example.LeBonCoinp.Items;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemCreateRequest(
     @NotBlank String title,
     @Size (max=2000) String description,
     @NotNull @DecimalMin("0.0")  BigDecimal price,
     @URL  String imageUrl
) {}