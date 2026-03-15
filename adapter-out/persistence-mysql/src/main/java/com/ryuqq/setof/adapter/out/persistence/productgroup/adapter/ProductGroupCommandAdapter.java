package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.application.productgroup.port.out.command.ProductGroupCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

/**
 * ProductGroupCommandAdapter - 상품 그룹 Command 어댑터.
 *
 * <p>ProductGroupCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-003: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class ProductGroupCommandAdapter implements ProductGroupCommandPort {

    private final ProductGroupJpaRepository jpaRepository;
    private final ProductGroupJpaEntityMapper mapper;
    private final EntityManager entityManager;

    public ProductGroupCommandAdapter(
            ProductGroupJpaRepository jpaRepository,
            ProductGroupJpaEntityMapper mapper,
            EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    /**
     * 상품 그룹을 저장합니다.
     *
     * <p>ID가 지정된 경우(마이그레이션) native INSERT로 처리하고, ID가 없는 경우(일반 등록) JPA save로 auto_increment를 사용합니다.
     *
     * @param productGroup 저장할 상품 그룹 도메인 객체
     * @return 저장된 상품 그룹 ID
     */
    @Override
    public Long persist(ProductGroup productGroup) {
        ProductGroupJpaEntity entity = mapper.toEntity(productGroup);
        if (entity.getId() != null) {
            entityManager
                    .createNativeQuery(
                            "INSERT INTO product_groups (id, seller_id, brand_id, category_id,"
                                + " shipping_policy_id, refund_policy_id, product_group_name,"
                                + " option_type, regular_price, current_price, sale_price,"
                                + " discount_rate, status, created_at, updated_at, deleted_at)"
                                + " VALUES (:id, :sellerId, :brandId, :categoryId,"
                                + " :shippingPolicyId, :refundPolicyId, :productGroupName,"
                                + " :optionType, :regularPrice, :currentPrice, :salePrice,"
                                + " :discountRate, :status, :createdAt, :updatedAt, :deletedAt)")
                    .setParameter("id", entity.getId())
                    .setParameter("sellerId", entity.getSellerId())
                    .setParameter("brandId", entity.getBrandId())
                    .setParameter("categoryId", entity.getCategoryId())
                    .setParameter("shippingPolicyId", entity.getShippingPolicyId())
                    .setParameter("refundPolicyId", entity.getRefundPolicyId())
                    .setParameter("productGroupName", entity.getProductGroupName())
                    .setParameter("optionType", entity.getOptionType())
                    .setParameter("regularPrice", entity.getRegularPrice())
                    .setParameter("currentPrice", entity.getCurrentPrice())
                    .setParameter("salePrice", entity.getSalePrice())
                    .setParameter("discountRate", entity.getDiscountRate())
                    .setParameter("status", entity.getStatus())
                    .setParameter("createdAt", entity.getCreatedAt())
                    .setParameter("updatedAt", entity.getUpdatedAt())
                    .setParameter("deletedAt", entity.getDeletedAt())
                    .executeUpdate();
            return entity.getId();
        }
        ProductGroupJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }
}
