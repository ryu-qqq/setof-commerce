package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.UpdateBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerGroupUseCase;
import com.ryuqq.setof.application.banner.validator.BannerGroupValidator;
import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroupUpdateData;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import org.springframework.stereotype.Service;

/**
 * UpdateBannerGroupService - 배너 그룹 정보 수정 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은 Manager에서
 * 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>그룹 정보(타이틀, 배너 타입, 노출 기간, 활성 여부)만 수정합니다. 슬라이드 수정은 {@link UpdateBannerSlidesService}를 통해 별도로
 * 처리합니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → UpdateContext 생성 (시간 캡슐화)
 *   <li>Validator를 통해 배너 그룹 존재 확인
 *   <li>도메인 updateGroupInfo() 호출로 그룹 정보만 변경
 *   <li>Manager를 통해 그룹 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class UpdateBannerGroupService implements UpdateBannerGroupUseCase {

    private final BannerGroupCommandFactory bannerGroupCommandFactory;
    private final BannerGroupValidator bannerGroupValidator;
    private final BannerGroupCommandManager bannerGroupCommandManager;

    public UpdateBannerGroupService(
            BannerGroupCommandFactory bannerGroupCommandFactory,
            BannerGroupValidator bannerGroupValidator,
            BannerGroupCommandManager bannerGroupCommandManager) {
        this.bannerGroupCommandFactory = bannerGroupCommandFactory;
        this.bannerGroupValidator = bannerGroupValidator;
        this.bannerGroupCommandManager = bannerGroupCommandManager;
    }

    @Override
    public void execute(UpdateBannerGroupCommand command) {
        UpdateContext<BannerGroupId, BannerGroupUpdateData> context =
                bannerGroupCommandFactory.createUpdateContext(command);

        BannerGroup bannerGroup = bannerGroupValidator.findExistingOrThrow(context.id());
        BannerGroupUpdateData data = context.updateData();
        bannerGroup.updateGroupInfo(
                data.title(),
                data.bannerType(),
                data.displayPeriod(),
                data.active(),
                context.changedAt());

        bannerGroupCommandManager.persist(bannerGroup);
    }
}
