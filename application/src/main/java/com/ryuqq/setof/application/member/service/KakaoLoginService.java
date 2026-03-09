package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.auth.dto.response.KakaoLoginResult;
import com.ryuqq.setof.application.member.dto.command.KakaoLoginCommand;
import com.ryuqq.setof.application.member.dto.query.MemberWithCredentials;
import com.ryuqq.setof.application.member.internal.KakaoLoginCoordinator;
import com.ryuqq.setof.application.member.port.in.KakaoLoginUseCase;
import com.ryuqq.setof.application.member.validator.KakaoLoginValidator;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 카카오 소셜 로그인 서비스.
 *
 * <p>KakaoLoginUseCase를 구현하며, 검증 후 코디네이터에 분기 처리를 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Service
public class KakaoLoginService implements KakaoLoginUseCase {

    private final KakaoLoginValidator kakaoLoginValidator;
    private final KakaoLoginCoordinator kakaoLoginCoordinator;

    public KakaoLoginService(
            KakaoLoginValidator kakaoLoginValidator, KakaoLoginCoordinator kakaoLoginCoordinator) {
        this.kakaoLoginValidator = kakaoLoginValidator;
        this.kakaoLoginCoordinator = kakaoLoginCoordinator;
    }

    @Override
    public KakaoLoginResult execute(KakaoLoginCommand command) {
        Optional<MemberWithCredentials> existingOpt =
                kakaoLoginValidator.validateAndFindExisting(command.phoneNumber());

        return kakaoLoginCoordinator.coordinate(existingOpt, command);
    }
}
