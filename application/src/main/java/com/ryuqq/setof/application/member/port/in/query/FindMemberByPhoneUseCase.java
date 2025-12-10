package com.ryuqq.setof.application.member.port.in.query;

import com.ryuqq.setof.application.member.dto.query.FindMemberByPhoneQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import java.util.Optional;

/**
 * Find Member By Phone UseCase (Query)
 *
 * <p>핸드폰 번호로 회원 조회를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface FindMemberByPhoneUseCase {

    /**
     * 핸드폰 번호로 회원 조회
     *
     * @param query 조회 쿼리
     * @return 회원 상세 정보 (Optional - 미존재 시 empty)
     */
    Optional<MemberDetailResponse> execute(FindMemberByPhoneQuery query);
}
