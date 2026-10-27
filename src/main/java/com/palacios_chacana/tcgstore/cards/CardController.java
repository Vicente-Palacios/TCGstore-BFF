package com.palacios_chacana.tcgstore.cards;

import com.palacios_chacana.tcgstore.cards.dto.CardRequestDto;
import com.palacios_chacana.tcgstore.cards.dto.OnWrite;
import com.palacios_chacana.tcgstore.cards.dto.CardResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<CardResponseDto> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getById(@PathVariable String id) {
        return cardService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('Admin', 'Colaborador')")
    @PostMapping
    public ResponseEntity<CardResponseDto> create(@Validated(OnWrite.class) @RequestBody CardRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.create(request));
    }

    @PreAuthorize("hasAnyRole('Admin', 'Colaborador')")
    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> update(@PathVariable String id, @Validated(OnWrite.class) @RequestBody CardRequestDto request) {
        return cardService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('Admin', 'Colaborador')")
    @PatchMapping("/{id}")
    public ResponseEntity<CardResponseDto> partialUpdate(@PathVariable String id, @Valid @RequestBody CardRequestDto request) {
        return cardService.partialUpdate(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return cardService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

