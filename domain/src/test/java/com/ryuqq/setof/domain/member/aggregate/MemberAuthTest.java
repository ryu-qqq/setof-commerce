package com.ryuqq.setof.domain.member.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.member.MemberFixtures;
import com.ryuqq.setof.domain.member.exception.MemberAlreadyRegisteredException;
import com.ryuqq.setof.domain.member.exception.SocialLoginAlreadyExistsException;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.PasswordHash;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("MemberAuth Aggregate 단위 테스트")
class MemberAuthTest {

    @Nested
    @DisplayName("forPhoneAuth() - 전화번호 인증 수단 생성")
    class ForPhoneAuthTest {

        @Test
        @DisplayName("전화번호 인증 수단을 생성한다")
        void createPhoneAuth() {
            // when
            MemberAuth auth = MemberFixtures.newPhoneMemberAuth();

            // then
            assertThat(auth.id()).isNotNull();
            assertThat(auth.isNew()).isTrue();
            assertThat(auth.authProvider()).isEqualTo(AuthProvider.PHONE);
            assertThat(auth.providerUserIdValue()).isEqualTo("010-1234-5678");
            assertThat(auth.hasPassword()).isTrue();
            assertThat(auth.passwordHashValue()).isNotBlank();
            assertThat(auth.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("전화번호 인증 수단의 memberId가 올바르게 설정된다")
        void phoneAuthHasCorrectMemberId() {
            // when
            MemberAuth auth = MemberFixtures.newPhoneMemberAuth();

            // then
            assertThat(auth.memberId()).isNotNull();
            assertThat(auth.memberIdValue()).isEqualTo(MemberFixtures.DEFAULT_MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("forSocialAuth() - 소셜 인증 수단 생성")
    class ForSocialAuthTest {

        @Test
        @DisplayName("카카오 소셜 인증 수단을 생성한다")
        void createKakaoSocialAuth() {
            // when
            MemberAuth auth = MemberFixtures.newKakaoMemberAuth();

            // then
            assertThat(auth.isNew()).isTrue();
            assertThat(auth.authProvider()).isEqualTo(AuthProvider.KAKAO);
            assertThat(auth.providerUserIdValue()).isEqualTo("kakao_123456789");
            assertThat(auth.hasPassword()).isFalse();
            assertThat(auth.passwordHash()).isNull();
            assertThat(auth.passwordHashValue()).isNull();
            assertThat(auth.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("활성 전화번호 인증 수단을 복원한다")
        void reconstituteActivePhoneAuth() {
            // when
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // then
            assertThat(auth.isNew()).isFalse();
            assertThat(auth.idValue()).isEqualTo(10L);
            assertThat(auth.authProvider()).isEqualTo(AuthProvider.PHONE);
            assertThat(auth.hasPassword()).isTrue();
            assertThat(auth.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("활성 카카오 인증 수단을 복원한다")
        void reconstituteActiveKakaoAuth() {
            // when
            MemberAuth auth = MemberFixtures.activeKakaoMemberAuth();

            // then
            assertThat(auth.isNew()).isFalse();
            assertThat(auth.idValue()).isEqualTo(20L);
            assertThat(auth.authProvider()).isEqualTo(AuthProvider.KAKAO);
            assertThat(auth.hasPassword()).isFalse();
            assertThat(auth.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 인증 수단을 복원한다")
        void reconstituteDeletedAuth() {
            // when
            MemberAuth auth = MemberFixtures.deletedMemberAuth();

            // then
            assertThat(auth.isDeleted()).isTrue();
            assertThat(auth.idValue()).isEqualTo(30L);
        }
    }

    @Nested
    @DisplayName("changePassword() - 비밀번호 변경")
    class ChangePasswordTest {

        @Test
        @DisplayName("전화번호 인증 수단의 비밀번호를 변경한다")
        void changePasswordForPhoneAuth() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();
            PasswordHash newHash = PasswordHash.of("$2a$10$newHashedPasswordValue");
            Instant now = CommonVoFixtures.now();

            // when
            auth.changePassword(newHash, now);

            // then
            assertThat(auth.passwordHashValue()).isEqualTo("$2a$10$newHashedPasswordValue");
            assertThat(auth.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("소셜 인증 수단에서 비밀번호 변경 시 예외가 발생한다")
        void changePasswordForSocialAuthThrowsException() {
            // given
            MemberAuth auth = MemberFixtures.activeKakaoMemberAuth();
            PasswordHash newHash = PasswordHash.of("$2a$10$newHashedPasswordValue");

            // when & then
            assertThatThrownBy(() -> auth.changePassword(newHash, CommonVoFixtures.now()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("소셜 로그인");
        }
    }

    @Nested
    @DisplayName("validateCanBeReplacedBy() - 인증 수단 교체 검증")
    class ValidateCanBeReplacedByTest {

        @Test
        @DisplayName("동일 제공자로 교체 시도 시 MemberAlreadyRegisteredException이 발생한다")
        void sameProviderThrowsMemberAlreadyRegisteredException() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // when & then
            assertThatThrownBy(() -> auth.validateCanBeReplacedBy(AuthProvider.PHONE))
                    .isInstanceOf(MemberAlreadyRegisteredException.class);
        }

        @Test
        @DisplayName("소셜 인증에서 전화번호 기반으로 교체 시도 시 SocialLoginAlreadyExistsException이 발생한다")
        void socialToPhoneThrowsSocialLoginAlreadyExistsException() {
            // given
            MemberAuth auth = MemberFixtures.activeKakaoMemberAuth();

            // when & then
            assertThatThrownBy(() -> auth.validateCanBeReplacedBy(AuthProvider.PHONE))
                    .isInstanceOf(SocialLoginAlreadyExistsException.class);
        }

        @Test
        @DisplayName("전화번호 기반에서 소셜 인증으로 교체는 허용된다")
        void phoneToSocialIsAllowed() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // when & then (예외 없이 통과)
            auth.validateCanBeReplacedBy(AuthProvider.KAKAO);
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeletionTest {

        @Test
        @DisplayName("인증 수단을 소프트 삭제한다")
        void deleteMemberAuth() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();
            Instant now = CommonVoFixtures.now();

            // when
            auth.delete(now);

            // then
            assertThat(auth.isDeleted()).isTrue();
            assertThat(auth.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("id 관련 getter들이 올바른 값을 반환한다")
        void returnsIdValues() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // then
            assertThat(auth.id()).isNotNull();
            assertThat(auth.idValue()).isEqualTo(10L);
            assertThat(auth.memberId()).isNotNull();
            assertThat(auth.memberIdValue()).isEqualTo(MemberFixtures.DEFAULT_MEMBER_ID);
        }

        @Test
        @DisplayName("시간 관련 getter들이 올바른 값을 반환한다")
        void returnsTimeValues() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // then
            assertThat(auth.createdAt()).isNotNull();
            assertThat(auth.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus() getter가 올바른 값을 반환한다")
        void returnsDeletionStatus() {
            // given
            MemberAuth auth = MemberFixtures.activePhoneMemberAuth();

            // then
            assertThat(auth.deletionStatus()).isNotNull();
            assertThat(auth.deletionStatus().isDeleted()).isFalse();
        }
    }
}
