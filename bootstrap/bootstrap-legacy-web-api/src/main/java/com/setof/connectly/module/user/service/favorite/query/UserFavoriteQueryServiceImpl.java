package com.setof.connectly.module.user.service.favorite.query;

import com.setof.connectly.module.user.dto.favorite.CreateUserFavorite;
import com.setof.connectly.module.user.dto.favorite.CreateUserFavorites;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteResponse;
import com.setof.connectly.module.user.entity.UserFavorite;
import com.setof.connectly.module.user.repository.favorite.UserFavoriteRepository;
import com.setof.connectly.module.user.service.favorite.fetch.UserFavoriteFindService;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFavoriteQueryServiceImpl implements UserFavoriteQueryService {

    private final UserFavoriteFindService userFavoriteFindService;
    private final UserFavoriteRepository userFavoriteRepository;

    @Override
    public UserFavoriteResponse createFavorite(CreateUserFavorite createUserFavorite) {
        long userId = SecurityUtils.currentUserId();
        UserFavoriteResponse userFavoriteResponse =
                userFavoriteFindService.fetchUserFavoriteEntity(
                        createUserFavorite.getProductGroupId());
        if (userFavoriteResponse.getUserFavoriteId() == 0) {
            UserFavorite save =
                    userFavoriteRepository.save(
                            new UserFavorite(userId, createUserFavorite.getProductGroupId()));
            userFavoriteResponse.setUserFavoriteId(save.getUserId());
        }
        return userFavoriteResponse;
    }

    @Transactional
    @Override
    public long deleteFavorite(CreateUserFavorites createUserFavorites) {
        List<UserFavorite> userFavorites =
                userFavoriteFindService.fetchUserFavoriteEntities(
                        createUserFavorites.getProductGroupIds());
        userFavoriteRepository.deleteAll(userFavorites);
        return userFavorites.size();
    }

    @Transactional
    @Override
    public long deleteFavorite(long productGroupId) {
        List<UserFavorite> userFavorites =
                userFavoriteFindService.fetchUserFavoriteEntities(List.of(productGroupId));
        userFavoriteRepository.deleteAll(userFavorites);
        return productGroupId;
    }
}
