package com.ryuqq.setof.application.banner.manager.command;

import com.ryuqq.setof.application.banner.port.out.command.BannerPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Banner 영속성 Manager (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Transactional
public class BannerPersistenceManager {

    private final BannerPersistencePort bannerPersistencePort;

    public BannerPersistenceManager(BannerPersistencePort bannerPersistencePort) {
        this.bannerPersistencePort = bannerPersistencePort;
    }

    /**
     * 배너 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param banner 저장/수정할 배너
     * @return 저장된 Banner ID
     */
    public BannerId persist(Banner banner) {
        return bannerPersistencePort.persist(banner);
    }
}
