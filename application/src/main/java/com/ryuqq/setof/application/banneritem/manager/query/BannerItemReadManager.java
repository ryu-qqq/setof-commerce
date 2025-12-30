package com.ryuqq.setof.application.banneritem.manager.query;

import com.ryuqq.setof.application.banneritem.port.out.query.BannerItemQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * BannerItemReadManager - BannerItem 조회 Manager
 *
 * <p>QueryPort를 통한 조회 로직을 캡슐화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerItemReadManager {

    private final BannerItemQueryPort queryPort;

    public BannerItemReadManager(BannerItemQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * BannerItem ID로 조회
     *
     * @param bannerItemId BannerItem ID
     * @return BannerItem Optional
     */
    public Optional<BannerItem> findById(BannerItemId bannerItemId) {
        return queryPort.findById(bannerItemId);
    }

    /**
     * Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerId Banner ID
     * @return 활성 BannerItem 목록
     */
    public List<BannerItem> findActiveByBannerId(BannerId bannerId) {
        return queryPort.findActiveByBannerId(bannerId);
    }

    /**
     * Banner ID로 전체 아이템 목록 조회
     *
     * @param bannerId Banner ID
     * @return BannerItem 목록
     */
    public List<BannerItem> findAllByBannerId(BannerId bannerId) {
        return queryPort.findAllByBannerId(bannerId);
    }

    /**
     * 여러 Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerIds Banner ID 목록
     * @return 활성 BannerItem 목록
     */
    public List<BannerItem> findActiveByBannerIds(List<BannerId> bannerIds) {
        return queryPort.findActiveByBannerIds(bannerIds);
    }
}
