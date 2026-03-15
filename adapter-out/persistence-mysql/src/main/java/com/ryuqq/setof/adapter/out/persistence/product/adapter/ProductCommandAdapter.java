package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.application.product.port.out.command.ProductCommandPort;
import com.ryuqq.setof.domain.product.aggregate.Product;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductCommandAdapter - 상품 Command 어댑터.
 *
 * <p>ProductCommandPort를 구현하여 영속성 계층과 연결합니다.
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
public class ProductCommandAdapter implements ProductCommandPort {

    private final ProductJpaRepository jpaRepository;
    private final ProductJpaEntityMapper mapper;
    private final EntityManager entityManager;

    public ProductCommandAdapter(
            ProductJpaRepository jpaRepository,
            ProductJpaEntityMapper mapper,
            EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    /**
     * 상품 저장.
     *
     * <p>ID가 지정된 경우(마이그레이션) native INSERT로 처리하고, ID가 없는 경우(일반 등록) JPA save로 auto_increment를 사용합니다.
     *
     * @param product 상품 도메인 객체
     * @return 저장된 상품 ID
     */
    @Override
    public Long persist(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        if (entity.getId() != null) {
            insertWithNativeQuery(entity);
            return entity.getId();
        }
        ProductJpaEntity saved = jpaRepository.save(entity);
        return saved.getId();
    }

    /**
     * 상품 목록 일괄 저장.
     *
     * <p>각 상품의 ID 유무에 따라 native INSERT 또는 JPA save로 분기합니다.
     *
     * @param products 상품 도메인 객체 목록
     * @return 저장된 상품 ID 목록
     */
    @Override
    public List<Long> persistAll(List<Product> products) {
        List<ProductJpaEntity> newEntities = new ArrayList<>();
        List<Long> resultIds = new ArrayList<>();

        for (Product product : products) {
            ProductJpaEntity entity = mapper.toEntity(product);
            if (entity.getId() != null) {
                insertWithNativeQuery(entity);
                resultIds.add(entity.getId());
            } else {
                newEntities.add(entity);
            }
        }

        if (!newEntities.isEmpty()) {
            List<ProductJpaEntity> saved = jpaRepository.saveAll(newEntities);
            saved.forEach(e -> resultIds.add(e.getId()));
        }

        return resultIds;
    }

    private void insertWithNativeQuery(ProductJpaEntity entity) {
        entityManager
                .createNativeQuery(
                        "INSERT INTO products (id, product_group_id, sku_code, regular_price,"
                                + " current_price, sale_price, discount_rate, stock_quantity,"
                                + " status, sort_order, created_at, updated_at)"
                                + " VALUES (:id, :productGroupId, :skuCode, :regularPrice,"
                                + " :currentPrice, :salePrice, :discountRate, :stockQuantity,"
                                + " :status, :sortOrder, :createdAt, :updatedAt)")
                .setParameter("id", entity.getId())
                .setParameter("productGroupId", entity.getProductGroupId())
                .setParameter("skuCode", entity.getSkuCode())
                .setParameter("regularPrice", entity.getRegularPrice())
                .setParameter("currentPrice", entity.getCurrentPrice())
                .setParameter("salePrice", entity.getSalePrice())
                .setParameter("discountRate", entity.getDiscountRate())
                .setParameter("stockQuantity", entity.getStockQuantity())
                .setParameter("status", entity.getStatus())
                .setParameter("sortOrder", entity.getSortOrder())
                .setParameter("createdAt", entity.getCreatedAt())
                .setParameter("updatedAt", entity.getUpdatedAt())
                .executeUpdate();
    }
}
