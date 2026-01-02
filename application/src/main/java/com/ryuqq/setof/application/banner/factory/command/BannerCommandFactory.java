package com.ryuqq.setof.application.banner.factory.command;

import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerType;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * Banner Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerCommandFactory {

    private final ClockHolder clockHolder;

    public BannerCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateBannerCommand → Banner 도메인 변환
     *
     * @param command 생성 커맨드
     * @return Banner 도메인 객체
     */
    public Banner createBanner(CreateBannerCommand command) {
        ContentTitle title = ContentTitle.of(command.title());
        BannerType bannerType = BannerType.valueOf(command.bannerType());
        DisplayPeriod displayPeriod =
                DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());

        return Banner.forNew(title, bannerType, displayPeriod, clockHolder.getClock());
    }

    /**
     * UpdateBannerCommand로 기존 Banner 업데이트
     *
     * @param existing 기존 배너
     * @param command 수정 커맨드
     */
    public void applyUpdateBanner(Banner existing, UpdateBannerCommand command) {
        ContentTitle title = ContentTitle.of(command.title());
        DisplayPeriod displayPeriod =
                DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());

        existing.update(title, displayPeriod);
    }
}
