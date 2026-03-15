package com.ryuqq.setof.storage.legacy.user.adapter;

import com.ryuqq.setof.application.member.port.out.command.MemberAuthCommandPort;
import com.ryuqq.setof.application.member.port.out.command.MemberCommandPort;
import com.ryuqq.setof.application.member.port.out.command.MemberConsentCommandPort;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.aggregate.MemberAuth;
import com.ryuqq.setof.domain.member.aggregate.MemberConsent;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import com.ryuqq.setof.storage.legacy.common.Yn;
import com.ryuqq.setof.storage.legacy.user.entity.LegacyUserEntity;
import com.ryuqq.setof.storage.legacy.user.repository.LegacyMemberJpaRepository;
import java.time.LocalDateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyMemberCommandAdapter - 레거시 회원 명령 Adapter.
 *
 * <p>단일 users 테이블에 member + auth + consent 데이터를 저장합니다. 3개 포트를 모두 구현하여, 같은 트랜잭션 내에서 ThreadLocal로 엔티티
 * 참조를 공유합니다.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Component
@ConditionalOnProperty(name = "persistence.member.enabled", havingValue = "true")
public class LegacyMemberCommandAdapter
        implements MemberCommandPort, MemberAuthCommandPort, MemberConsentCommandPort {

    private final LegacyMemberJpaRepository jpaRepository;
    private final ThreadLocal<LegacyUserEntity> pendingEntity = new ThreadLocal<>();

    public LegacyMemberCommandAdapter(LegacyMemberJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Long persist(Member member) {
        LocalDateTime now = LocalDateTime.now();
        LegacyUserEntity entity =
                LegacyUserEntity.create(
                        member.phoneNumberValue(),
                        "",
                        member.memberNameValue(),
                        LegacyUserEntity.SocialLoginType.EMAIL,
                        null,
                        Yn.Y,
                        Yn.Y,
                        Yn.N,
                        now,
                        now);
        LegacyUserEntity saved = jpaRepository.save(entity);
        pendingEntity.set(saved);
        return saved.getId();
    }

    @Override
    public void persist(MemberAuth memberAuth) {
        LegacyUserEntity entity = resolvePendingEntity();
        if (memberAuth.hasPassword()) {
            entity.resetPassword(memberAuth.passwordHashValue());
        }
        if (memberAuth.authProvider() != com.ryuqq.setof.domain.member.vo.AuthProvider.PHONE) {
            entity.integrateSocial(
                    parseSocialLoginType(memberAuth.authProvider().name()),
                    memberAuth.providerUserIdValue(),
                    entity.getGender(),
                    entity.getDateOfBirth());
        }
    }

    @Override
    public void persist(MemberConsent memberConsent) {
        // 레거시 users 테이블의 consent 컬럼은 persist(Member)에서 기본값으로 설정됨.
        // 별도 업데이트 불필요 (레거시는 가입 시 동의 필수).
    }

    private LegacyUserEntity resolvePendingEntity() {
        LegacyUserEntity entity = pendingEntity.get();
        if (entity != null) {
            pendingEntity.remove();
            return entity;
        }
        throw new IllegalStateException(
                "Pending entity not found. persist(Member) must be called first.");
    }

    private LegacyUserEntity findEntityById(long userId) {
        return jpaRepository
                .findById(userId)
                .orElseThrow(() -> new MemberNotFoundException(String.valueOf(userId)));
    }

    private LegacyUserEntity.SocialLoginType parseSocialLoginType(String socialLoginType) {
        if (socialLoginType == null || socialLoginType.isBlank()) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
        try {
            return LegacyUserEntity.SocialLoginType.valueOf(socialLoginType);
        } catch (IllegalArgumentException e) {
            return LegacyUserEntity.SocialLoginType.EMAIL;
        }
    }
}
