package com.ryuqq.setof.adapter.in.rest.v2.member.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.KakaoLinkApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.RegisterMemberApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.ResetPasswordApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.member.dto.command.WithdrawApiRequest;
import com.ryuqq.setof.application.member.dto.bundle.ConsentItem;
import com.ryuqq.setof.application.member.dto.command.IntegrateKakaoCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Member API Mapper
 *
 * <p>회원 관련 API DTO ↔ Application Command/Query 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class MemberApiMapper {

    /**
     * 회원가입 요청 → 회원가입 커맨드 변환
     *
     * @param request API 요청
     * @return RegisterMemberCommand
     */
    public RegisterMemberCommand toRegisterCommand(RegisterMemberApiRequest request) {
        List<ConsentItem> consents = new ArrayList<>();
        consents.add(new ConsentItem("PRIVACY", request.privacyConsent()));
        consents.add(new ConsentItem("SERVICE_TERMS", request.serviceTermsConsent()));
        consents.add(new ConsentItem("MARKETING", request.adConsent()));

        return new RegisterMemberCommand(
                request.phoneNumber(),
                request.email(),
                request.password(),
                request.name(),
                request.dateOfBirth(),
                request.gender(),
                consents);
    }

    /**
     * 비밀번호 재설정 요청 → 커맨드 변환
     *
     * @param request API 요청
     * @return ResetPasswordCommand
     */
    public ResetPasswordCommand toResetPasswordCommand(ResetPasswordApiRequest request) {
        return new ResetPasswordCommand(request.phoneNumber(), request.newPassword());
    }

    /**
     * 회원 탈퇴 요청 → 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param request API 요청
     * @return WithdrawMemberCommand
     */
    public WithdrawMemberCommand toWithdrawCommand(String memberId, WithdrawApiRequest request) {
        return new WithdrawMemberCommand(memberId, request.reason());
    }

    /**
     * 카카오 연동 요청 → 커맨드 변환
     *
     * @param memberId 회원 ID
     * @param request API 요청
     * @return IntegrateKakaoCommand
     */
    public IntegrateKakaoCommand toIntegrateKakaoCommand(
            String memberId, KakaoLinkApiRequest request) {
        return IntegrateKakaoCommand.withoutProfile(memberId, request.kakaoId());
    }

    /**
     * 내 정보 조회 쿼리 생성
     *
     * @param memberId 회원 ID
     * @return GetCurrentMemberQuery
     */
    public GetCurrentMemberQuery toGetCurrentMemberQuery(String memberId) {
        return new GetCurrentMemberQuery(memberId);
    }
}
