package com.setof.connectly.module.user.service.favorite.query;

import com.setof.connectly.module.user.dto.favorite.CreateUserFavorite;
import com.setof.connectly.module.user.dto.favorite.CreateUserFavorites;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteResponse;

public interface UserFavoriteQueryService {

    UserFavoriteResponse createFavorite(CreateUserFavorite createUserFavorite);

    long deleteFavorite(CreateUserFavorites createUserFavorites);

    long deleteFavorite(long productGroupId);
}
