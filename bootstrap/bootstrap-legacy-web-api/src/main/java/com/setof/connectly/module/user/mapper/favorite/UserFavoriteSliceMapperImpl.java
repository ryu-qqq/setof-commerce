package com.setof.connectly.module.user.mapper.favorite;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import com.setof.connectly.module.user.dto.favorite.UserFavoriteThumbnail;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class UserFavoriteSliceMapperImpl extends AbstractGeneralSliceMapper<UserFavoriteThumbnail>
        implements UserFavoriteSliceMapper {

    @Override
    public CustomSlice<UserFavoriteThumbnail> toSlice(
            List<UserFavoriteThumbnail> userFavoriteThumbnail, Pageable pageable, long totalCount) {
        return super.toSlice(userFavoriteThumbnail, pageable, totalCount);
    }
}
