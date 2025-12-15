package com.setof.connectly.module.user.dto.favorite;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUserFavorites {

    @Size(min = 1, message = "삭제 할 좋아요 하나라도 있어야 합니다.")
    private List<Long> productGroupIds;
}
