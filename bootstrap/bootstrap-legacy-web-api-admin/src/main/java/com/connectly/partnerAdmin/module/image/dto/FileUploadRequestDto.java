package com.connectly.partnerAdmin.module.image.dto;

public record FileUploadRequestDto(
    String imageUrl,
    String originUrl,
    String contentType
) {}

