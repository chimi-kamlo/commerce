package com.ecommerce.utils;

import com.ecommerce.dto.ApiResponseDto;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiResponseRefactor  {
    public <T> ApiResponseDto<T> response(T data, String message, Map<String,String> error, boolean valide){

        return new ApiResponseDto<>(valide,message,data,error);

    }
}
