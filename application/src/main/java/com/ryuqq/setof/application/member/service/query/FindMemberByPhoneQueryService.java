package com.ryuqq.setof.application.member.service.query;

import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.query.FindMemberByPhoneQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.query.FindMemberByPhoneUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 핸드폰 번호로 회원 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>MemberReadManager로 핸드폰 번호 기반 회원 조회
 *   <li>MemberAssembler로 MemberDetailResponse 변환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class FindMemberByPhoneQueryService implements FindMemberByPhoneUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberAssembler memberAssembler;

    public FindMemberByPhoneQueryService(
            MemberReadManager memberReadManager, MemberAssembler memberAssembler) {
        this.memberReadManager = memberReadManager;
        this.memberAssembler = memberAssembler;
    }

    @Override
    public Optional<MemberDetailResponse> execute(FindMemberByPhoneQuery query) {
        Optional<Member> memberOptional =
                memberReadManager.findByPhoneNumberOptional(query.phoneNumber());
        return memberOptional.map(memberAssembler::toMemberDetailResponse);
    }
}
