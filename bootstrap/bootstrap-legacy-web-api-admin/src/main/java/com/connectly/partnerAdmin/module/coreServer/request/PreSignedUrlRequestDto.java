package com.connectly.partnerAdmin.module.coreServer.request;

import com.connectly.partnerAdmin.module.image.enums.ImagePath;

public record PreSignedUrlRequestDto(
	String fileName,
	ImagePath imagePath
) {
}
