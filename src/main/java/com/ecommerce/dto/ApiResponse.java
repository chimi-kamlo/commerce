package com.ecommerce.dto;

import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Map<String, String> errors
) {}