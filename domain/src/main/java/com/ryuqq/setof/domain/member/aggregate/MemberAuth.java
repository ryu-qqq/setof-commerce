package com.ryuqq.setof.domain.member.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.exception.MemberAlreadyRegisteredException;
import com.ryuqq.setof.domain.member.exception.SocialLoginAlreadyExistsException;
import com.ryuqq.setof.domain.member.id.MemberAuthId;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import com.ryuqq.setof.domain.member.vo.ProviderUserId;
import java.time.Instant;

/**
 * 회원 인증 수단 Aggregate.
 *
 * <p>회원의 로그인 수단을 관리합니다. 한 회원이 여러 인증 수단을 보유할 수 있습니다. (예: PHONE + KAKAO 동시 연동)
 *
 * <p>PHONE 인증 시 passwordHash를 보유하며, 소셜 인증(KAKAO)은 passwordHash가 null입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MemberAuth {

    private final MemberAuthId id;
    private MemberId memberId;
    private final AuthProvider authProvider;
    private final ProviderUserId providerUserId;
    private PasswordHash passwordHash;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private MemberAuth(
            MemberAuthId id,
            MemberId memberId,
            AuthProvider authProvider,
            ProviderUserId providerUserId,
            PasswordHash passwordHash,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.authProvider = authProvider;
        this.providerUserId = providerUserId;
        this.passwordHash = passwordHash;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 전화번호 인증 수단 생성 (비밀번호 포함).
     *
     * @param memberId 회원 ID
     * @param providerUserId 전화번호
     * @param passwordHash 비밀번호 해시
     * @param occurredAt 생성 시각
     * @return 전화번호 인증 MemberAuth 인스턴스
     */
    public static MemberAuth forPhoneAuth(
            MemberId memberId,
            ProviderUserId providerUserId,
            PasswordHash passwordHash,
            Instant occurredAt) {
        return new MemberAuth(
                MemberAuthId.forNew(),
                memberId,
                AuthProvider.PHONE,
                providerUserId,
                passwordHash,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /**
     * 소셜 인증 수단 생성 (비밀번호 없음).
     *
     * @param memberId 회원 ID
     * @param authProvider 인증 제공자 (KAKAO 등)
     * @param providerUserId 소셜 서비스의 고유 ID
     * @param occurredAt 생성 시각
     * @return 소셜 인증 MemberAuth 인스턴스
     */
    public static MemberAuth forSocialAuth(
            MemberId memberId,
            AuthProvider authProvider,
            ProviderUserId providerUserId,
            Instant occurredAt) {
        return new MemberAuth(
                MemberAuthId.forNew(),
                memberId,
                authProvider,
                providerUserId,
                null,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static MemberAuth reconstitute(
            MemberAuthId id,
            MemberId memberId,
            AuthProvider authProvider,
            ProviderUserId providerUserId,
            PasswordHash passwordHash,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new MemberAuth(
                id,
                memberId,
                authProvider,
                providerUserId,
                passwordHash,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    public boolean isNew() {
        return id.isNew();
    }

    /**
     * 회원 PK 할당 (auto-increment 후 1회만 호출).
     *
     * @param memberId 생성된 회원 PK
     * @throws IllegalStateException 이미 할당된 경우
     */
    public void assignMemberId(MemberId memberId) {
        if (this.memberId != null) {
            throw new IllegalStateException("memberId는 이미 할당되었습니다");
        }
        this.memberId = memberId;
    }

    /**
     * 비밀번호 변경.
     *
     * @param newPasswordHash 새 비밀번호 해시
     * @param occurredAt 변경 시각
     * @throws IllegalStateException 소셜 인증인 경우
     */
    public void changePassword(PasswordHash newPasswordHash, Instant occurredAt) {
        if (!authProvider.requiresPassword()) {
            throw new IllegalStateException("소셜 로그인은 비밀번호를 변경할 수 없습니다");
        }
        this.passwordHash = newPasswordHash;
        this.updatedAt = occurredAt;
    }

    /**
     * 현재 인증 수단이 새로운 인증 제공자로 교체 가능한지 검증합니다.
     *
     * <ul>
     *   <li>동일 제공자: 이미 가입된 회원
     *   <li>소셜 → 비밀번호 기반: 소셜 로그인으로 이미 가입되어 있음
     *   <li>비밀번호 기반 → 소셜: 교체 허용
     * </ul>
     *
     * @param newProvider 교체하려는 인증 제공자
     * @throws MemberAlreadyRegisteredException 동일 제공자로 이미 가입된 경우
     * @throws SocialLoginAlreadyExistsException 소셜 → 비밀번호 기반 교체 시도 시
     */
    public void validateCanBeReplacedBy(AuthProvider newProvider) {
        if (this.authProvider == newProvider) {
            throw new MemberAlreadyRegisteredException(newProvider.displayName());
        }

        if (!this.authProvider.requiresPassword() && newProvider.requiresPassword()) {
            throw new SocialLoginAlreadyExistsException(this.authProvider.displayName());
        }
    }

    /** 인증 수단 삭제 (소프트 삭제). */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean hasPassword() {
        return passwordHash != null;
    }

    // VO Getters
    public MemberAuthId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public MemberId memberId() {
        return memberId;
    }

    public Long memberIdValue() {
        return memberId.value();
    }

    public AuthProvider authProvider() {
        return authProvider;
    }

    public ProviderUserId providerUserId() {
        return providerUserId;
    }

    public String providerUserIdValue() {
        return providerUserId.value();
    }

    public PasswordHash passwordHash() {
        return passwordHash;
    }

    public String passwordHashValue() {
        return passwordHash != null ? passwordHash.value() : null;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
