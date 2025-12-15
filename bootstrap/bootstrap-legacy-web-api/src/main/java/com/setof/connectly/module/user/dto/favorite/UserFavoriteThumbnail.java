package com.setof.connectly.module.user.dto.favorite;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import com.setof.connectly.module.product.dto.brand.BrandDto;
import com.setof.connectly.module.product.entity.group.embedded.Price;
import com.setof.connectly.module.product.entity.group.embedded.ProductStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteThumbnail extends ProductGroupThumbnail {

    protected long userFavoriteId;

    @QueryProjection
    public UserFavoriteThumbnail(
            long productGroupId,
            long sellerId,
            String productGroupName,
            BrandDto brand,
            String productImageUrl,
            Price price,
            LocalDateTime insertDate,
            ProductStatus productStatus,
            long userFavoriteId) {
        super(
                productGroupId,
                sellerId,
                productGroupName,
                brand,
                productImageUrl,
                price,
                insertDate,
                productStatus);
        this.userFavoriteId = userFavoriteId;
    }

    public double getAverageRating() {
        return super.getAverageRating();
    }

    public long getReviewCount() {
        return super.getReviewCount();
    }

    public double getScore() {
        return super.getScore();
    }
}
