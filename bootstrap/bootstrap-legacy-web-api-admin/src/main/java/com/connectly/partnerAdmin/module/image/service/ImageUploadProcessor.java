package com.connectly.partnerAdmin.module.image.service;

import com.connectly.partnerAdmin.module.image.enums.PathType;

public interface ImageUploadProcessor {

    String uploadImage(Long id, String imageUrl, PathType pathType);

}
