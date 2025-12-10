package com.ryuqq.setof.adapter.in.rest.v1.user.dto.response;

public record UserV1ApiResponse(boolean isJoined, UserDetailV1Response joinedUser) {
    public UserV1ApiResponse(boolean isJoined) {
        this(isJoined, null);
    }

    public record UserDetailV1Response(
            Long userId,
            String phoneNumber,
            String name,
            String email,
            String adConsent,
            Long totalMileage) {}
}
