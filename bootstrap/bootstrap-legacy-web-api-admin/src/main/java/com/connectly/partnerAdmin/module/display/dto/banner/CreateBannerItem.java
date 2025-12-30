package com.connectly.partnerAdmin.module.display.dto.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBannerItem {

    @NotNull(message = "bannerId는 필수입니다")
    private long bannerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long bannerItemId;

    @NotBlank(message = "title 필수입니다")
    @Length(max = 50, message = "title은 50자를 넘어갈 수 없습니다.")
    private String title;

    @Setter
    @NotBlank(message = "imageUrl 필수입니다")
    private String imageUrl;

    private String linkUrl;

    @NotNull(message = "displayPeriod는 필수입니다")
    private DisplayPeriod displayPeriod;

    @Max(value = 10, message = "전시순서는 10 보다 작아야 합니다.")
    @Min(value = 1, message = "전시순서는 최소 0보다 커야합니다.")
    private int displayOrder;

    @NotNull(message = "displayYn 필수입니다")
    private Yn displayYn;

    private Double width;

    private Double height;


    public Double getWidth() {
        if (width == null) {
            return 0.0;
        }
        return width;
    }

    public Double getHeight() {
        if (height == null) {
            return 0.0;
        }
        return height;
    }
}
