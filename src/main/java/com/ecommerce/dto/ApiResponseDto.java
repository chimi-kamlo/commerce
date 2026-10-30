package com.ecommerce.dto;

import java.util.Map;

public record ApiResponseDto<T>(
        boolean success,
        String message,
        T data,
        Map<String, String> errors
) {}