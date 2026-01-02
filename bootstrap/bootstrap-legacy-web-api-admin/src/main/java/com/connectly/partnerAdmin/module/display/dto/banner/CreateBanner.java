package com.connectly.partnerAdmin.module.display.dto.banner;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.BannerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBanner {

    @NotBlank(message = "title 필수입니다")
    @Length(max = 50, message = "title은 50자를 넘어갈 수 없습니다.")
    private String title;

    @NotNull(message = "bannerType는 필수입니다")
    private BannerType bannerType;

    @NotNull(message = "displayPeriod는 필수입니다")
    private DisplayPeriod displayPeriod;

    @NotNull(message = "displayYn는 필수입니다")
    private Yn displayYn;

}
