package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideQueryDslRepository;
import com.ryuqq.setof.application.banner.port.out.BannerGroupQueryPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * BannerGroupQueryAdapter - 배너 그룹 Query 어댑터.
 *
 * <p>BannerGroupQueryPort를 구현하여 배너 그룹 단건 조회를 담당합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity → Domain 변환 시 Mapper 사용.
 *
 * <p>활성화 조건: persistence.legacy.banner.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.banner.enabled", havingValue = "false")
public class BannerGroupQueryAdapter implements BannerGroupQueryPort {

    private final BannerSlideQueryDslRepository queryDslRepository;
    private final BannerGroupJpaEntityMapper bannerGroupMapper;
    private final BannerSlideJpaEntityMapper bannerSlideMapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: QueryDslRepository + Mapper 의존.
     *
     * @param queryDslRepository QueryDSL 레포지토리
     * @param bannerGroupMapper 배너 그룹 매퍼
     * @param bannerSlideMapper 배너 슬라이드 매퍼
     */
    public BannerGroupQueryAdapter(
            BannerSlideQueryDslRepository queryDslRepository,
            BannerGroupJpaEntityMapper bannerGroupMapper,
            BannerSlideJpaEntityMapper bannerSlideMapper) {
        this.queryDslRepository = queryDslRepository;
        this.bannerGroupMapper = bannerGroupMapper;
        this.bannerSlideMapper = bannerSlideMapper;
    }

    /**
     * ID로 배너 그룹을 조회합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @return 배너 그룹 (없으면 empty)
     */
    @Override
    public Optional<BannerGroup> findById(long bannerGroupId) {
        BannerGroupJpaEntity groupEntity = queryDslRepository.findBannerGroupById(bannerGroupId);
        if (groupEntity == null) {
            return Optional.empty();
        }
        List<BannerSlide> slides =
                queryDslRepository.findSlidesByGroupId(bannerGroupId).stream()
                        .map(bannerSlideMapper::toDomain)
                        .toList();
        return Optional.of(bannerGroupMapper.toDomain(groupEntity, slides));
    }

    @Override
    public List<BannerGroup> findByCriteria(BannerGroupSearchCriteria criteria) {
        String bannerType = criteria.bannerType() != null ? criteria.bannerType().name() : null;

        List<BannerGroupJpaEntity> entities =
                queryDslRepository.searchBannerGroups(
                        bannerType,
                        criteria.active(),
                        criteria.displayPeriodStart(),
                        criteria.displayPeriodEnd(),
                        criteria.titleKeyword(),
                        criteria.lastDomainId(),
                        criteria.offset(),
                        criteria.size(),
                        criteria.isNoOffset());

        return entities.stream()
                .map(entity -> bannerGroupMapper.toDomain(entity, List.of()))
                .toList();
    }

    @Override
    public long countByCriteria(BannerGroupSearchCriteria criteria) {
        String bannerType = criteria.bannerType() != null ? criteria.bannerType().name() : null;

        return queryDslRepository.countBannerGroups(
                bannerType,
                criteria.active(),
                criteria.displayPeriodStart(),
                criteria.displayPeriodEnd(),
                criteria.titleKeyword());
    }
}
