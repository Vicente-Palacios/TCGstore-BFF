package com.palacios_chacana.tcgstore.cards.dto;

import jakarta.validation.groups.Default;

/**
 * Grupo de validación para crear (POST) y reemplazar (PUT) una carta: exige los campos obligatorios.
 * En PATCH (actualización parcial) solo aplica el grupo por defecto: los campos pueden venir nulos,
 * pero si vienen deben ser válidos.
 */
public interface OnWrite extends Default {
}
