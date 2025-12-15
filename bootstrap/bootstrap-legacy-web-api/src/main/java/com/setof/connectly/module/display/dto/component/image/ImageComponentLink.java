package com.setof.connectly.module.display.dto.component.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import lombok.*;
import org.springframework.util.StringUtils;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageComponentLink {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imageComponentItemId;

    private int displayOrder;
    private String imageUrl;
    private String linkUrl;

    @JsonIgnore
    private static final String IMAGE_URL_PREFIX =
            "https://connectly-luxury-image-temp.s3.ap-northeast-2.amazonaws.com/";

    public String getLinkUrl() {
        return StringUtils.hasText(linkUrl) ? linkUrl : "";
    }

    public String getImageUrl() {
        return StringUtils.hasText(imageUrl) ? imageUrl : "";
    }

    public void setImageComponentItemId(Long imageComponentItemId) {
        this.imageComponentItemId = imageComponentItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageComponentLink that = (ImageComponentLink) o;

        return displayOrder == that.displayOrder
                && (!Objects.equals(linkUrl, that.linkUrl))
                && isImageChanged(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayOrder, linkUrl, imageUrl);
    }

    public boolean isImageChanged(String currentImageUrl, String newImageUrl) {
        return newImageUrl != null && newImageUrl.contains(IMAGE_URL_PREFIX);
    }

    public static ImageComponentLink createEmpty() {
        return new ImageComponentLink();
    }
}
