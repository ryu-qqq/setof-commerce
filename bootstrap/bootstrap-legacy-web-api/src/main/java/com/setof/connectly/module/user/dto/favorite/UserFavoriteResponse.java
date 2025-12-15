package com.setof.connectly.module.user.dto.favorite;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteResponse {

    private long userFavoriteId;
    private ProductGroupThumbnail productGroupThumbnail;

    @QueryProjection
    public UserFavoriteResponse(long userFavoriteId, ProductGroupThumbnail productGroupThumbnail) {
        this.userFavoriteId = userFavoriteId;
        this.productGroupThumbnail = productGroupThumbnail;
    }

    public void setUserFavoriteId(long userFavoriteId) {
        this.userFavoriteId = userFavoriteId;
    }
}
