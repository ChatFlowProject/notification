package com.example.notification.dto;

import com.example.notification.common.ApiStatus;

import java.util.Collection;

public record ApiResponse<T>(
        ApiStatus status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ApiStatus.ERROR, message, null);
    }
}

