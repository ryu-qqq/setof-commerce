package com.ryuqq.setof.storage.legacy.banner.adapter;

import com.ryuqq.setof.application.banner.port.out.BannerSlideQueryPort;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.storage.legacy.banner.entity.LegacyBannerItemEntity;
import com.ryuqq.setof.storage.legacy.banner.mapper.LegacyBannerMapper;
import com.ryuqq.setof.storage.legacy.banner.repository.LegacyBannerQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyBannerQueryAdapter - 레거시 배너 조회 Adapter.
 *
 * <p>레거시 DB를 조회하여 BannerSlide 도메인 객체로 변환 후 반환한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyBannerQueryAdapter implements BannerSlideQueryPort {

    private final LegacyBannerQueryDslRepository repository;
    private final LegacyBannerMapper mapper;

    public LegacyBannerQueryAdapter(
            LegacyBannerQueryDslRepository repository, LegacyBannerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BannerSlide> fetchDisplayBannerSlides(BannerType bannerType) {
        List<LegacyBannerItemEntity> entities = repository.fetchBannerItems(bannerType.name());
        return mapper.toDomains(entities);
    }
}
