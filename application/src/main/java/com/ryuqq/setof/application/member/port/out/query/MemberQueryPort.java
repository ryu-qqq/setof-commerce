package com.ryuqq.setof.application.member.port.out.query;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import java.util.Optional;

/**
 * Member Query Port (Query)
 *
 * <p>Member Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface MemberQueryPort {

    /**
     * ID로 Member 단건 조회
     *
     * @param id Member ID (Value Object)
     * @return Member Domain (Optional)
     */
    Optional<Member> findById(MemberId id);

    /**
     * 핸드폰 번호로 Member 조회
     *
     * @param phoneNumber 핸드폰 번호 (Value Object)
     * @return Member Domain (Optional)
     */
    Optional<Member> findByPhoneNumber(PhoneNumber phoneNumber);

    /**
     * 소셜 ID로 Member 조회
     *
     * @param socialId 소셜 ID (Value Object)
     * @return Member Domain (Optional)
     */
    Optional<Member> findBySocialId(SocialId socialId);

    /**
     * 핸드폰 번호로 Member 존재 여부 확인
     *
     * @param phoneNumber 핸드폰 번호 (Value Object)
     * @return 존재 여부
     */
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
}
