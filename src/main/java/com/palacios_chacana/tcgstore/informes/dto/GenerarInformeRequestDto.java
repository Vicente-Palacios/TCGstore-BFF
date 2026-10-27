package com.palacios_chacana.tcgstore.informes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GenerarInformeRequestDto(
        @NotBlank @Email String email
) {
}
