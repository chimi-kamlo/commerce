package com.ecommerce.dto;

import java.util.Map;

public record ApiErrorDto(
        boolean success,
        String message,
        Long statut,
        Map<String, String> errors

) {
}
