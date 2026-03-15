package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
import com.ryuqq.setof.application.banner.port.out.command.BannerGroupCommandPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerGroupCommandAdapter - 배너 그룹 Command 어댑터.
 *
 * <p>BannerGroupCommandPort를 구현하여 배너 그룹 및 슬라이드의 저장·갱신을 담당합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-005: Domain → Entity 변환 시 Mapper 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupCommandAdapter implements BannerGroupCommandPort {

    private final BannerGroupJpaRepository bannerGroupJpaRepository;
    private final BannerSlideJpaRepository bannerSlideJpaRepository;
    private final BannerGroupJpaEntityMapper bannerGroupMapper;
    private final BannerSlideJpaEntityMapper bannerSlideMapper;

    public BannerGroupCommandAdapter(
            BannerGroupJpaRepository bannerGroupJpaRepository,
            BannerSlideJpaRepository bannerSlideJpaRepository,
            BannerGroupJpaEntityMapper bannerGroupMapper,
            BannerSlideJpaEntityMapper bannerSlideMapper) {
        this.bannerGroupJpaRepository = bannerGroupJpaRepository;
        this.bannerSlideJpaRepository = bannerSlideJpaRepository;
        this.bannerGroupMapper = bannerGroupMapper;
        this.bannerSlideMapper = bannerSlideMapper;
    }

    @Override
    public Long persist(BannerGroup bannerGroup) {
        BannerGroupJpaEntity entity = bannerGroupMapper.toEntity(bannerGroup);
        return bannerGroupJpaRepository.save(entity).getId();
    }

    @Override
    public void persistSlides(long bannerGroupId, List<BannerSlide> slides) {
        List<BannerSlideJpaEntity> entities =
                slides.stream()
                        .map(slide -> bannerSlideMapper.toEntity(bannerGroupId, slide))
                        .toList();
        bannerSlideJpaRepository.saveAll(entities);
    }

    @Override
    public void updateSlides(long bannerGroupId, List<BannerSlide> slides) {
        List<BannerSlideJpaEntity> entities =
                slides.stream()
                        .map(slide -> bannerSlideMapper.toEntity(bannerGroupId, slide))
                        .toList();
        bannerSlideJpaRepository.saveAll(entities);
    }
}
