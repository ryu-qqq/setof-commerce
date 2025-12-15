package com.setof.connectly.module.display.dto.banner;

import com.setof.connectly.module.display.enums.BannerType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BannerFilter {

    @NotNull(message = "bannerType 은 필수입니다.")
    private BannerType bannerType;
}
