package com.ryuqq.setof.adapter.out.persistence.memberauth.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * MemberAuthJpaEntity - 회원 인증 수단 JPA 엔티티.
 *
 * <p>하나의 회원이 여러 인증 수단(PHONE, KAKAO)을 가질 수 있습니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "member_auths")
public class MemberAuthJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "auth_provider", nullable = false, length = 20)
    private String authProvider;

    @Column(name = "provider_user_id", nullable = false, length = 255)
    private String providerUserId;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    protected MemberAuthJpaEntity() {
        super();
    }

    private MemberAuthJpaEntity(
            Long id,
            Long memberId,
            String authProvider,
            String providerUserId,
            String passwordHash,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.memberId = memberId;
        this.authProvider = authProvider;
        this.providerUserId = providerUserId;
        this.passwordHash = passwordHash;
    }

    public static MemberAuthJpaEntity create(
            Long id,
            Long memberId,
            String authProvider,
            String providerUserId,
            String passwordHash,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new MemberAuthJpaEntity(
                id,
                memberId,
                authProvider,
                providerUserId,
                passwordHash,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
