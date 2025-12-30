package com.connectly.partnerAdmin.module.payload;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private T data;
    private Response response;

    @Builder
    public ApiResponse(T data, Response response) {
        this.data = data;
        this.response = response;
    }

    public static <T> ApiResponse<T> success(T apiResult) {
        return new ApiResponse<>(apiResult, Response.builder()
                .status(200)
                .message("success")
                .build());
    }

    public static <T> ApiResponse<T> dataNotFoundWithErrorMessage(String errorMessage) {
        return new ApiResponse<>(null, Response.builder()
                .status(404)
                .message(errorMessage)
                .build());
    }



}
