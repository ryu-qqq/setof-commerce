package com.setof.connectly.module.user.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUserFavorite {

    @NotNull(message = "상품 아이디는 필수 입니다.")
    private long productGroupId;
}
