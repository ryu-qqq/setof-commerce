package com.ryuqq.setof.adapter.out.persistence.brand.mapper;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import com.ryuqq.setof.domain.brand.vo.BrandLogoUrl;
import com.ryuqq.setof.domain.brand.vo.BrandNameEn;
import com.ryuqq.setof.domain.brand.vo.BrandNameKo;
import com.ryuqq.setof.domain.brand.vo.BrandStatus;
import org.springframework.stereotype.Component;

/**
 * BrandJpaEntityMapper - Entity <-> Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Brand 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Brand -> BrandJpaEntity (저장용)
 *   <li>BrandJpaEntity -> Brand (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandJpaEntityMapper {

    /**
     * Domain -> Entity 변환
     *
     * @param domain Brand 도메인
     * @return BrandJpaEntity
     */
    public BrandJpaEntity toEntity(Brand domain) {
        return BrandJpaEntity.of(
                domain.getIdValue(),
                domain.getCodeValue(),
                domain.getNameKoValue(),
                domain.getNameEnValue(),
                domain.getLogoUrlValue(),
                domain.getStatusValue(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * Entity -> Domain 변환
     *
     * @param entity BrandJpaEntity
     * @return Brand 도메인
     */
    public Brand toDomain(BrandJpaEntity entity) {
        return Brand.reconstitute(
                BrandId.of(entity.getId()),
                BrandCode.of(entity.getCode()),
                BrandNameKo.of(entity.getNameKo()),
                BrandNameEn.of(entity.getNameEn()),
                BrandLogoUrl.of(entity.getLogoUrl()),
                BrandStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
