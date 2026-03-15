package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.RemoveBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.port.in.command.RemoveBannerGroupUseCase;
import com.ryuqq.setof.application.banner.validator.BannerGroupValidator;
import com.ryuqq.setof.application.common.dto.command.StatusChangeContext;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import org.springframework.stereotype.Service;

/**
 * RemoveBannerGroupService - 배너 그룹 삭제 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은 Manager에서
 * 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → StatusChangeContext 생성 (삭제 시각 캡슐화)
 *   <li>Validator를 통해 배너 그룹 존재 확인
 *   <li>도메인 remove() 호출로 소프트 삭제 처리
 *   <li>Manager를 통해 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RemoveBannerGroupService implements RemoveBannerGroupUseCase {

    private final BannerGroupCommandFactory bannerGroupCommandFactory;
    private final BannerGroupValidator bannerGroupValidator;
    private final BannerGroupCommandManager bannerGroupCommandManager;

    public RemoveBannerGroupService(
            BannerGroupCommandFactory bannerGroupCommandFactory,
            BannerGroupValidator bannerGroupValidator,
            BannerGroupCommandManager bannerGroupCommandManager) {
        this.bannerGroupCommandFactory = bannerGroupCommandFactory;
        this.bannerGroupValidator = bannerGroupValidator;
        this.bannerGroupCommandManager = bannerGroupCommandManager;
    }

    @Override
    public void execute(RemoveBannerGroupCommand command) {
        StatusChangeContext<BannerGroupId> context =
                bannerGroupCommandFactory.createRemoveContext(command);

        BannerGroup bannerGroup = bannerGroupValidator.findExistingOrThrow(context.id());
        bannerGroup.remove(context.changedAt());

        bannerGroupCommandManager.persist(bannerGroup);
    }
}
