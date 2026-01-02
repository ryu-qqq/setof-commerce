package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.UpdateBannerCommand;
import com.ryuqq.setof.application.banner.factory.command.BannerCommandFactory;
import com.ryuqq.setof.application.banner.manager.command.BannerPersistenceManager;
import com.ryuqq.setof.application.banner.manager.query.BannerReadManager;
import com.ryuqq.setof.application.banner.port.in.command.UpdateBannerUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import org.springframework.stereotype.Service;

/**
 * Banner 수정 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateBannerService implements UpdateBannerUseCase {

    private final BannerReadManager bannerReadManager;
    private final BannerCommandFactory bannerCommandFactory;
    private final BannerPersistenceManager bannerPersistenceManager;

    public UpdateBannerService(
            BannerReadManager bannerReadManager,
            BannerCommandFactory bannerCommandFactory,
            BannerPersistenceManager bannerPersistenceManager) {
        this.bannerReadManager = bannerReadManager;
        this.bannerCommandFactory = bannerCommandFactory;
        this.bannerPersistenceManager = bannerPersistenceManager;
    }

    @Override
    public void execute(UpdateBannerCommand command) {
        Banner banner = bannerReadManager.findById(command.bannerId());
        bannerCommandFactory.applyUpdateBanner(banner, command);
        bannerPersistenceManager.persist(banner);
    }
}
