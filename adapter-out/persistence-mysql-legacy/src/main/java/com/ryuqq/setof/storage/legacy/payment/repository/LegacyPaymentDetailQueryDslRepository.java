package com.ryuqq.setof.storage.legacy.payment.repository;

import static com.ryuqq.setof.storage.legacy.order.entity.QLegacyOrderEntity.legacyOrderEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentBillEntity.legacyPaymentBillEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentEntity.legacyPaymentEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyPaymentSnapshotShippingAddressEntity.legacyPaymentSnapshotShippingAddressEntity;
import static com.ryuqq.setof.storage.legacy.payment.entity.QLegacyVBankAccountEntity.legacyVBankAccountEntity;
import static com.ryuqq.setof.storage.legacy.paymentmethod.entity.QLegacyPaymentMethodEntity.legacyPaymentMethodEntity;
import static com.ryuqq.setof.storage.legacy.refundaccount.entity.QLegacyRefundAccountEntity.legacyRefundAccountEntity;
import static com.ryuqq.setof.storage.legacy.user.entity.QLegacyUserEntity.legacyUserEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.storage.legacy.payment.dto.LegacyPaymentDetailFlatDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * LegacyPaymentDetailQueryDslRepository - 결제 단건 상세 조회 Repository.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>Projections.constructor() 사용 (@QueryProjection 금지).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Repository
public class LegacyPaymentDetailQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public LegacyPaymentDetailQueryDslRepository(
            @Qualifier("legacyJpaQueryFactory") JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 결제 ID와 사용자 ID로 결제 상세 조회 (1행).
     *
     * <p>payment INNER JOIN payment_bill LEFT JOIN payment_method LEFT JOIN vbank_account INNER
     * JOIN users LEFT JOIN payment_snapshot_shipping_address LEFT JOIN refund_account(userId 기준)
     * 구조로 조회합니다.
     *
     * @param paymentId 결제 ID
     * @param userId 사용자 ID (본인 확인용)
     * @return 결제 상세 flat DTO (없으면 null)
     */
    public LegacyPaymentDetailFlatDto fetchPaymentDetail(long paymentId, long userId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                LegacyPaymentDetailFlatDto.class,
                                // payment
                                legacyPaymentEntity.id,
                                legacyPaymentEntity.paymentStatus.stringValue(),
                                // payment_method
                                legacyPaymentMethodEntity.paymentMethod.coalesce(""),
                                // payment
                                legacyPaymentEntity.paymentDate,
                                legacyPaymentEntity.canceledDate,
                                // payment_bill
                                legacyPaymentBillEntity.paymentAmount,
                                legacyPaymentBillEntity.usedMileageAmount,
                                legacyPaymentBillEntity.paymentAgencyId.coalesce(""),
                                legacyPaymentBillEntity.cardName.coalesce(""),
                                legacyPaymentBillEntity.cardNumber.coalesce(""),
                                // vbank_account (LEFT JOIN)
                                legacyVBankAccountEntity.vBankName.coalesce(""),
                                legacyVBankAccountEntity.vBankNumber.coalesce(""),
                                legacyVBankAccountEntity.paymentAmount.coalesce(0L),
                                legacyVBankAccountEntity.vBankDueDate,
                                // payment_bill - 구매자 정보
                                legacyPaymentBillEntity.buyerName.coalesce(""),
                                legacyPaymentBillEntity.buyerEmail.coalesce(""),
                                legacyPaymentBillEntity.buyerPhoneNumber.coalesce(""),
                                // payment_snapshot_shipping_address - 수령인 정보
                                legacyPaymentSnapshotShippingAddressEntity.receiverName.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.phoneNumber.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.addressLine1.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.addressLine2.coalesce(
                                        ""),
                                legacyPaymentSnapshotShippingAddressEntity.zipCode.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.country.coalesce(""),
                                legacyPaymentSnapshotShippingAddressEntity.deliveryRequest.coalesce(
                                        ""),
                                // users - 주문자 전화번호
                                legacyUserEntity.phoneNumber.coalesce(""),
                                // refund_account - 환불 계좌 정보
                                legacyRefundAccountEntity.bankName.coalesce(""),
                                legacyRefundAccountEntity.accountNumber.coalesce(""),
                                legacyRefundAccountEntity.id.coalesce(0L),
                                legacyRefundAccountEntity.accountHolderName.coalesce("")))
                .from(legacyPaymentEntity)
                .innerJoin(legacyPaymentBillEntity)
                .on(legacyPaymentBillEntity.paymentId.eq(legacyPaymentEntity.id))
                .leftJoin(legacyPaymentMethodEntity)
                .on(legacyPaymentMethodEntity.id.eq(legacyPaymentBillEntity.paymentMethodId))
                .leftJoin(legacyVBankAccountEntity)
                .on(legacyVBankAccountEntity.paymentId.eq(legacyPaymentEntity.id))
                .innerJoin(legacyUserEntity)
                .on(legacyUserEntity.id.eq(legacyPaymentEntity.userId))
                .leftJoin(legacyPaymentSnapshotShippingAddressEntity)
                .on(legacyPaymentSnapshotShippingAddressEntity.paymentId.eq(legacyPaymentEntity.id))
                .leftJoin(legacyRefundAccountEntity)
                .on(legacyRefundAccountEntity.userId.eq(legacyPaymentEntity.userId))
                .where(legacyPaymentEntity.id.eq(paymentId), legacyPaymentEntity.userId.eq(userId))
                .fetchOne();
    }

    /**
     * 결제 ID로 해당 결제에 포함된 주문 ID 목록 조회.
     *
     * @param paymentId 결제 ID
     * @return 주문 ID 목록
     */
    public List<Long> fetchOrderIdsByPaymentId(long paymentId) {
        return queryFactory
                .select(legacyOrderEntity.id)
                .from(legacyOrderEntity)
                .where(legacyOrderEntity.paymentId.eq(paymentId))
                .orderBy(legacyOrderEntity.id.asc())
                .fetch();
    }
}
