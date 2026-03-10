package com.ryuqq.setof.adapter.out.persistence.productgroupdescription.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.entity.DescriptionImageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.mapper.ProductGroupDescriptionJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroupdescription.repository.DescriptionImageJpaRepository;
import com.ryuqq.setof.application.productdescription.port.out.command.DescriptionImageCommandPort;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DescriptionImageCommandAdapter - 상세설명 이미지 Command 어댑터.
 *
 * <p>DescriptionImageCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class DescriptionImageCommandAdapter implements DescriptionImageCommandPort {

    private final DescriptionImageJpaRepository jpaRepository;
    private final ProductGroupDescriptionJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 의존.
     *
     * @param jpaRepository JPA 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public DescriptionImageCommandAdapter(
            DescriptionImageJpaRepository jpaRepository,
            ProductGroupDescriptionJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    /**
     * 이미지 목록 일괄 저장.
     *
     * @param images DescriptionImage 도메인 객체 목록
     * @param productGroupDescriptionId 상세설명 ID
     */
    @Override
    public void persistAll(List<DescriptionImage> images, Long productGroupDescriptionId) {
        List<DescriptionImageJpaEntity> entities =
                images.stream()
                        .map(image -> mapper.toImageEntity(image, productGroupDescriptionId))
                        .toList();
        jpaRepository.saveAll(entities);
    }

    /**
     * 상세설명 ID 기준으로 이미지 삭제.
     *
     * @param productGroupDescriptionId 상세설명 ID
     */
    @Override
    public void deleteByProductGroupDescriptionId(Long productGroupDescriptionId) {
        jpaRepository.deleteByProductGroupDescriptionId(productGroupDescriptionId);
    }
}
