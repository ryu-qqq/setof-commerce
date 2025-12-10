package com.ryuqq.setof.adapter.out.persistence.refreshtoken.adapter;

import com.ryuqq.setof.adapter.out.persistence.refreshtoken.repository.RefreshTokenQueryDslRepository;
import com.ryuqq.setof.application.auth.port.out.query.RefreshTokenQueryPort;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * RefreshTokenQueryAdapter - Refresh Token Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Refresh Token 조회 요청을 QueryDslRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>회원 ID로 토큰 값 조회 (findTokenByMemberId)
 *   <li>토큰 값으로 회원 ID 조회 (findMemberIdByToken)
 *   <li>QueryDslRepository 호출
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 *   <li>JPAQueryFactory 직접 사용 금지 (QueryDslRepository에서 처리)
 * </ul>
 *
 * <p><strong>Hexagonal Architecture:</strong>
 *
 * <ul>
 *   <li>RefreshTokenQueryPort 인터페이스 구현
 *   <li>Application Layer의 Port를 Persistence Layer에서 구현
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class RefreshTokenQueryAdapter implements RefreshTokenQueryPort {

    private final RefreshTokenQueryDslRepository queryDslRepository;

    public RefreshTokenQueryAdapter(RefreshTokenQueryDslRepository queryDslRepository) {
        this.queryDslRepository = queryDslRepository;
    }

    /**
     * 회원 ID로 토큰 값 조회
     *
     * <p>가장 최근 토큰 반환 (1:N 관계에서 최신 토큰)
     *
     * @param memberId 회원 ID (UUID 문자열)
     * @return 토큰 값 (Optional)
     */
    @Override
    public Optional<String> findTokenByMemberId(String memberId) {
        return queryDslRepository.findTokenByMemberId(memberId);
    }

    /**
     * 토큰 값으로 회원 ID 조회
     *
     * @param tokenValue 토큰 값
     * @return 회원 ID (Optional)
     */
    @Override
    public Optional<String> findMemberIdByToken(String tokenValue) {
        return queryDslRepository.findMemberIdByToken(tokenValue);
    }
}
