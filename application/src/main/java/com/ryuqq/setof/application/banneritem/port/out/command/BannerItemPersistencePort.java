package com.ryuqq.setof.application.banneritem.port.out.command;

import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;

/**
 * BannerItem 영속성 Outbound Port (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BannerItemPersistencePort {

    /**
     * BannerItem 저장/수정 (JPA merge 활용)
     *
     * @param bannerItem 저장/수정할 BannerItem
     * @return 저장된 BannerItem ID
     */
    BannerItemId persist(BannerItem bannerItem);

    /**
     * BannerItem 목록 일괄 저장
     *
     * @param bannerItems 저장할 BannerItem 목록
     * @return 저장된 BannerItem ID 목록
     */
    List<BannerItemId> persistAll(List<BannerItem> bannerItems);

    /**
     * Banner ID로 모든 아이템 삭제 (Soft Delete)
     *
     * @param bannerId Banner ID
     */
    void deleteAllByBannerId(BannerId bannerId);
}
