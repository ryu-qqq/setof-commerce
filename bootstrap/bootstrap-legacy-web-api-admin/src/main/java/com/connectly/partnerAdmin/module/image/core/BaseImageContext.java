package com.connectly.partnerAdmin.module.image.core;

import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseImageContext implements ImageContext {

    private ImagePath imagePath;
    private String imageUrl;
    
}
