package com.ecommerce.utils;

import com.ecommerce.dto.ApiResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApiResponseRefactor  {
    public <T> ApiResponse<T> response(T data,String message,Map<String,String> error,boolean valide){

        return new ApiResponse<>(valide,message,data,error);

    }
}
