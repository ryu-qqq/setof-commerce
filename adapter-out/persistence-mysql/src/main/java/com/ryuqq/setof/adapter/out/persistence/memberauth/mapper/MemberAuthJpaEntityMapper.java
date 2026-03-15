package com.ryuqq.setof.adapter.out.persistence.memberauth.mapper;

import com.ryuqq.setof.adapter.out.persistence.memberauth.entity.MemberAuthJpaEntity;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.id.MemberAuthId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import org.springframework.stereotype.Component;

/**
 * MemberAuthJpaEntityMapper - 회원 인증 수단 Entity-Domain 매퍼.
 *
 * <p>Entity ↔ Domain 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MemberAuthJpaEntityMapper {

    /**
     * Domain → Entity 변환.
     *
     * @param domain MemberAuth 도메인 객체
     * @return MemberAuthJpaEntity
     */
    public MemberAuthJpaEntity toEntity(MemberAuth domain) {
        return MemberAuthJpaEntity.create(
                domain.idValue(),
                domain.memberIdValue(),
                domain.authProvider().name(),
                domain.providerUserIdValue(),
                domain.passwordHashValue(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.deletionStatus().deletedAt());
    }

    /**
     * Entity → Domain 변환.
     *
     * @param entity MemberAuthJpaEntity
     * @return MemberAuth 도메인 객체
     */
    public MemberAuth toDomain(MemberAuthJpaEntity entity) {
        DeletionStatus deletionStatus =
                DeletionStatus.reconstitute(entity.isDeleted(), entity.getDeletedAt());

        PasswordHash passwordHash =
                entity.getPasswordHash() != null ? PasswordHash.of(entity.getPasswordHash()) : null;

        return MemberAuth.reconstitute(
                MemberAuthId.of(entity.getId()),
                MemberId.of(entity.getMemberId()),
                AuthProvider.valueOf(entity.getAuthProvider()),
                ProviderUserId.of(entity.getProviderUserId()),
                passwordHash,
                deletionStatus,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
