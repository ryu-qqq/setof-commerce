package com.ryuqq.setof.application.member.validator;

import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberAlreadyRegisteredException;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Component;

/**
 * MemberValidator - 회원 검증기.
 *
 * <p>회원 존재 여부 확인, 회원 조회 등 공통 검증 로직을 제공합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class MemberValidator {

    private final MemberReadManager memberReadManager;

    public MemberValidator(MemberReadManager memberReadManager) {
        this.memberReadManager = memberReadManager;
    }

    /**
     * 전화번호로 중복 가입 여부를 검증합니다.
     *
     * @param phoneNumber 전화번호
     * @throws MemberAlreadyRegisteredException 이미 가입된 경우
     */
    public void validateNotRegistered(String phoneNumber) {
        if (memberReadManager.existsByPhoneNumber(phoneNumber)) {
            throw new MemberAlreadyRegisteredException();
        }
    }

    /**
     * 전화번호로 회원을 조회합니다.
     *
     * @param phoneNumber 전화번호
     * @return 회원 도메인 객체
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우
     */
    public Member getByPhoneNumber(String phoneNumber) {
        return memberReadManager
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberNotFoundException(phoneNumber));
    }

    /**
     * 회원 ID로 회원을 조회합니다.
     *
     * @param memberId 회원 ID
     * @return 회원 도메인 객체
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우
     */
    public Member getById(long memberId) {
        return memberReadManager.getById(memberId);
    }
}
