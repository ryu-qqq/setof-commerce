package com.ryuqq.setof.application.gnb.factory.command;

import com.ryuqq.setof.application.gnb.dto.command.CreateGnbCommand;
import com.ryuqq.setof.application.gnb.dto.command.UpdateGnbCommand;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * Gnb Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class GnbCommandFactory {

    private final ClockHolder clockHolder;

    public GnbCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateGnbCommand → Gnb 도메인 변환
     *
     * @param command 생성 커맨드
     * @return Gnb 도메인 객체
     */
    public Gnb createGnb(CreateGnbCommand command) {
        ContentTitle title = ContentTitle.of(command.title());
        ImageUrl linkUrl = command.linkUrl() != null ? ImageUrl.of(command.linkUrl()) : null;
        DisplayOrder displayOrder = DisplayOrder.of(command.displayOrder());
        DisplayPeriod displayPeriod =
                command.hasDisplayPeriod()
                        ? DisplayPeriod.of(command.displayStartDate(), command.displayEndDate())
                        : null;

        return Gnb.forNew(title, linkUrl, displayOrder, displayPeriod, clockHolder.getClock());
    }

    /**
     * UpdateGnbCommand로 기존 Gnb 업데이트
     *
     * @param existing 기존 GNB
     * @param command 수정 커맨드
     */
    public void applyUpdateGnb(Gnb existing, UpdateGnbCommand command) {
        ContentTitle title = ContentTitle.of(command.title());
        ImageUrl linkUrl = command.linkUrl() != null ? ImageUrl.of(command.linkUrl()) : null;
        DisplayOrder displayOrder = DisplayOrder.of(command.displayOrder());
        DisplayPeriod displayPeriod =
                command.hasDisplayPeriod()
                        ? DisplayPeriod.of(command.displayStartDate(), command.displayEndDate())
                        : null;

        existing.update(title, linkUrl, displayOrder, displayPeriod);
    }
}
