package com.setof.connectly.module.image.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PreSignedUrlResponse {
    private String preSignedUrl;
    private String objectKey;
}
