package com.ryuqq.setof.application.banneritem.manager.command;

import com.ryuqq.setof.application.banneritem.port.out.command.BannerItemPersistencePort;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerItemPersistenceManager - BannerItem 영속성 Manager
 *
 * <p>PersistencePort를 통한 저장 로직을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerItemPersistenceManager {

    private final BannerItemPersistencePort persistencePort;

    public BannerItemPersistenceManager(BannerItemPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    /**
     * BannerItem 저장
     *
     * @param bannerItem 저장할 BannerItem
     * @return 저장된 BannerItem ID
     */
    public BannerItemId persist(BannerItem bannerItem) {
        return persistencePort.persist(bannerItem);
    }

    /**
     * BannerItem 목록 일괄 저장
     *
     * @param bannerItems 저장할 BannerItem 목록
     * @return 저장된 BannerItem ID 목록
     */
    public List<BannerItemId> persistAll(List<BannerItem> bannerItems) {
        return persistencePort.persistAll(bannerItems);
    }

    /**
     * Banner ID로 모든 아이템 삭제
     *
     * @param bannerId Banner ID
     */
    public void deleteAllByBannerId(BannerId bannerId) {
        persistencePort.deleteAllByBannerId(bannerId);
    }
}
