package com.ryuqq.setof.storage.legacy.user.mapper;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Email;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.DateOfBirth;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.user.dto.LegacyMemberProfileQueryDto;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

/**
 * LegacyMemberEntityMapper - 레거시 회원 Entity <-> Domain 매퍼.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
public class LegacyMemberEntityMapper {

    /**
     * LegacyUserEntity → Member 도메인 변환.
     *
     * <p>레거시 user_id가 곧 member PK(Long auto-increment)입니다.
     *
     * @param entity 레거시 사용자 엔티티
     * @return Member 도메인 객체
     */
    public Member toDomain(LegacyUserEntity entity) {
        return Member.reconstitute(
                MemberId.of(entity.getId()),
                MemberName.of(entity.getName()),
                entity.getEmail() != null ? Email.of(entity.getEmail()) : null,
                PhoneNumber.of(entity.getPhoneNumber()),
                entity.getDateOfBirth() != null ? DateOfBirth.of(entity.getDateOfBirth()) : null,
                mapGender(entity.getGender()),
                mapMemberStatus(entity),
                mapDeletionStatus(entity),
                toInstant(entity.getInsertDate()),
                toInstant(entity.getUpdateDate()));
    }

    /**
     * LegacyMemberProfileQueryDto → Member 도메인 변환.
     *
     * @param dto JOIN 쿼리 결과 DTO
     * @return Member 도메인 객체
     */
    public Member toDomainFromProfile(LegacyMemberProfileQueryDto dto) {
        return Member.reconstitute(
                MemberId.of(dto.getUserId()),
                MemberName.of(dto.getName()),
                dto.getEmail() != null ? Email.of(dto.getEmail()) : null,
                PhoneNumber.of(dto.getPhoneNumber()),
                dto.getDateOfBirth() != null ? DateOfBirth.of(dto.getDateOfBirth()) : null,
                mapGender(dto.getGender()),
                mapMemberStatus(dto.getWithdrawalYn(), dto.getDeleteYn()),
                mapDeletionStatus(dto.getDeleteYn(), toInstant(dto.getInsertDate())),
                toInstant(dto.getInsertDate()),
                toInstant(dto.getInsertDate()));
    }

    private Gender mapGender(LegacyUserEntity.Gender entityGender) {
        if (entityGender == null) {
            return Gender.OTHER;
        }
        return switch (entityGender) {
            case MALE -> Gender.MALE;
            case FEMALE -> Gender.FEMALE;
            default -> Gender.OTHER;
        };
    }

    private MemberStatus mapMemberStatus(LegacyUserEntity entity) {
        return mapMemberStatus(entity.getWithdrawalYn(), entity.getDeleteYn());
    }

    private MemberStatus mapMemberStatus(Yn withdrawalYn, Yn deleteYn) {
        if (withdrawalYn == Yn.Y) {
            return MemberStatus.WITHDRAWN;
        }
        if (deleteYn == Yn.Y) {
            return MemberStatus.INACTIVE;
        }
        return MemberStatus.ACTIVE;
    }

    private DeletionStatus mapDeletionStatus(LegacyUserEntity entity) {
        if (entity.getDeleteYn() == Yn.Y) {
            return DeletionStatus.deletedAt(toInstant(entity.getUpdateDate()));
        }
        return DeletionStatus.active();
    }

    private DeletionStatus mapDeletionStatus(Yn deleteYn, Instant fallbackInstant) {
        if (deleteYn == Yn.Y) {
            return DeletionStatus.deletedAt(fallbackInstant);
        }
        return DeletionStatus.active();
    }

    private static final ZoneId LEGACY_DB_ZONE = ZoneId.of("Asia/Seoul");

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Instant.now();
        }
        return localDateTime.atZone(LEGACY_DB_ZONE).toInstant();
    }
}
