package com.ryuqq.setof.adapter.in.rest.v1.user.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.JoinV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.LoginV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.PasswordV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.command.WithdrawV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.query.SearchUserV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.response.LoginV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.user.dto.response.UserV1ApiResponse;
import com.ryuqq.setof.application.auth.dto.response.TokenPairResponse;
import com.ryuqq.setof.application.member.dto.bundle.ConsentItem;
import com.ryuqq.setof.application.member.dto.command.LocalLoginCommand;
import com.ryuqq.setof.application.member.dto.command.LogoutMemberCommand;
import com.ryuqq.setof.application.member.dto.command.RegisterMemberCommand;
import com.ryuqq.setof.application.member.dto.command.ResetPasswordCommand;
import com.ryuqq.setof.application.member.dto.command.WithdrawMemberCommand;
import com.ryuqq.setof.application.member.dto.query.FindMemberByPhoneQuery;
import com.ryuqq.setof.application.member.dto.query.GetCurrentMemberQuery;
import com.ryuqq.setof.application.member.dto.response.MemberDetailResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * V1 User API Mapper
 *
 * <p>V1 DTO를 V2 Command/Query로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class UserV1ApiMapper {

    // ===== V1 Request → V2 Command =====

    public LocalLoginCommand toLocalLoginCommand(LoginV1ApiRequest request) {
        return new LocalLoginCommand(request.phoneNumber(), request.passwordHash());
    }

    public RegisterMemberCommand toRegisterMemberCommand(JoinV1ApiRequest request) {
        List<ConsentItem> consents =
                convertConsents(
                        request.privacyConsent(),
                        request.serviceTermsConsent(),
                        request.adConsent());

        return new RegisterMemberCommand(
                request.phoneNumber(),
                null, // email - V1에 없음
                request.passwordHash(),
                request.name(),
                null, // dateOfBirth - V1에 없음
                null, // gender - V1에 없음
                consents);
    }

    public GetCurrentMemberQuery toGetCurrentMemberQuery(String memberId) {
        return new GetCurrentMemberQuery(memberId);
    }

    public FindMemberByPhoneQuery toFindMemberByPhoneQuery(
            SearchUserV1ApiRequest searchUserV1ApiRequest) {
        return new FindMemberByPhoneQuery(searchUserV1ApiRequest.phoneNumber());
    }

    public WithdrawMemberCommand toWithdrawMemberCommand(
            String memberId, WithdrawV1ApiRequest request) {
        return new WithdrawMemberCommand(memberId, request.reason());
    }

    public ResetPasswordCommand toResetPasswordCommand(
            String phoneNumber, PasswordV1ApiRequest request) {
        return new ResetPasswordCommand(phoneNumber, request.passwordHash());
    }

    public LogoutMemberCommand toLogoutMemberCommand(String memberId) {
        return new LogoutMemberCommand(memberId);
    }

    // ===== V2 Response → V1 Response =====

    public LoginV1ApiResponse toLoginResponse(TokenPairResponse tokens) {
        return new LoginV1ApiResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                "Bearer",
                tokens.accessTokenExpiresIn());
    }

    public UserV1ApiResponse toUserResponse(MemberDetailResponse detail) {
        UserV1ApiResponse.UserDetailV1Response userDetail =
                new UserV1ApiResponse.UserDetailV1Response(
                        null, // userId - memberId가 UUID라 Long 변환 불가
                        detail.phoneNumber(),
                        detail.name(),
                        detail.email(),
                        "N", // adConsent - V2에서 조회 불가, 기본값
                        0L // totalMileage - V2에서 조회 불가, 기본값
                        );
        return new UserV1ApiResponse(true, userDetail);
    }

    // ===== Helper Methods =====

    private List<ConsentItem> convertConsents(String privacy, String service, String ad) {
        List<ConsentItem> consents = new ArrayList<>();
        consents.add(new ConsentItem("PRIVACY", "Y".equals(privacy)));
        consents.add(new ConsentItem("SERVICE_TERMS", "Y".equals(service)));
        consents.add(new ConsentItem("AD", "Y".equals(ad)));
        return consents;
    }
}
