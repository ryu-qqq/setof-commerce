package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.RegisterBannerGroupCommand;
import com.ryuqq.setof.application.banner.factory.BannerGroupCommandFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupCommandManager;
import com.ryuqq.setof.application.banner.port.in.command.RegisterBannerGroupUseCase;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import org.springframework.stereotype.Service;

/**
 * RegisterBannerGroupService - 배너 그룹 등록 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다. @Transactional은 Manager에서
 * 선언하며 Service에서는 선언하지 않습니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 Command → BannerGroup 도메인 객체 생성
 *   <li>Manager를 통해 배너 그룹 및 슬라이드 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterBannerGroupService implements RegisterBannerGroupUseCase {

    private final BannerGroupCommandFactory bannerGroupCommandFactory;
    private final BannerGroupCommandManager bannerGroupCommandManager;

    public RegisterBannerGroupService(
            BannerGroupCommandFactory bannerGroupCommandFactory,
            BannerGroupCommandManager bannerGroupCommandManager) {
        this.bannerGroupCommandFactory = bannerGroupCommandFactory;
        this.bannerGroupCommandManager = bannerGroupCommandManager;
    }

    @Override
    public Long execute(RegisterBannerGroupCommand command) {
        BannerGroup bannerGroup = bannerGroupCommandFactory.create(command);
        return bannerGroupCommandManager.persistWithSlides(bannerGroup);
    }
}
