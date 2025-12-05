package com.ryuqq.setof.application.member.facade;

import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.manager.MemberPersistenceManager;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import org.springframework.stereotype.Component;

/**
 * 회원 가입 Facade
 *
 * <p>Member 저장 + Token 발급 + 이벤트 발행을 조율
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RegisterMemberFacade {

    private final MemberPersistenceManager memberPersistenceManager;
    private final TokenManager tokenManager;
    private final MemberEventDispatcher memberEventDispatcher;

    public RegisterMemberFacade(
            MemberPersistenceManager memberPersistenceManager,
            TokenManager tokenManager,
            MemberEventDispatcher memberEventDispatcher) {
        this.memberPersistenceManager = memberPersistenceManager;
        this.tokenManager = tokenManager;
        this.memberEventDispatcher = memberEventDispatcher;
    }

    /**
     * 회원 등록 처리
     *
     * <p>순서:
     *
     * <ol>
     *   <li>Member 저장 (트랜잭션) - UUID v7 ID 이미 포함
     *   <li>Token 발급 (외부 시스템)
     *   <li>도메인 이벤트 발행 및 클리어
     * </ol>
     *
     * @param member 저장할 Member (UUID v7 ID 포함)
     * @return 회원 가입 응답
     */
    public RegisterMemberResponse register(Member member) {
        MemberId memberId = member.getId();

        memberPersistenceManager.persist(member);

        TokenPairResponse tokens = tokenManager.issueTokens(memberId.getValue());

        memberEventDispatcher.publishAndClear(member);

        return new RegisterMemberResponse(memberId.getValue(), tokens);
    }
}
