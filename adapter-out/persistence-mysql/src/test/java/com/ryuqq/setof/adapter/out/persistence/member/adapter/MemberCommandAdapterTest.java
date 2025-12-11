package com.ryuqq.setof.adapter.out.persistence.member.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.JpaSliceTestSupport;
import com.ryuqq.setof.adapter.out.persistence.member.entity.MemberJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.member.mapper.MemberJpaEntityMapper;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

/**
 * MemberCommandAdapter 통합 테스트
 *
 * <p>MemberPersistencePort 구현체의 저장 기능을 검증합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("MemberCommandAdapter 통합 테스트")
@Import({MemberCommandAdapter.class, MemberJpaEntityMapper.class})
class MemberCommandAdapterTest extends JpaSliceTestSupport {

    @Autowired private MemberCommandAdapter memberCommandAdapter;

    @Nested
    @DisplayName("persist 메서드")
    class Persist {

        @Test
        @DisplayName("성공 - LOCAL 회원을 저장한다")
        void persist_localMember_savesSuccessfully() {
            // Given
            Member member = MemberFixture.createLocalMember();

            // When
            MemberId savedId = memberCommandAdapter.persist(member);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();
            assertThat(savedId.value()).isEqualTo(member.getId().value());

            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, member.getIdValue());
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getPhoneNumber()).isEqualTo(member.getPhoneNumberValue());
            assertThat(savedEntity.getEmail()).isEqualTo(member.getEmailValue());
            assertThat(savedEntity.getName()).isEqualTo(member.getNameValue());
            assertThat(savedEntity.getProvider()).isEqualTo(AuthProvider.LOCAL);
            assertThat(savedEntity.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        }

        @Test
        @DisplayName("성공 - KAKAO 회원을 저장한다")
        void persist_kakaoMember_savesSuccessfully() {
            // Given
            Member member = MemberFixture.createKakaoMember();

            // When
            MemberId savedId = memberCommandAdapter.persist(member);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();

            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, member.getIdValue());
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getProvider()).isEqualTo(AuthProvider.KAKAO);
            assertThat(savedEntity.getSocialId()).isEqualTo(member.getSocialIdValue());
            assertThat(savedEntity.getPasswordHash()).isNull();
        }

        @Test
        @DisplayName("성공 - 탈퇴 회원 정보를 저장한다")
        void persist_withdrawnMember_savesWithdrawalInfo() {
            // Given
            Member member = MemberFixture.createWithdrawnMember();

            // When
            MemberId savedId = memberCommandAdapter.persist(member);
            flushAndClear();

            // Then
            assertThat(savedId).isNotNull();

            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, member.getIdValue());
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
            assertThat(savedEntity.getWithdrawalReason()).isNotNull();
            assertThat(savedEntity.getWithdrawnAt()).isNotNull();
        }

        @Test
        @DisplayName("성공 - 동의 정보가 올바르게 저장된다")
        void persist_member_savesConsentInfo() {
            // Given
            Member member = MemberFixture.createLocalMember();

            // When
            memberCommandAdapter.persist(member);
            flushAndClear();

            // Then
            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, member.getIdValue());
            assertThat(savedEntity.isPrivacyConsent()).isTrue();
            assertThat(savedEntity.isServiceTermsConsent()).isTrue();
            assertThat(savedEntity.isAdConsent()).isFalse();
        }

        @Test
        @DisplayName("성공 - 회원 정보를 업데이트한다")
        void persist_existingMember_updatesSuccessfully() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9d01";
            Member originalMember = MemberFixture.createLocalMemberWithId(memberId);
            memberCommandAdapter.persist(originalMember);
            flushAndClear();

            // 업데이트된 회원 정보 (reconstitute로 수정된 상태 재구성)
            Member updatedMember = MemberFixture.createReconstitutedMember(memberId);

            // When
            memberCommandAdapter.persist(updatedMember);
            flushAndClear();

            // Then
            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, memberId);
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getId()).isEqualTo(memberId);
        }
    }

    @Nested
    @DisplayName("UUID v7 PK 검증")
    class UuidV7PrimaryKey {

        @Test
        @DisplayName("성공 - UUID v7 형식의 ID가 PK로 저장된다")
        void persist_uuidV7Id_savedAsPrimaryKey() {
            // Given
            Member member = MemberFixture.createLocalMember();
            String expectedId = member.getIdValue();

            // When
            memberCommandAdapter.persist(member);
            flushAndClear();

            // Then
            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, expectedId);
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getId()).isEqualTo(expectedId);
            assertThat(savedEntity.getId())
                    .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        }

        @Test
        @DisplayName("성공 - 순차적 회원 ID가 올바르게 저장된다")
        void persist_sequentialMemberIds_savedCorrectly() {
            // Given
            Member member1 = MemberFixture.createWithSequence(1);
            Member member2 = MemberFixture.createWithSequence(2);
            Member member3 = MemberFixture.createWithSequence(3);

            // When
            memberCommandAdapter.persist(member1);
            memberCommandAdapter.persist(member2);
            memberCommandAdapter.persist(member3);
            flushAndClear();

            // Then
            MemberJpaEntity entity1 = find(MemberJpaEntity.class, member1.getIdValue());
            MemberJpaEntity entity2 = find(MemberJpaEntity.class, member2.getIdValue());
            MemberJpaEntity entity3 = find(MemberJpaEntity.class, member3.getIdValue());

            assertThat(entity1).isNotNull();
            assertThat(entity2).isNotNull();
            assertThat(entity3).isNotNull();
            assertThat(entity1.getId()).isNotEqualTo(entity2.getId());
            assertThat(entity2.getId()).isNotEqualTo(entity3.getId());
        }
    }

    @Nested
    @DisplayName("nullable 필드 처리")
    class NullableFields {

        @Test
        @DisplayName("성공 - KAKAO 회원은 email과 password가 null이어도 저장된다")
        void persist_kakaoMember_nullableFieldsSavedCorrectly() {
            // Given
            Member kakaoMember = MemberFixture.createKakaoMember();

            // When
            memberCommandAdapter.persist(kakaoMember);
            flushAndClear();

            // Then
            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, kakaoMember.getIdValue());
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getPasswordHash()).isNull();
        }

        @Test
        @DisplayName("성공 - LOCAL 회원은 socialId가 null이어도 저장된다")
        void persist_localMember_nullSocialIdSavedCorrectly() {
            // Given
            Member localMember = MemberFixture.createLocalMember();

            // When
            memberCommandAdapter.persist(localMember);
            flushAndClear();

            // Then
            MemberJpaEntity savedEntity = find(MemberJpaEntity.class, localMember.getIdValue());
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getSocialId()).isNull();
        }
    }
}
