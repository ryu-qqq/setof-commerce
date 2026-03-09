package com.ryuqq.setof.storage.legacy.composite.qna.repository;

import static com.ryuqq.setof.storage.legacy.brand.entity.QLegacyBrandEntity.legacyBrandEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotOptionDetailEntity.legacyOrderSnapshotOptionDetailEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductGroupEntity.legacyOrderSnapshotProductGroupEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductGroupImageEntity.legacyOrderSnapshotProductGroupImageEntity;
import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderSnapshotProductOptionEntity.legacyOrderSnapshotProductOptionEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaAnswerEntity.legacyQnaAnswerEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaEntity.legacyQnaEntity;
import static com.ryuqq.setof.storage.legacy.qna.entity.QLegacyQnaOrderEntity.legacyQnaOrderEntity;
import static com.ryuqq.setof.storage.legacy.seller.entity.QLegacySellerEntity.legacySellerEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.composite.qna.condition.LegacyWebQnaCompositeConditionBuilder;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebMyOrderQnaQueryDto;
import com.ryuqq.setof.storage.legacy.composite.qna.dto.LegacyWebQnaAnswerQueryDto;
import com.ryuqq.setof.storage.legacy.order.entity.LegacyOrderSnapshotProductGroupImageEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyWebQnaOrderQueryDslRepository - 주문 Q&A 복합 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * <p>내 주문 Q&A 조회 시 주문 스냅샷(상품그룹, 이미지) + 주문 정보 JOIN이 필요한 복합 조회를 처리합니다.
 *
 * <p>옵션 정보는 별도 쿼리로 조회합니다 (cartesian product 방지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyWebQnaOrderQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private final LegacyWebQnaCompositeConditionBuilder conditionBuilder;

    public LegacyWebQnaOrderQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory,
            LegacyWebQnaCompositeConditionBuilder conditionBuilder) {
        this.queryFactory = queryFactory;
        this.conditionBuilder = conditionBuilder;
    }

    /**
     * 내 주문 Q&A 상세 목록 조회 (GroupBy + 답변 + 주문 스냅샷 상품 정보 포함).
     *
     * <p>JOIN 경로: qna → qna_order → orders → order_snapshot_product_group → brand +
     * order_snapshot_product_group_image
     *
     * @param qnaIds Q&A ID 목록
     * @return 내 주문 Q&A 상세 목록
     */
    public List<LegacyWebMyOrderQnaQueryDto> fetchMyOrderQnasByIds(List<Long> qnaIds) {
        if (qnaIds == null || qnaIds.isEmpty()) {
            return List.of();
        }

        return queryFactory
                .from(legacyQnaEntity)
                .leftJoin(legacyQnaAnswerEntity)
                .on(legacyQnaAnswerEntity.qnaId.eq(legacyQnaEntity.id))
                .join(legacyQnaOrderEntity)
                .on(legacyQnaOrderEntity.qnaId.eq(legacyQnaEntity.id))
                .join(legacyOrderEntity)
                .on(legacyOrderEntity.id.eq(legacyQnaOrderEntity.orderId))
                .join(legacyOrderSnapshotProductGroupEntity)
                .on(legacyOrderSnapshotProductGroupEntity.orderId.eq(legacyQnaOrderEntity.orderId))
                .join(legacyBrandEntity)
                .on(legacyBrandEntity.id.eq(legacyOrderSnapshotProductGroupEntity.brandId))
                .join(legacyOrderSnapshotProductGroupImageEntity)
                .on(
                        legacyOrderSnapshotProductGroupImageEntity.orderId.eq(
                                legacyQnaOrderEntity.orderId),
                        legacyOrderSnapshotProductGroupImageEntity.productGroupImageType.eq(
                                LegacyOrderSnapshotProductGroupImageEntity.ProductGroupImageType
                                        .MAIN),
                        legacyOrderSnapshotProductGroupImageEntity.deleteYn.eq(
                                LegacyOrderSnapshotProductGroupImageEntity.Yn.N))
                .join(legacySellerEntity)
                .on(legacySellerEntity.id.eq(legacyQnaEntity.sellerId))
                .leftJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyQnaEntity.userId))
                .where(conditionBuilder.qnaIdIn(qnaIds), conditionBuilder.notDeleted())
                .orderBy(legacyQnaEntity.id.desc())
                .transform(
                        GroupBy.groupBy(legacyQnaEntity.id)
                                .list(
                                        Projections.constructor(
                                                LegacyWebMyOrderQnaQueryDto.class,
                                                legacyQnaEntity.id,
                                                legacyQnaEntity.qnaContents.title,
                                                legacyQnaEntity.qnaContents.content,
                                                legacyQnaEntity.privateYn.stringValue(),
                                                legacyQnaEntity.qnaStatus,
                                                legacyQnaEntity.qnaType,
                                                legacyQnaEntity.qnaDetailType,
                                                legacyQnaEntity.userType,
                                                legacyQnaEntity.userId,
                                                legacyUserEntity.name,
                                                legacyOrderSnapshotProductGroupEntity
                                                        .productGroupId,
                                                legacyOrderSnapshotProductGroupEntity
                                                        .productGroupName,
                                                legacyOrderSnapshotProductGroupImageEntity.imageUrl,
                                                legacyBrandEntity.id,
                                                legacyBrandEntity.brandName,
                                                legacyOrderEntity.id,
                                                legacyOrderEntity.paymentId,
                                                legacyOrderEntity.orderAmount,
                                                legacyOrderEntity.quantity,
                                                legacyQnaEntity.insertDate,
                                                legacyQnaEntity.updateDate,
                                                GroupBy.set(
                                                        Projections.constructor(
                                                                LegacyWebQnaAnswerQueryDto.class,
                                                                legacyQnaAnswerEntity.id,
                                                                legacyQnaAnswerEntity.qnaParentId,
                                                                legacyQnaAnswerEntity.qnaWriterType,
                                                                legacyQnaAnswerEntity
                                                                        .qnaContents
                                                                        .title,
                                                                legacyQnaAnswerEntity
                                                                        .qnaContents
                                                                        .content,
                                                                legacyQnaAnswerEntity.insertDate,
                                                                legacyQnaAnswerEntity
                                                                        .updateDate)))));
    }

    /**
     * 주문 옵션 값 조회 (orderId 기반).
     *
     * <p>order_snapshot_product_option → order_snapshot_option_detail 경로로 옵션 값을 조회합니다.
     *
     * @param orderIds 주문 ID 목록
     * @return orderId → 옵션 값 공백 JOIN 문자열 Map
     */
    public Map<Long, String> fetchOptionsByOrderIds(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return Map.of();
        }

        record OrderOptionRow(long orderId, String optionValue) {}

        List<OrderOptionRow> rows =
                queryFactory
                        .select(
                                Projections.constructor(
                                        OrderOptionRow.class,
                                        legacyOrderSnapshotProductOptionEntity.orderId,
                                        legacyOrderSnapshotOptionDetailEntity.optionValue))
                        .distinct()
                        .from(legacyOrderSnapshotProductOptionEntity)
                        .innerJoin(legacyOrderSnapshotOptionDetailEntity)
                        .on(
                                legacyOrderSnapshotOptionDetailEntity.orderId.eq(
                                        legacyOrderSnapshotProductOptionEntity.orderId),
                                legacyOrderSnapshotOptionDetailEntity.optionDetailId.eq(
                                        legacyOrderSnapshotProductOptionEntity.optionDetailId))
                        .where(legacyOrderSnapshotProductOptionEntity.orderId.in(orderIds))
                        .fetch();

        return rows.stream()
                .collect(
                        Collectors.groupingBy(
                                OrderOptionRow::orderId,
                                Collectors.mapping(
                                        OrderOptionRow::optionValue, Collectors.joining(" "))));
    }
}
