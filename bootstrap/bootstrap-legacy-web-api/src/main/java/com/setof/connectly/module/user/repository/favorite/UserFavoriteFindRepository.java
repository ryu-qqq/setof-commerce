package com.setof.connectly.module.user.repository.favorite;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.user.dto.favorite.MyFavoriteFilter;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteResponse;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail;
import com.setof.connectly.module.user.entity.UserFavorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserFavoriteFindRepository {
    List<UserFavoriteThumbnail> fetchMyFavorites(
            long userId, MyFavoriteFilter filter, Pageable pageable);

    List<UserFavorite> fetchUserFavorites(long userId, List<Long> productGroupIds);

    Optional<UserFavoriteResponse> fetchUserFavorite(long userId, long productGroupId);

    List<Long> fetchUserFavoriteIds(long userId);

    JPAQuery<Long> fetchUserFavoriteCountQuery(long userId);

    boolean hasUserFavoriteProduct(long userId, long productGroupId);
}
