package com.ecommerce.dto;

import java.util.Set;

public record PasserCommandeRequestDto(Set<LignecommandeRequestDto> lignes) {
}
