package com.ryuqq.setof.application.banner.service.command;

import com.ryuqq.setof.application.banner.dto.command.ActivateBannerCommand;
import com.ryuqq.setof.application.banner.manager.command.BannerPersistenceManager;
import com.ryuqq.setof.application.banner.manager.query.BannerReadManager;
import com.ryuqq.setof.application.banner.port.in.command.ActivateBannerUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 배너 활성화 Service
 *
 * @author development-team
 * @since 2.0.0
 */
@Service
@Transactional
public class ActivateBannerService implements ActivateBannerUseCase {

    private final BannerReadManager bannerReadManager;
    private final BannerPersistenceManager bannerPersistenceManager;

    public ActivateBannerService(
            BannerReadManager bannerReadManager,
            BannerPersistenceManager bannerPersistenceManager) {
        this.bannerReadManager = bannerReadManager;
        this.bannerPersistenceManager = bannerPersistenceManager;
    }

    @Override
    public void execute(ActivateBannerCommand command) {
        BannerId bannerId = BannerId.of(command.bannerId());
        Banner banner = bannerReadManager.findById(bannerId);
        banner.activate();
        bannerPersistenceManager.persist(banner);
    }
}
