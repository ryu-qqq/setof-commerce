package com.ryuqq.setof.application.member.port.in.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.member.dto.query.GetMembersQuery;
import com.ryuqq.setof.application.member.dto.response.MemberSummaryResponse;

/**
 * 회원 목록 조회 UseCase (Port-In)
 *
 * <p>회원 목록을 페이징하여 조회합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetMembersUseCase {

    /**
     * 회원 목록 조회
     *
     * @param query 검색 조건 (이름, 핸드폰 번호, 상태, 페이징)
     * @return 회원 목록 (PageResponse)
     */
    PageResponse<MemberSummaryResponse> execute(GetMembersQuery query);
}
