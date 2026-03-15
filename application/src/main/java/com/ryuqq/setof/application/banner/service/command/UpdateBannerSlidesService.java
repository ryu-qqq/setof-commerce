package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.UpdateBannerSlidesCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerSlidesUseCase;
import com.ryuqq.setof.application.banner.validator.BannerGroupValidator;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * UpdateBannerSlidesService - 배너 슬라이드 일괄 수정 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은 Manager에서
 * 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>슬라이드 Diff 기반으로 추가/수정/삭제를 처리합니다. 그룹 정보 수정은 {@link UpdateBannerGroupService}를 통해 별도로 처리합니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → UpdateContext 생성 (시간 캡슐화)
 *   <li>Validator를 통해 배너 그룹 존재 확인
 *   <li>도메인 updateSlides() 호출로 슬라이드 Diff 계산
 *   <li>Manager를 통해 그룹 저장
 *   <li>Manager를 통해 슬라이드 Diff 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateBannerSlidesService implements UpdateBannerSlidesUseCase {

    private final BannerGroupCommandFactory bannerGroupCommandFactory;
    private final BannerGroupValidator bannerGroupValidator;
    private final BannerGroupCommandManager bannerGroupCommandManager;

    public UpdateBannerSlidesService(
            BannerGroupCommandFactory bannerGroupCommandFactory,
            BannerGroupValidator bannerGroupValidator,
            BannerGroupCommandManager bannerGroupCommandManager) {
        this.bannerGroupCommandFactory = bannerGroupCommandFactory;
        this.bannerGroupValidator = bannerGroupValidator;
        this.bannerGroupCommandManager = bannerGroupCommandManager;
    }

    @Override
    public void execute(UpdateBannerSlidesCommand command) {
        UpdateContext<BannerGroupId, List<BannerGroupUpdateData.SlideEntry>> context =
                bannerGroupCommandFactory.createSlideUpdateContext(command);

        BannerGroup bannerGroup = bannerGroupValidator.findExistingOrThrow(context.id());
        BannerSlideDiff diff = bannerGroup.updateSlides(context.updateData(), context.changedAt());

        bannerGroupCommandManager.persist(bannerGroup);
        bannerGroupCommandManager.persistSlideDiff(bannerGroup.idValue(), diff);
    }
}
