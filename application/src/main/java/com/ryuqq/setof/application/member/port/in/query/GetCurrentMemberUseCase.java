package com.ryuqq.setof.application.member.port.in.query;

import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;

/**
 * Get Current Member UseCase (Query)
 *
 * <p>현재 로그인한 회원 정보 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetCurrentMemberUseCase {

    /**
     * 현재 회원 정보 조회 실행
     *
     * @param query 조회 쿼리
     * @return 회원 상세 정보
     */
    MemberDetailResponse execute(GetCurrentMemberQuery query);
}
