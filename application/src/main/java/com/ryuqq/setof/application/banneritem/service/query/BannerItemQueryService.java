package com.ryuqq.setof.application.banneritem.service.query;

import com.ryuqq.setof.application.banneritem.assembler.BannerItemAssembler;
import com.ryuqq.setof.application.banneritem.dto.response.BannerItemResponse;
import com.ryuqq.setof.application.banneritem.manager.query.BannerItemReadManager;
import com.ryuqq.setof.application.banneritem.port.in.query.GetBannerItemsUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BannerItemQueryService - BannerItem 조회 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class BannerItemQueryService implements GetBannerItemsUseCase {

    private final BannerItemReadManager readManager;
    private final BannerItemAssembler assembler;

    public BannerItemQueryService(
            BannerItemReadManager readManager, BannerItemAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<BannerItemResponse> getActiveByBannerId(BannerId bannerId) {
        List<BannerItem> items = readManager.findActiveByBannerId(bannerId);
        return assembler.toResponseList(items);
    }

    @Override
    public List<BannerItemResponse> getActiveByBannerIds(List<BannerId> bannerIds) {
        List<BannerItem> items = readManager.findActiveByBannerIds(bannerIds);
        return assembler.toResponseList(items);
    }
}
