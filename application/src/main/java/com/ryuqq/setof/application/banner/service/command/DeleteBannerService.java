package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.DeleteBannerCommand;
import com.ryuqq.setof.application.banner.manager.command.BannerPersistenceManager;
import com.ryuqq.setof.application.banner.manager.query.BannerReadManager;
import com.ryuqq.setof.application.banner.port.in.command.DeleteBannerUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import org.springframework.stereotype.Service;

/**
 * Banner 삭제 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteBannerService implements DeleteBannerUseCase {

    private final BannerReadManager bannerReadManager;
    private final BannerPersistenceManager bannerPersistenceManager;

    public DeleteBannerService(
            BannerReadManager bannerReadManager,
            BannerPersistenceManager bannerPersistenceManager) {
        this.bannerReadManager = bannerReadManager;
        this.bannerPersistenceManager = bannerPersistenceManager;
    }

    @Override
    public void execute(DeleteBannerCommand command) {
        Banner banner = bannerReadManager.findById(command.bannerId());
        banner.delete();
        bannerPersistenceManager.persist(banner);
    }
}
