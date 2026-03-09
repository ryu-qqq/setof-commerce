package com.ryuqq.setof.application.member.port.out.query;

import com.ryuqq.setof.application.member.dto.query.MemberProfile;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;

/**
 * 회원 조회 Port.
 *
 * <p>레거시 DB에서 회원 정보를 조회하는 아웃바운드 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
public interface MemberQueryPort {

    /**
     * 레거시 사용자 ID로 회원 조회.
     *
     * @param userId 레거시 user_id
     * @return 회원 도메인 객체
     */
    Optional<Member> findByLegacyId(Long userId);

    /**
     * 전화번호로 회원 조회.
     *
     * @param phoneNumber 전화번호
     * @return 회원 도메인 객체
     */
    Optional<Member> findByPhoneNumber(String phoneNumber);

    /**
     * 전화번호로 가입 여부 확인.
     *
     * @param phoneNumber 전화번호
     * @return 가입 여부
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * 전화번호로 회원 + 인증 정보 조회 (로그인용).
     *
     * <p>레거시 users 테이블에서 passwordHash, socialLoginType 등 인증에 필요한 정보를 함께 반환합니다.
     *
     * @param phoneNumber 전화번호
     * @return 회원 + 인증 정보
     */
    Optional<MemberWithCredentials> findWithCredentialsByPhoneNumber(String phoneNumber);

    /**
     * 레거시 사용자 ID로 회원 프로필 조회 (등급 + 마일리지 포함).
     *
     * <p>users + user_grade + user_mileage JOIN 쿼리 결과를 반환합니다.
     *
     * @param userId 레거시 user_id
     * @return 회원 프로필 (등급, 마일리지 포함)
     */
    Optional<MemberProfile> findProfileByLegacyId(Long userId);
}
