package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.CreateBannerCommand;
import com.ryuqq.setof.application.banner.factory.command.BannerCommandFactory;
import com.ryuqq.setof.application.banner.manager.command.BannerPersistenceManager;
import com.ryuqq.setof.application.banner.port.in.command.CreateBannerUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import org.springframework.stereotype.Service;

/**
 * Banner 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateBannerService implements CreateBannerUseCase {

    private final BannerCommandFactory bannerCommandFactory;
    private final BannerPersistenceManager bannerPersistenceManager;

    public CreateBannerService(
            BannerCommandFactory bannerCommandFactory,
            BannerPersistenceManager bannerPersistenceManager) {
        this.bannerCommandFactory = bannerCommandFactory;
        this.bannerPersistenceManager = bannerPersistenceManager;
    }

    @Override
    public Long execute(CreateBannerCommand command) {
        Banner banner = bannerCommandFactory.createBanner(command);
        return bannerPersistenceManager.persist(banner).value();
    }
}
