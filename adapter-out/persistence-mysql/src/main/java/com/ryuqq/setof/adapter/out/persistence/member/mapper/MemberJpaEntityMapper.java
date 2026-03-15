package com.ryuqq.setof.adapter.out.persistence.member.mapper;

import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.common.vo.Email;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.DateOfBirth;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import org.springframework.stereotype.Component;

/**
 * MemberJpaEntityMapper - 회원 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain Member 도메인 객체
     * @return MemberJpaEntity
     */
    public MemberJpaEntity toEntity(Member domain) {
        return MemberJpaEntity.create(
                domain.idValue(),
                domain.memberNameValue(),
                domain.email() != null ? domain.email().value() : null,
                domain.phoneNumberValue(),
                domain.dateOfBirth() != null ? domain.dateOfBirth().value() : null,
                domain.gender() != null ? domain.gender().name() : null,
                domain.status().name(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity MemberJpaEntity
     * @return Member 도메인 객체
     */
    public Member toDomain(MemberJpaEntity entity) {
        MemberId memberId = MemberId.of(entity.getId());

        MemberName memberName = MemberName.of(entity.getName());

        Email email =
                entity.getEmail() != null && !entity.getEmail().isBlank()
                        ? Email.of(entity.getEmail())
                        : null;

        PhoneNumber phoneNumber = PhoneNumber.of(entity.getPhoneNumber());

        DateOfBirth dateOfBirth =
                entity.getDateOfBirth() != null ? DateOfBirth.of(entity.getDateOfBirth()) : null;

        Gender gender =
                entity.getGender() != null && !entity.getGender().isBlank()
                        ? Gender.valueOf(entity.getGender())
                        : null;

        MemberStatus status = MemberStatus.valueOf(entity.getStatus());

        DeletionStatus deletionStatus =
                entity.getDeletedAt() != null
                        ? DeletionStatus.deletedAt(entity.getDeletedAt())
                        : DeletionStatus.active();

        return Member.reconstitute(
                memberId,
                memberName,
                email,
                phoneNumber,
                dateOfBirth,
                gender,
                status,
                deletionStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
