package com.ryuqq.setof.domain.member.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.Email;
import com.ryuqq.setof.domain.common.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.DateOfBirth;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.MemberStatus;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Member Aggregate 단위 테스트")
class MemberTest {

    @Nested
    @DisplayName("forNew() - 신규 회원 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 필드로 신규 회원을 생성한다")
        void createNewMemberWithRequiredFields() {
            // when
            Member member = MemberFixtures.newMember();

            // then
            assertThat(member.id()).isNotNull();
            assertThat(member.memberNameValue()).isEqualTo("홍길동");
            assertThat(member.emailValue()).isEqualTo("test@example.com");
            assertThat(member.phoneNumberValue()).isEqualTo("010-1234-5678");
            assertThat(member.gender()).isEqualTo(Gender.MALE);
            assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
            assertThat(member.isActive()).isTrue();
            assertThat(member.isDeleted()).isFalse();
            assertThat(member.legacyMemberId()).isNull();
            assertThat(member.isMigrated()).isFalse();
        }

        @Test
        @DisplayName("신규 회원의 createdAt과 updatedAt이 동일하다")
        void createdAtEqualsUpdatedAt() {
            // given
            Instant now = CommonVoFixtures.now();
            Member member =
                    Member.forNew(
                            MemberFixtures.defaultMemberId(),
                            MemberFixtures.defaultMemberName(),
                            MemberFixtures.defaultEmail(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            MemberFixtures.defaultDateOfBirth(),
                            Gender.FEMALE,
                            now);

            // then
            assertThat(member.createdAt()).isEqualTo(now);
            assertThat(member.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("forMigration() - 레거시 마이그레이션 회원 생성")
    class ForMigrationTest {

        @Test
        @DisplayName("레거시 마이그레이션 회원을 생성한다")
        void createMigratedMember() {
            // when
            Member member = MemberFixtures.migratedMember();

            // then
            assertThat(member.legacyMemberId()).isNotNull();
            assertThat(member.legacyMemberIdValue()).isEqualTo(1001L);
            assertThat(member.isMigrated()).isTrue();
            assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
            assertThat(member.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 상태 회원을 복원한다")
        void reconstituteActiveMember() {
            // when
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.id()).isNotNull();
            assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
            assertThat(member.isActive()).isTrue();
            assertThat(member.isDeleted()).isFalse();
            assertThat(member.canLogin()).isTrue();
        }

        @Test
        @DisplayName("정지 상태 회원을 복원한다")
        void reconstituteSuspendedMember() {
            // when
            Member member = MemberFixtures.suspendedMember();

            // then
            assertThat(member.status()).isEqualTo(MemberStatus.SUSPENDED);
            assertThat(member.isActive()).isFalse();
            assertThat(member.canLogin()).isFalse();
        }

        @Test
        @DisplayName("탈퇴 상태 회원을 복원한다")
        void reconstituteWithdrawnMember() {
            // when
            Member member = MemberFixtures.withdrawnMember();

            // then
            assertThat(member.status()).isEqualTo(MemberStatus.WITHDRAWN);
            assertThat(member.isActive()).isFalse();
            assertThat(member.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("소프트 삭제된 회원을 복원한다")
        void reconstituteDeletedMember() {
            // when
            Member member = MemberFixtures.deletedMember();

            // then
            assertThat(member.isDeleted()).isTrue();
            assertThat(member.isActive()).isFalse();
        }

        @Test
        @DisplayName("레거시 마이그레이션된 활성 회원을 복원한다")
        void reconstituteMigratedMember() {
            // when
            Member member = MemberFixtures.activeMigratedMember();

            // then
            assertThat(member.isMigrated()).isTrue();
            assertThat(member.legacyMemberIdValue()).isEqualTo(1001L);
            assertThat(member.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("updateProfile() - 프로필 수정")
    class UpdateProfileTest {

        @Test
        @DisplayName("프로필 정보를 수정한다")
        void updateProfile() {
            // given
            Member member = MemberFixtures.activeMember();
            MemberName newName = MemberName.of("김철수");
            Email newEmail = Email.of("new@example.com");
            PhoneNumber newPhone = PhoneNumber.of("010-9999-8888");
            DateOfBirth newDob = DateOfBirth.of(LocalDate.of(1985, 6, 15));
            Instant now = CommonVoFixtures.now();

            // when
            member.updateProfile(newName, newEmail, newPhone, newDob, Gender.FEMALE, now);

            // then
            assertThat(member.memberNameValue()).isEqualTo("김철수");
            assertThat(member.emailValue()).isEqualTo("new@example.com");
            assertThat(member.phoneNumberValue()).isEqualTo("010-9999-8888");
            assertThat(member.gender()).isEqualTo(Gender.FEMALE);
            assertThat(member.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("상태 변경 메서드 테스트")
    class StateChangeTest {

        @Test
        @DisplayName("정지된 회원을 활성화한다")
        void activateSuspendedMember() {
            // given
            Member member = MemberFixtures.suspendedMember();
            assertThat(member.status()).isEqualTo(MemberStatus.SUSPENDED);
            Instant now = CommonVoFixtures.now();

            // when
            member.activate(now);

            // then
            assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
            assertThat(member.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("활성 회원을 정지한다")
        void suspendActiveMember() {
            // given
            Member member = MemberFixtures.activeMember();
            assertThat(member.status()).isEqualTo(MemberStatus.ACTIVE);
            Instant now = CommonVoFixtures.now();

            // when
            member.suspend(now);

            // then
            assertThat(member.status()).isEqualTo(MemberStatus.SUSPENDED);
            assertThat(member.canLogin()).isFalse();
            assertThat(member.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("회원을 탈퇴 처리한다")
        void withdrawMember() {
            // given
            Member member = MemberFixtures.activeMember();
            Instant now = CommonVoFixtures.now();

            // when
            member.withdraw(now);

            // then
            assertThat(member.status()).isEqualTo(MemberStatus.WITHDRAWN);
            assertThat(member.isDeleted()).isTrue();
            assertThat(member.isActive()).isFalse();
            assertThat(member.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("회원을 소프트 삭제한다")
        void deleteMember() {
            // given
            Member member = MemberFixtures.activeMember();
            Instant now = CommonVoFixtures.now();

            // when
            member.delete(now);

            // then
            assertThat(member.isDeleted()).isTrue();
            assertThat(member.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("비즈니스 규칙 검증 테스트")
    class BusinessRuleTest {

        @Test
        @DisplayName("활성 상태이고 삭제되지 않은 회원은 로그인 가능하다")
        void activeNonDeletedMemberCanLogin() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.canLogin()).isTrue();
        }

        @Test
        @DisplayName("삭제된 회원은 활성 상태여도 isActive()가 false다")
        void deletedMemberIsNotActive() {
            // given
            Member member = MemberFixtures.deletedMember();

            // then
            assertThat(member.isDeleted()).isTrue();
            assertThat(member.isActive()).isFalse();
        }

        @Test
        @DisplayName("정지된 회원은 canLogin()이 false다")
        void suspendedMemberCanNotLogin() {
            // given
            Member member = MemberFixtures.suspendedMember();

            // then
            assertThat(member.canLogin()).isFalse();
        }

        @Test
        @DisplayName("레거시 ID가 없는 회원은 isMigrated()가 false다")
        void memberWithoutLegacyIdIsNotMigrated() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.isMigrated()).isFalse();
            assertThat(member.legacyMemberId()).isNull();
            assertThat(member.legacyMemberIdValue()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id 관련 getter들이 올바른 값을 반환한다")
        void returnsIdValues() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.id()).isNotNull();
            assertThat(member.id()).isInstanceOf(MemberId.class);
            assertThat(member.idValue()).isEqualTo(MemberFixtures.DEFAULT_MEMBER_ID);
        }

        @Test
        @DisplayName("시간 관련 getter들이 올바른 값을 반환한다")
        void returnsTimeValues() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.createdAt()).isNotNull();
            assertThat(member.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus() getter가 올바른 값을 반환한다")
        void returnsDeletionStatus() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.deletionStatus()).isNotNull();
            assertThat(member.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("dateOfBirth() getter가 올바른 값을 반환한다")
        void returnsDateOfBirth() {
            // given
            Member member = MemberFixtures.activeMember();

            // then
            assertThat(member.dateOfBirth()).isNotNull();
            assertThat(member.dateOfBirth().value()).isEqualTo(LocalDate.of(1990, 1, 1));
        }
    }
}
