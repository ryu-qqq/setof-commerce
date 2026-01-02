package com.connectly.partnerAdmin.module.display.dto.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateBannerDisplayYn {
    private Yn displayYn;
}
