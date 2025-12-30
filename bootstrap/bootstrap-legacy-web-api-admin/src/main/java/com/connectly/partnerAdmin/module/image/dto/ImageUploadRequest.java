package com.connectly.partnerAdmin.module.image.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUploadRequest {
    private String immediateUploadUrl;
    private String imageUrl;
    private String contentType;

}
