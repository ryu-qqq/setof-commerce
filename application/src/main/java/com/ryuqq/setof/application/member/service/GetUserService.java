package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.GetUserUseCase;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Service;

@Service
public class GetUserService implements GetUserUseCase {

    private final MemberReadManager memberReadManager;
    private final MileageCompositeReadManager mileageReadManager;

    public GetUserService(
            MemberReadManager memberReadManager, MileageCompositeReadManager mileageReadManager) {
        this.memberReadManager = memberReadManager;
        this.mileageReadManager = mileageReadManager;
    }

    @Override
    public UserResult execute(long userId) {
        MemberLoginInfo loginInfo = memberReadManager.getLoginInfoById(userId);
        MileageSummary mileage = mileageReadManager.getMileageSummary(userId);
        Member member = loginInfo.member();

        String joinedDate =
                member.createdAt() != null
                        ? LocalDateTime.ofInstant(member.createdAt(), ZoneId.of("Asia/Seoul"))
                                .toString()
                        : null;

        return new UserResult(
                member.idValue(),
                member.phoneNumberValue(),
                member.memberNameValue(),
                member.emailValue(),
                mileage.currentMileage(),
                loginInfo.socialLoginType(),
                isSocialLogin(loginInfo.socialLoginType()) ? loginInfo.socialPkId() : "",
                joinedDate,
                member.isDeleted() ? "Y" : "N");
    }

    private boolean isSocialLogin(String socialLoginType) {
        return socialLoginType != null
                && !socialLoginType.isBlank()
                && !"none".equalsIgnoreCase(socialLoginType)
                && !"EMAIL".equalsIgnoreCase(socialLoginType);
    }
}
