package com.setof.connectly.module.user.service.favorite.fetch;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.exception.product.ProductGroupNotFoundException;
import com.setof.connectly.module.user.dto.favorite.MyFavoriteFilter;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteResponse;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail;
import com.setof.connectly.module.user.entity.UserFavorite;
import com.setof.connectly.module.user.mapper.favorite.UserFavoriteSliceMapper;
import com.setof.connectly.module.user.repository.favorite.UserFavoriteFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserFavoriteFindServiceImpl implements UserFavoriteFindService {
    private final UserFavoriteFindRepository userFavoriteFindRepository;
    private final UserFavoriteSliceMapper userFavoriteSliceMapper;

    @Override
    public List<UserFavorite> fetchUserFavoriteEntities(List<Long> productGroupIds) {
        long userId = SecurityUtils.currentUserId();
        return userFavoriteFindRepository.fetchUserFavorites(userId, productGroupIds);
    }

    @Override
    public UserFavoriteResponse fetchUserFavoriteEntity(long productGroupId) {
        long userId = SecurityUtils.currentUserId();
        return userFavoriteFindRepository
                .fetchUserFavorite(userId, productGroupId)
                .orElseThrow(() -> new ProductGroupNotFoundException(productGroupId));
    }

    @Override
    public boolean hasUserFavoriteProduct(long productGroupId) {
        long userId = SecurityUtils.currentUserId();
        return userFavoriteFindRepository.hasUserFavoriteProduct(userId, productGroupId);
    }

    @Override
    public CustomSlice<UserFavoriteThumbnail> fetchUserFavorites(
            MyFavoriteFilter filter, Pageable pageable) {
        long userId = SecurityUtils.currentUserId();
        List<UserFavoriteThumbnail> userFavoriteThumbnail =
                userFavoriteFindRepository.fetchMyFavorites(userId, filter, pageable);
        JPAQuery<Long> countQuery = userFavoriteFindRepository.fetchUserFavoriteCountQuery(userId);

        return userFavoriteSliceMapper.toSlice(
                userFavoriteThumbnail, pageable, countQuery.fetchCount());
    }

    @Override
    public List<Long> fetchUserFavoriteIds() {
        long userId = SecurityUtils.currentUserId();
        return userFavoriteFindRepository.fetchUserFavoriteIds(userId);
    }
}
