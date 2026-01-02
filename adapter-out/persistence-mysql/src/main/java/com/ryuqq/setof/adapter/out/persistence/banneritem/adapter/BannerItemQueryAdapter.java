package com.ryuqq.setof.adapter.out.persistence.banneritem.adapter;

import com.ryuqq.setof.adapter.out.persistence.banneritem.entity.CmsBannerItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banneritem.mapper.BannerItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banneritem.repository.CmsBannerItemQueryDslRepository;
import com.ryuqq.setof.application.banneritem.port.out.query.BannerItemQueryPort;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BannerItemQueryAdapter - BannerItem 조회 Adapter
 *
 * <p>BannerItemQueryPort 구현체로서 데이터베이스에서 BannerItem을 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BannerItemQueryAdapter implements BannerItemQueryPort {

    private final CmsBannerItemQueryDslRepository queryDslRepository;
    private final BannerItemJpaEntityMapper mapper;

    public BannerItemQueryAdapter(
            CmsBannerItemQueryDslRepository queryDslRepository, BannerItemJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<BannerItem> findById(BannerItemId bannerItemId) {
        return queryDslRepository.findById(bannerItemId.value()).map(mapper::toDomain);
    }

    @Override
    public List<BannerItem> findActiveByBannerId(BannerId bannerId) {
        List<CmsBannerItemJpaEntity> entities =
                queryDslRepository.findActiveByBannerId(bannerId.value());
        return mapper.toDomainList(entities);
    }

    @Override
    public List<BannerItem> findAllByBannerId(BannerId bannerId) {
        List<CmsBannerItemJpaEntity> entities =
                queryDslRepository.findAllByBannerId(bannerId.value());
        return mapper.toDomainList(entities);
    }

    @Override
    public List<BannerItem> findActiveByBannerIds(List<BannerId> bannerIds) {
        List<Long> ids = bannerIds.stream().map(BannerId::value).toList();
        List<CmsBannerItemJpaEntity> entities = queryDslRepository.findActiveByBannerIds(ids);
        return mapper.toDomainList(entities);
    }
}
