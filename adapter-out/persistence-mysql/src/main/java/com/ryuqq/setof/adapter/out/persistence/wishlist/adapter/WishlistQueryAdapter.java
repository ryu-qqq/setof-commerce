package com.ryuqq.setof.adapter.out.persistence.wishlist.adapter;

import com.ryuqq.setof.adapter.out.persistence.wishlist.dto.WishlistItemQueryDto;
import com.ryuqq.setof.adapter.out.persistence.wishlist.entity.WishlistItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.wishlist.mapper.WishlistItemJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.wishlist.repository.WishlistItemQueryDslRepository;
import com.ryuqq.setof.application.wishlist.dto.response.WishlistItemResult;
import com.ryuqq.setof.application.wishlist.port.out.query.WishlistQueryPort;
import com.ryuqq.setof.domain.wishlist.aggregate.WishlistItem;
import com.ryuqq.setof.domain.wishlist.query.WishlistSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * WishlistQueryAdapter - 찜 항목 Query 어댑터.
 *
 * <p>WishlistQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지). fetchSlice는 예외적으로 WishlistItemResult를 반환합니다.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * <p>활성화 조건: persistence.wishlist.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(
        name = "persistence.wishlist.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class WishlistQueryAdapter implements WishlistQueryPort {

    private final WishlistItemQueryDslRepository queryDslRepository;
    private final WishlistItemJpaEntityMapper mapper;

    public WishlistQueryAdapter(
            WishlistItemQueryDslRepository queryDslRepository, WishlistItemJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * 레거시 회원 ID + 상품 그룹 ID로 찜 항목 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @param productGroupId 상품 그룹 ID
     * @return 찜 항목 Optional
     */
    @Override
    public Optional<WishlistItem> findByUserIdAndProductGroupId(Long userId, long productGroupId) {
        return queryDslRepository
                .findByLegacyMemberIdAndProductGroupId(userId, productGroupId)
                .map(mapper::toDomain);
    }

    /**
     * 레거시 회원 ID로 찜 항목 목록 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @return 찜 항목 목록
     */
    @Override
    public List<WishlistItem> findAllByUserId(Long userId) {
        List<WishlistItemJpaEntity> entities = queryDslRepository.findAllByLegacyMemberId(userId);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 커서 기반 찜 목록 슬라이스 조회.
     *
     * <p>product_groups, product_group_images, brand 4개 테이블 조인 쿼리 실행 후 WishlistItemResult로 변환합니다.
     *
     * @param criteria 검색 조건
     * @return 찜 항목 결과 목록
     */
    @Override
    public List<WishlistItemResult> fetchSlice(WishlistSearchCriteria criteria) {
        List<WishlistItemQueryDto> dtos = queryDslRepository.fetchSlice(criteria);
        return dtos.stream().map(mapper::toResult).toList();
    }

    /**
     * 레거시 회원 ID로 찜 항목 개수 조회.
     *
     * <p>Port의 userId 파라미터는 legacy_member_id에 매핑됩니다.
     *
     * @param userId 레거시 회원 ID
     * @return 찜 항목 개수
     */
    @Override
    public long countByUserId(Long userId) {
        return queryDslRepository.countByLegacyMemberId(userId);
    }
}
