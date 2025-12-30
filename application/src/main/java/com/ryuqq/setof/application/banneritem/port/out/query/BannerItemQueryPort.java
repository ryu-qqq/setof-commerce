package com.ryuqq.setof.application.banneritem.port.out.query;

import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import java.util.Optional;

/**
 * BannerItem 조회 Outbound Port (Query)
 *
 * <p>배너 아이템 조회를 위한 Outbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BannerItemQueryPort {

    /**
     * BannerItem ID로 조회
     *
     * @param bannerItemId BannerItem ID
     * @return BannerItem Optional
     */
    Optional<BannerItem> findById(BannerItemId bannerItemId);

    /**
     * Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerId Banner ID
     * @return 활성 BannerItem 목록 (displayOrder 순)
     */
    List<BannerItem> findActiveByBannerId(BannerId bannerId);

    /**
     * Banner ID로 전체 아이템 목록 조회 (삭제 제외)
     *
     * @param bannerId Banner ID
     * @return BannerItem 목록 (displayOrder 순)
     */
    List<BannerItem> findAllByBannerId(BannerId bannerId);

    /**
     * 여러 Banner ID로 활성 아이템 목록 조회
     *
     * @param bannerIds Banner ID 목록
     * @return 활성 BannerItem 목록
     */
    List<BannerItem> findActiveByBannerIds(List<BannerId> bannerIds);
}
