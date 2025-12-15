package com.setof.connectly.module.product.dto;

import com.setof.connectly.module.event.enums.EventProductType;
import com.setof.connectly.module.product.dto.cateogry.ProductCategoryDto;
import com.setof.connectly.module.product.dto.group.ProductDto;
import com.setof.connectly.module.product.dto.group.ProductGroupDto;
import com.setof.connectly.module.product.dto.image.ProductImageDto;
import com.setof.connectly.module.product.dto.notice.ProductNoticeDto;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ProductGroupResponse {
    private ProductGroupDto productGroup;
    private ProductNoticeDto productNotices;
    @Builder.Default private Set<ProductImageDto> productGroupImages = new HashSet<>();
    @Builder.Default private Set<ProductDto> products = new HashSet<>();
    @Builder.Default private Set<ProductCategoryDto> categories = new HashSet<>();
    private String detailDescription;

    private double mileageRate;
    private double expectedMileageAmount;
    private boolean isFavorite;
    private EventProductType eventProductType;

    public void setExpectedMileageAmount(double expectedMileageAmount) {
        this.expectedMileageAmount = expectedMileageAmount;
    }

    public void setMileageRate(double mileageRate) {
        this.mileageRate = mileageRate;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setEventProductType(EventProductType eventProductType) {
        this.eventProductType = eventProductType;
        if (eventProductType.isRaffle()) {
            setMileageRate(0);
            setExpectedMileageAmount(0);
        }
    }
}
