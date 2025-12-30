package com.connectly.partnerAdmin.module.coreServer;

import java.time.LocalDate;
import java.util.UUID;

import com.connectly.partnerAdmin.module.image.enums.ImagePath;
import com.connectly.partnerAdmin.module.image.enums.PathType;

public class ImageFileNameGenerator {

    public static String generate(Long id, String assetUrl, PathType pathType, String fileExtension) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://").append(assetUrl).append("/");
        sb.append(pathType.name()).append("/");
        sb.append(LocalDate.now()).append("/");

        if (id != null) {
            sb.append(id).append("/");
        }

        sb.append(UUID.randomUUID()).append(".").append(fileExtension);
        return sb.toString();
    }


    public static String generate(ImagePath pathType, String encodedFileName) {
        return pathType.name()
            + "/"
            + UUID.randomUUID()
            + "/"
            + encodedFileName;
    }

}
