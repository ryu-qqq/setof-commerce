package com.setof.connectly.module.user.service.favorite.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.user.dto.favorite.MyFavoriteFilter;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteResponse;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail;
import com.setof.connectly.module.user.entity.UserFavorite;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserFavoriteFindService {
    List<UserFavorite> fetchUserFavoriteEntities(List<Long> productGroupId);

    UserFavoriteResponse fetchUserFavoriteEntity(long productGroupId);

    boolean hasUserFavoriteProduct(long productGroupId);

    CustomSlice<UserFavoriteThumbnail> fetchUserFavorites(
            MyFavoriteFilter filter, Pageable pageable);

    List<Long> fetchUserFavoriteIds();
}
