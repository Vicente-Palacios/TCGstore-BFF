package com.palacios_chacana.tcgstore.cards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CardRequestDto(
        @NotBlank(groups = OnWrite.class) @Size(max = 255) String name,
        @NotBlank(groups = OnWrite.class) @Size(max = 255) String game,
        @Size(max = 255) String setName,
        @Size(max = 255) String rarity,
        @Size(max = 255) String condition,
        @NotNull(groups = OnWrite.class) @PositiveOrZero Double price,
        @NotNull(groups = OnWrite.class) @PositiveOrZero Integer stock,
        @Size(max = 1000) String imageUrl
) {
}
