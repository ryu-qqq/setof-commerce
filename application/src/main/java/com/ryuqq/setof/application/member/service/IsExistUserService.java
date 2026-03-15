package com.ryuqq.setof.application.member.service;

import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MemberLoginInfo;
import com.ryuqq.setof.application.member.manager.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.IsExistUserUseCase;
import com.ryuqq.setof.application.mileage.manager.MileageCompositeReadManager;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class IsExistUserService implements IsExistUserUseCase {

    private final MemberReadManager memberReadManager;
    private final MileageCompositeReadManager mileageReadManager;

    public IsExistUserService(
            MemberReadManager memberReadManager, MileageCompositeReadManager mileageReadManager) {
        this.memberReadManager = memberReadManager;
        this.mileageReadManager = mileageReadManager;
    }

    @Override
    public IsExistUserResult execute(String phoneNumber) {
        Optional<Member> memberOpt = memberReadManager.findByPhoneNumber(phoneNumber);

        if (memberOpt.isEmpty()) {
            return IsExistUserResult.notJoined();
        }

        Member member = memberOpt.get();
        MemberLoginInfo loginInfo = memberReadManager.getLoginInfoById(member.idValue());
        MileageSummary mileage = mileageReadManager.getMileageSummary(member.idValue());

        return IsExistUserResult.joined(
                member.idValue(),
                member.memberNameValue(),
                loginInfo.socialLoginType(),
                member.phoneNumberValue(),
                isSocialLogin(loginInfo.socialLoginType()) ? loginInfo.socialPkId() : "",
                mileage.currentMileage(),
                member.createdAt() != null
                        ? LocalDateTime.ofInstant(member.createdAt(), ZoneId.of("Asia/Seoul"))
                                .toString()
                        : null,
                member.isDeleted() ? "Y" : "N");
    }

    private boolean isSocialLogin(String socialLoginType) {
        return socialLoginType != null
                && !socialLoginType.isBlank()
                && !"none".equalsIgnoreCase(socialLoginType)
                && !"EMAIL".equalsIgnoreCase(socialLoginType);
    }
}
