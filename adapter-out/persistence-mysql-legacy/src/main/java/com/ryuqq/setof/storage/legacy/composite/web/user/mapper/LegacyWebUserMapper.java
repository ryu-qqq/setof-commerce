package com.ryuqq.setof.storage.legacy.composite.web.user.mapper;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyJoinedUserResult;
import com.ryuqq.setof.application.legacy.user.dto.response.LegacyMyPageResult;
import com.ryuqq.setof.application.legacy.user.dto.response.LegacyUserResult;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebJoinedUserQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebMyPageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebUserQueryDto;
import org.springframework.stereotype.Component;

/**
 * 레거시 사용자 Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserMapper {

    public LegacyUserResult toResult(LegacyWebUserQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyUserResult.of(
                dto.userId(),
                dto.name(),
                dto.phoneNumber(),
                dto.socialLoginType(),
                null,
                dto.gradeName(),
                dto.currentMileage(),
                dto.insertDate());
    }

    public LegacyJoinedUserResult toJoinedResult(LegacyWebJoinedUserQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyJoinedUserResult.of(
                dto.name(),
                dto.userId(),
                dto.socialLoginType(),
                dto.phoneNumber(),
                dto.socialPkId(),
                dto.currentMileage(),
                dto.insertDate(),
                dto.deleteYn());
    }

    public LegacyMyPageResult toMyPageResult(LegacyWebMyPageQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyMyPageResult.of(
                dto.name(),
                dto.phoneNumber(),
                dto.email(),
                dto.socialLoginType(),
                dto.insertDate(),
                dto.gradeName(),
                dto.currentMileage());
    }
}
