package com.ryuqq.setof.adapter.out.persistence.bank.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.bank.entity.BankJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.bank.entity.QBankJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * BankQueryDslRepository - Bank QueryDSL Repository
 *
 * <p>QueryDSL 기반 조회 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>표준 메서드:</strong>
 *
 * <ul>
 *   <li>findById(Long id): ID로 단건 조회
 *   <li>findByBankCode(String bankCode): 은행 코드로 조회
 *   <li>findAllActive(): 활성 은행 목록 조회 (displayOrder 순)
 *   <li>existsById(Long id): 존재 여부 확인
 *   <li>existsActiveById(Long id): 활성 은행 존재 여부 확인
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>Join 절대 금지
 *   <li>비즈니스 로직 금지
 *   <li>Mapper 호출 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class BankQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QBankJpaEntity qBank = QBankJpaEntity.bankJpaEntity;

    public BankQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * ID로 Bank 단건 조회
     *
     * @param id Bank ID
     * @return BankJpaEntity (Optional)
     */
    public Optional<BankJpaEntity> findById(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qBank).where(qBank.id.eq(id)).fetchOne());
    }

    /**
     * 은행 코드로 Bank 단건 조회
     *
     * @param bankCode 은행 코드
     * @return BankJpaEntity (Optional)
     */
    public Optional<BankJpaEntity> findByBankCode(String bankCode) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qBank).where(qBank.bankCode.eq(bankCode)).fetchOne());
    }

    /**
     * 은행 이름으로 Bank 단건 조회
     *
     * <p>V1 레거시 API 지원을 위한 메서드입니다.
     *
     * @param bankName 은행 이름
     * @return BankJpaEntity (Optional)
     */
    public Optional<BankJpaEntity> findByBankName(String bankName) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qBank).where(qBank.bankName.eq(bankName)).fetchOne());
    }

    /**
     * 활성화된 모든 Bank 목록 조회
     *
     * @return BankJpaEntity 목록 (displayOrder 순)
     */
    public List<BankJpaEntity> findAllActive() {
        return queryFactory
                .selectFrom(qBank)
                .where(qBank.isActive.isTrue())
                .orderBy(qBank.displayOrder.asc())
                .fetch();
    }

    /**
     * ID로 Bank 존재 여부 확인
     *
     * @param id Bank ID
     * @return 존재 여부
     */
    public boolean existsById(Long id) {
        Integer count = queryFactory.selectOne().from(qBank).where(qBank.id.eq(id)).fetchFirst();
        return count != null;
    }

    /**
     * 활성 Bank 존재 여부 확인
     *
     * @param id Bank ID
     * @return 활성 존재 여부
     */
    public boolean existsActiveById(Long id) {
        Integer count =
                queryFactory
                        .selectOne()
                        .from(qBank)
                        .where(qBank.id.eq(id), qBank.isActive.isTrue())
                        .fetchFirst();
        return count != null;
    }
}
