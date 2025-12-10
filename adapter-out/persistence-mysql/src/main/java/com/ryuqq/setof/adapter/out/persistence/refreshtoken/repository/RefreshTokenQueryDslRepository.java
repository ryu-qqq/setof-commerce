package com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity.QRefreshTokenJpaEntity;
import org.springframework.stereotype.Repository;

/**
 * RefreshTokenQueryDslRepository - RefreshToken QueryDSL Repository
 *
 * <p>QueryDSL 기반 Refresh Token 조회/삭제 쿼리를 처리하는 전용 Repository입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>회원 ID 기준 토큰 삭제
 *   <li>토큰 값 기준 삭제
 * </ul>
 *
 * <p><strong>@Query 대체:</strong>
 *
 * <ul>
 *   <li>JPA Repository의 @Query, @Modifying 어노테이션 대신 QueryDSL 사용
 *   <li>타입 안전한 쿼리 작성
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>❌ 비즈니스 로직 금지
 *   <li>❌ Mapper 호출 금지
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Repository
public class RefreshTokenQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final QRefreshTokenJpaEntity qRefreshToken =
            QRefreshTokenJpaEntity.refreshTokenJpaEntity;

    public RefreshTokenQueryDslRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 회원 ID로 Refresh Token 삭제
     *
     * <p>로그아웃, 회원 탈퇴 시 해당 회원의 모든 토큰 삭제
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 삭제된 레코드 수
     */
    public long deleteByMemberId(String memberId) {
        return queryFactory
                .delete(qRefreshToken)
                .where(qRefreshToken.memberId.eq(memberId))
                .execute();
    }

    /**
     * 토큰 값으로 Refresh Token 삭제
     *
     * <p>특정 토큰만 무효화 시 사용
     *
     * @param token Refresh Token 값
     * @return 삭제된 레코드 수
     */
    public long deleteByToken(String token) {
        return queryFactory.delete(qRefreshToken).where(qRefreshToken.token.eq(token)).execute();
    }

    /**
     * 회원 ID로 토큰 값 조회
     *
     * <p>가장 최근 토큰 반환 (createdAt DESC)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 값 (Optional)
     */
    public java.util.Optional<String> findTokenByMemberId(String memberId) {
        String token =
                queryFactory
                        .select(qRefreshToken.token)
                        .from(qRefreshToken)
                        .where(qRefreshToken.memberId.eq(memberId))
                        .orderBy(qRefreshToken.createdAt.desc())
                        .fetchFirst();
        return java.util.Optional.ofNullable(token);
    }

    /**
     * 토큰 값으로 회원 ID 조회
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (Optional)
     */
    public java.util.Optional<String> findMemberIdByToken(String tokenValue) {
        String memberId =
                queryFactory
                        .select(qRefreshToken.memberId)
                        .from(qRefreshToken)
                        .where(qRefreshToken.token.eq(tokenValue))
                        .fetchOne();
        return java.util.Optional.ofNullable(memberId);
    }
}
