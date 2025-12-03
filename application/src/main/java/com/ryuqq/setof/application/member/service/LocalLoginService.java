package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.component.MemberPolicyValidator;
import com.ryuqq.setof.application.member.component.MemberReader;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.response.LocalLoginResponse;
import com.ryuqq.setof.application.member.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.manager.TokenManager;
import com.ryuqq.setof.application.member.port.in.command.LocalLoginUseCase;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 로컬 로그인 서비스
 *
 * <p>핸드폰 번호 + 비밀번호 로그인 처리
 *
 * <p>흐름:
 *
 * <ol>
 *   <li>MemberReader로 회원 조회
 *   <li>MemberPolicyValidator로 로그인 가능 여부 검증
 *   <li>TokenManager로 JWT 발급 + Refresh Token 저장
 *   <li>MemberAssembler로 LocalLoginResponse 생성
 * </ol>
 *
 * <p>주의: JWT 발급/Redis 저장은 트랜잭션 외부에서 수행됨
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class LocalLoginService implements LocalLoginUseCase {

    private final MemberReader memberReader;
    private final MemberPolicyValidator memberPolicyValidator;
    private final TokenManager tokenManager;
    private final MemberAssembler memberAssembler;

    public LocalLoginService(
            MemberReader memberReader,
            MemberPolicyValidator memberPolicyValidator,
            TokenManager tokenManager,
            MemberAssembler memberAssembler) {
        this.memberReader = memberReader;
        this.memberPolicyValidator = memberPolicyValidator;
        this.tokenManager = tokenManager;
        this.memberAssembler = memberAssembler;
    }

    @Override
    public LocalLoginResponse execute(LocalLoginCommand command) {
        Member member = memberReader.getByPhoneNumber(command.phoneNumber());

        memberPolicyValidator.validateCanLogin(member, command.rawPassword());

        TokenPairResponse tokens = tokenManager.issueTokens(member.getIdValue());

        return memberAssembler.toLocalLoginResponse(member.getIdValue(), tokens);
    }
}
