package com.ryuqq.setof.application.member.facade.command;

import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.auth.port.in.command.IssueTokensUseCase;
import com.ryuqq.setof.application.member.dto.response.RegisterMemberResponse;
import com.ryuqq.setof.application.member.event.MemberEventDispatcher;
import com.ryuqq.setof.application.member.manager.command.MemberPersistenceManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.MemberId;
import org.springframework.stereotype.Component;

/**
 * 회원 가입 Facade
 *
 * <p>Member 저장 + Token 발급 + 이벤트 발행을 조율
 *
 * <p>여러 Manager를 조합하는 Command Facade
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class RegisterMemberFacade {

    private final MemberPersistenceManager memberPersistenceManager;
    private final IssueTokensUseCase issueTokensUseCase;
    private final MemberEventDispatcher memberEventDispatcher;

    public RegisterMemberFacade(
            MemberPersistenceManager memberPersistenceManager,
            IssueTokensUseCase issueTokensUseCase,
            MemberEventDispatcher memberEventDispatcher) {
        this.memberPersistenceManager = memberPersistenceManager;
        this.issueTokensUseCase = issueTokensUseCase;
        this.memberEventDispatcher = memberEventDispatcher;
    }

    /**
     * 회원 등록 처리 (persist 패턴)
     *
     * <p>순서:
     *
     * <ol>
     *   <li>Member 저장 (트랜잭션) - UUID v7 ID 이미 포함
     *   <li>Token 발급 (외부 시스템)
     *   <li>도메인 이벤트 발행 (pullDomainEvents 패턴)
     * </ol>
     *
     * @param member 저장할 Member (UUID v7 ID 포함)
     * @return 회원 가입 응답
     */
    public RegisterMemberResponse persistMember(Member member) {
        MemberId memberId = member.getId();

        memberPersistenceManager.persist(member);

        TokenPairResponse tokens = issueTokensUseCase.execute(memberId.value().toString());

        memberEventDispatcher.publish(member);

        return new RegisterMemberResponse(memberId.value().toString(), tokens);
    }
}
