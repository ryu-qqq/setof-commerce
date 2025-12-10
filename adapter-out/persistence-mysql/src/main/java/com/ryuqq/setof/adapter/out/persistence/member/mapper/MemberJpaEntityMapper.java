package com.ryuqq.setof.adapter.out.persistence.member.mapper;

import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.Consent;
import com.ryuqq.setof.domain.member.vo.Email;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.Password;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import com.ryuqq.setof.domain.member.vo.WithdrawalInfo;
import com.ryuqq.setof.domain.member.vo.WithdrawalReason;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * MemberJpaEntityMapper - Entity ↔ Domain 변환 Mapper
 *
 * <p>Persistence Layer의 JPA Entity와 Domain Layer의 Member 간 변환을 담당합니다.
 *
 * <p><strong>변환 책임:</strong>
 *
 * <ul>
 *   <li>Member → MemberJpaEntity (저장용)
 *   <li>MemberJpaEntity → Member (조회용)
 *   <li>Value Object 추출 및 재구성
 * </ul>
 *
 * <p><strong>Instant 기반 시간 처리:</strong>
 *
 * <ul>
 *   <li>Domain과 Entity 모두 Instant 타입 사용 (UTC 기준)
 *   <li>시간 변환 로직 불필요 - 직접 전달
 *   <li>타임존 문제 원천 차단
 * </ul>
 *
 * <p><strong>Hexagonal Architecture 관점:</strong>
 *
 * <ul>
 *   <li>Adapter Layer의 책임
 *   <li>Domain과 Infrastructure 기술 분리
 *   <li>Domain은 JPA 의존성 없음
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Component
public class MemberJpaEntityMapper {

    /**
     * Domain → Entity 변환
     *
     * <p><strong>사용 시나리오:</strong>
     *
     * <ul>
     *   <li>신규 Member 저장 (Entity ID가 null)
     *   <li>기존 Member 수정 (Entity ID가 있음)
     * </ul>
     *
     * <p><strong>변환 규칙:</strong>
     *
     * <ul>
     *   <li>MemberId → id (UUID v7 문자열, PK로 사용)
     *   <li>Value Objects → primitive 값 추출
     *   <li>Consent → 개별 boolean 필드로 분해
     *   <li>WithdrawalInfo → withdrawalReason, withdrawnAt 분해
     *   <li>Instant → Instant (타입 일치, 변환 불필요)
     * </ul>
     *
     * @param domain Member 도메인
     * @return MemberJpaEntity
     */
    public MemberJpaEntity toEntity(Member domain) {
        return MemberJpaEntity.of(
                domain.getIdValue(), // UUID v7을 PK로 사용
                domain.getPhoneNumberValue(),
                domain.getEmailValue(),
                domain.getPasswordValue(),
                domain.getNameValue(),
                domain.getDateOfBirth(),
                domain.getGender(),
                domain.getProvider(),
                domain.getSocialIdValue(),
                domain.getStatus(),
                extractPrivacyConsent(domain.getConsent()),
                extractServiceTermsConsent(domain.getConsent()),
                extractAdConsent(domain.getConsent()),
                extractWithdrawalReason(domain.getWithdrawalInfo()),
                extractWithdrawnAt(domain.getWithdrawalInfo()),
                domain.getCreatedAt(), // Instant 직접 전달
                domain.getUpdatedAt(), // Instant 직접 전달
                null // deletedAt은 별도 관리
                );
    }

    /**
     * Entity → Domain 변환
     *
     * <p><strong>사용 시나리오:</strong>
     *
     * <ul>
     *   <li>데이터베이스에서 조회한 Entity를 Domain으로 변환
     *   <li>Application Layer로 전달
     * </ul>
     *
     * <p><strong>변환 규칙:</strong>
     *
     * <ul>
     *   <li>id (UUID v7) → MemberId
     *   <li>primitive 값 → Value Objects 재구성
     *   <li>consent 필드들 → Consent VO 재구성
     *   <li>withdrawal 필드들 → WithdrawalInfo VO 재구성
     *   <li>Instant → Instant (타입 일치, 변환 불필요)
     * </ul>
     *
     * @param entity MemberJpaEntity
     * @return Member 도메인
     */
    public Member toDomain(MemberJpaEntity entity) {
        return Member.reconstitute(
                MemberId.of(entity.getId()),
                PhoneNumber.of(entity.getPhoneNumber()),
                buildEmail(entity.getEmail()),
                Password.of(entity.getPasswordHash()),
                MemberName.of(entity.getName()),
                entity.getDateOfBirth(),
                entity.getGender(),
                entity.getProvider(),
                buildSocialId(entity.getSocialId()),
                entity.getStatus(),
                buildConsent(entity),
                buildWithdrawalInfo(entity),
                entity.getCreatedAt(), // Instant 직접 전달
                entity.getUpdatedAt()); // Instant 직접 전달
    }

    // ========== Private Helper Methods ==========

    private boolean extractPrivacyConsent(Consent consent) {
        return consent != null && consent.privacyConsent();
    }

    private boolean extractServiceTermsConsent(Consent consent) {
        return consent != null && consent.serviceConsent();
    }

    private boolean extractAdConsent(Consent consent) {
        return consent != null && consent.marketingConsent();
    }

    private WithdrawalReason extractWithdrawalReason(WithdrawalInfo info) {
        return info != null ? info.reason() : null;
    }

    /**
     * WithdrawalInfo에서 탈퇴 일시 추출
     *
     * @param info WithdrawalInfo (nullable)
     * @return 탈퇴 일시 Instant (nullable)
     */
    private Instant extractWithdrawnAt(WithdrawalInfo info) {
        return info != null ? info.withdrawnAt() : null;
    }

    private Email buildEmail(String email) {
        return Email.of(email);
    }

    private SocialId buildSocialId(String socialId) {
        return socialId != null ? SocialId.of(socialId) : null;
    }

    private Consent buildConsent(MemberJpaEntity entity) {
        return Consent.of(
                entity.isPrivacyConsent(), entity.isServiceTermsConsent(), entity.isAdConsent());
    }

    private WithdrawalInfo buildWithdrawalInfo(MemberJpaEntity entity) {
        if (entity.getWithdrawalReason() == null || entity.getWithdrawnAt() == null) {
            return null;
        }
        return WithdrawalInfo.of(entity.getWithdrawalReason(), entity.getWithdrawnAt());
    }
}
