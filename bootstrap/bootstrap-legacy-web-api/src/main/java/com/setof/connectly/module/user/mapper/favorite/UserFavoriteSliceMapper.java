package com.setof.connectly.module.user.mapper.favorite;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserFavoriteSliceMapper {

    CustomSlice<UserFavoriteThumbnail> toSlice(
            List<UserFavoriteThumbnail> userFavoriteThumbnail, Pageable pageable, long totalCount);
}
