package com.connectly.partnerAdmin.module.display.dto.component.image;

import com.connectly.partnerAdmin.module.display.entity.embedded.ImageSize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageComponentLink {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imageComponentItemId;
    private int displayOrder;
    @Setter
    private String imageUrl;
    private String linkUrl;
    @Setter
    private Long imageComponentId;
    private ImageSize imageSize;


    @JsonIgnore
    private static final String IMAGE_URL_PREFIX = "https://connectly-luxury-image-temp.s3.ap-northeast-2.amazonaws.com/";

    public String getLinkUrl() {
        return StringUtils.hasText(linkUrl) ? linkUrl : "";
    }

    public String getImageUrl() {
        return StringUtils.hasText(imageUrl) ? imageUrl : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageComponentLink that = (ImageComponentLink) o;

        return displayOrder == that.displayOrder &&
                (!Objects.equals(linkUrl, that.linkUrl)) &&
                isImageChanged(imageUrl, that.imageUrl);

    }

    @Override
    public int hashCode() {
        return Objects.hash(displayOrder, linkUrl, imageUrl);
    }

    public boolean isImageChanged(String currentImageUrl, String newImageUrl) {
        return currentImageUrl != null && newImageUrl.contains(IMAGE_URL_PREFIX);
    }

    public ImageSize getImageSize() {
        if(this.imageSize == null) {
            return new ImageSize(0 ,0);
        }
        return imageSize;
    }
}
