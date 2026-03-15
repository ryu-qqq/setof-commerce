package com.ryuqq.setof.adapter.out.persistence.memberconsent.mapper;

import com.ryuqq.setof.adapter.out.persistence.memberconsent.entity.MemberConsentJpaEntity;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import com.ryuqq.setof.domain.member.id.MemberId;
import org.springframework.stereotype.Component;

/**
 * MemberConsentJpaEntityMapper - 회원 동의 정보 Entity-Domain 매퍼.
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
public class MemberConsentJpaEntityMapper {

    public MemberConsentJpaEntity toEntity(MemberConsent domain) {
        return MemberConsentJpaEntity.create(
                domain.id(),
                domain.memberIdValue(),
                domain.privacyConsent(),
                domain.serviceTermsConsent(),
                domain.adConsent(),
                domain.createdAt(),
                domain.updatedAt());
    }

    public MemberConsent toDomain(MemberConsentJpaEntity entity) {
        return MemberConsent.reconstitute(
                entity.getId(),
                MemberId.of(entity.getMemberId()),
                entity.isPrivacyConsent(),
                entity.isServiceTermsConsent(),
                entity.isAdConsent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
