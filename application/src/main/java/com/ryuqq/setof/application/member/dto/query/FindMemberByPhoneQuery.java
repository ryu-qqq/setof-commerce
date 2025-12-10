package com.ryuqq.setof.application.member.dto.query;

/**
 * Find Member By Phone Query
 *
 * <p>핸드폰 번호로 회원 조회 쿼리
 *
 * @author development-team
 * @since 1.0.0
 */
public record FindMemberByPhoneQuery(String phoneNumber) {
    // Immutable query object - no additional behavior
}
