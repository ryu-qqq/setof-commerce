package com.ryuqq.setof.application.member.service.query;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.query.GetCurrentMemberUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import org.springframework.stereotype.Service;

/**
 * 현재 회원 정보 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReadManager로 회원 조회
 *   <li>MemberAssembler로 MemberDetailResponse 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetCurrentMemberService implements GetCurrentMemberUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberAssembler memberAssembler;

    public GetCurrentMemberService(
            MemberReadManager memberReadManager, MemberAssembler memberAssembler) {
        this.memberReadManager = memberReadManager;
        this.memberAssembler = memberAssembler;
    }

    @Override
    public MemberDetailResponse execute(GetCurrentMemberQuery query) {
        Member member = memberReadManager.findById(query.memberId());
        return memberAssembler.toMemberDetailResponse(member);
    }
}
