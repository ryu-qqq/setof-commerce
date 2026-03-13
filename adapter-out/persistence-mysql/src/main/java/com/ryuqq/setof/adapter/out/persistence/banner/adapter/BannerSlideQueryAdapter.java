package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideQueryDslRepository;
import com.ryuqq.setof.application.banner.port.out.BannerSlideQueryPort;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * BannerSlideQueryAdapter - 배너 슬라이드 Query 어댑터.
 *
 * <p>BannerSlideQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.legacy.banner.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.banner.enabled", havingValue = "false")
public class BannerSlideQueryAdapter implements BannerSlideQueryPort {

    private final BannerSlideQueryDslRepository queryDslRepository;
    private final BannerSlideJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: Mapper + QueryDslRepository 의존.
     *
     * @param queryDslRepository QueryDSL 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public BannerSlideQueryAdapter(
            BannerSlideQueryDslRepository queryDslRepository, BannerSlideJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 배너 타입별 전시 중인 슬라이드 목록 조회.
     *
     * @param bannerType 배너 타입
     * @return BannerSlide 목록
     */
    @Override
    public List<BannerSlide> fetchDisplayBannerSlides(BannerType bannerType) {
        return queryDslRepository.fetchDisplaySlides(bannerType.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
