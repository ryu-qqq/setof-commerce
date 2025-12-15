package com.setof.connectly.module.image.dto;

import com.setof.connectly.module.image.enums.ImagePath;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PreSignedUrlRequest {

    private String fileName;
    private ImagePath imagePath;
}
