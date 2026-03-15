package com.ryuqq.setof.domain.member.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("Member Value Objects 단위 테스트")
class MemberVoTest {

    // ===== MemberName =====
    @Nested
    @DisplayName("MemberName Value Object 테스트")
    class MemberNameTest {

        @Nested
        @DisplayName("생성 테스트")
        class CreationTest {

            @Test
            @DisplayName("유효한 이름으로 MemberName을 생성한다")
            void createWithValidName() {
                // when
                MemberName name = MemberName.of("홍길동");

                // then
                assertThat(name.value()).isEqualTo("홍길동");
            }

            @Test
            @DisplayName("앞뒤 공백이 제거된다")
            void trimWhitespace() {
                // when
                MemberName name = MemberName.of("  홍길동  ");

                // then
                assertThat(name.value()).isEqualTo("홍길동");
            }

            @Test
            @DisplayName("null 이름으로 생성하면 예외가 발생한다")
            void createWithNullThrowsException() {
                assertThatThrownBy(() -> MemberName.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("필수");
            }

            @Test
            @DisplayName("빈 이름으로 생성하면 예외가 발생한다")
            void createWithBlankThrowsException() {
                assertThatThrownBy(() -> MemberName.of(""))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            @DisplayName("50자를 초과하는 이름으로 생성하면 예외가 발생한다")
            void createWithTooLongNameThrowsException() {
                String longName = "가".repeat(51);
                assertThatThrownBy(() -> MemberName.of(longName))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("50");
            }

            @Test
            @DisplayName("정확히 50자인 이름은 생성된다")
            void createWith50CharName() {
                String name50 = "가".repeat(50);
                MemberName name = MemberName.of(name50);
                assertThat(name.value()).isEqualTo(name50);
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 이름은 동등하다")
            void sameNameEquals() {
                MemberName name1 = MemberName.of("홍길동");
                MemberName name2 = MemberName.of("홍길동");
                assertThat(name1).isEqualTo(name2);
                assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
            }

            @Test
            @DisplayName("다른 이름은 동등하지 않다")
            void differentNameNotEquals() {
                MemberName name1 = MemberName.of("홍길동");
                MemberName name2 = MemberName.of("김철수");
                assertThat(name1).isNotEqualTo(name2);
            }
        }
    }

    // ===== DateOfBirth =====
    @Nested
    @DisplayName("DateOfBirth Value Object 테스트")
    class DateOfBirthTest {

        @Nested
        @DisplayName("생성 테스트")
        class CreationTest {

            @Test
            @DisplayName("유효한 날짜로 DateOfBirth를 생성한다")
            void createWithValidDate() {
                // when
                DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 5, 15));

                // then
                assertThat(dob.value()).isEqualTo(LocalDate.of(1990, 5, 15));
            }

            @Test
            @DisplayName("null 날짜로 생성하면 예외가 발생한다")
            void createWithNullThrowsException() {
                assertThatThrownBy(() -> DateOfBirth.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("필수");
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 날짜는 동등하다")
            void sameDateEquals() {
                DateOfBirth dob1 = DateOfBirth.of(LocalDate.of(1990, 1, 1));
                DateOfBirth dob2 = DateOfBirth.of(LocalDate.of(1990, 1, 1));
                assertThat(dob1).isEqualTo(dob2);
                assertThat(dob1.hashCode()).isEqualTo(dob2.hashCode());
            }
        }
    }

    // ===== PasswordHash =====
    @Nested
    @DisplayName("PasswordHash Value Object 테스트")
    class PasswordHashTest {

        @Nested
        @DisplayName("생성 테스트")
        class CreationTest {

            @Test
            @DisplayName("유효한 해시값으로 PasswordHash를 생성한다")
            void createWithValidHash() {
                // when
                PasswordHash hash = PasswordHash.of("$2a$10$somehashedvalue");

                // then
                assertThat(hash.value()).isEqualTo("$2a$10$somehashedvalue");
            }

            @Test
            @DisplayName("null 값으로 생성하면 예외가 발생한다")
            void createWithNullThrowsException() {
                assertThatThrownBy(() -> PasswordHash.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("필수");
            }

            @Test
            @DisplayName("빈 값으로 생성하면 예외가 발생한다")
            void createWithBlankThrowsException() {
                assertThatThrownBy(() -> PasswordHash.of(""))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 해시값은 동등하다")
            void sameHashEquals() {
                PasswordHash hash1 = PasswordHash.of("$2a$10$somehashedvalue");
                PasswordHash hash2 = PasswordHash.of("$2a$10$somehashedvalue");
                assertThat(hash1).isEqualTo(hash2);
            }
        }
    }

    // ===== ProviderUserId =====
    @Nested
    @DisplayName("ProviderUserId Value Object 테스트")
    class ProviderUserIdTest {

        @Nested
        @DisplayName("생성 테스트")
        class CreationTest {

            @Test
            @DisplayName("전화번호로 ProviderUserId를 생성한다")
            void createWithPhoneNumber() {
                // when
                ProviderUserId id = ProviderUserId.of("010-1234-5678");

                // then
                assertThat(id.value()).isEqualTo("010-1234-5678");
            }

            @Test
            @DisplayName("소셜 ID로 ProviderUserId를 생성한다")
            void createWithSocialId() {
                // when
                ProviderUserId id = ProviderUserId.of("kakao_123456789");

                // then
                assertThat(id.value()).isEqualTo("kakao_123456789");
            }

            @Test
            @DisplayName("앞뒤 공백이 제거된다")
            void trimWhitespace() {
                // when
                ProviderUserId id = ProviderUserId.of("  010-1234-5678  ");

                // then
                assertThat(id.value()).isEqualTo("010-1234-5678");
            }

            @Test
            @DisplayName("null 값으로 생성하면 예외가 발생한다")
            void createWithNullThrowsException() {
                assertThatThrownBy(() -> ProviderUserId.of(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("필수");
            }

            @Test
            @DisplayName("빈 값으로 생성하면 예외가 발생한다")
            void createWithBlankThrowsException() {
                assertThatThrownBy(() -> ProviderUserId.of(""))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("동등성 테스트")
        class EqualityTest {

            @Test
            @DisplayName("같은 값은 동등하다")
            void sameValueEquals() {
                ProviderUserId id1 = ProviderUserId.of("010-1234-5678");
                ProviderUserId id2 = ProviderUserId.of("010-1234-5678");
                assertThat(id1).isEqualTo(id2);
                assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
            }
        }
    }

    // ===== RefreshTokenCacheKey =====
    @Nested
    @DisplayName("RefreshTokenCacheKey Value Object 테스트")
    class RefreshTokenCacheKeyTest {

        @Test
        @DisplayName("refresh token으로 캐시 키를 생성한다")
        void createCacheKey() {
            // when
            RefreshTokenCacheKey key = RefreshTokenCacheKey.of("myRefreshTokenValue");

            // then
            assertThat(key.value()).isEqualTo("cache:refresh-token:myRefreshTokenValue");
        }

        @Test
        @DisplayName("다른 refresh token은 다른 캐시 키를 생성한다")
        void differentTokensDifferentKeys() {
            // when
            RefreshTokenCacheKey key1 = RefreshTokenCacheKey.of("token1");
            RefreshTokenCacheKey key2 = RefreshTokenCacheKey.of("token2");

            // then
            assertThat(key1.value()).isNotEqualTo(key2.value());
        }

        @Test
        @DisplayName("캐시 키 접두사가 포함된다")
        void keyContainsPrefix() {
            // when
            RefreshTokenCacheKey key = RefreshTokenCacheKey.of("someToken");

            // then
            assertThat(key.value()).startsWith("cache:refresh-token:");
        }
    }

    // ===== AuthProvider =====
    @Nested
    @DisplayName("AuthProvider enum 테스트")
    class AuthProviderTest {

        @Test
        @DisplayName("PHONE은 비밀번호가 필요하다")
        void phoneRequiresPassword() {
            assertThat(AuthProvider.PHONE.requiresPassword()).isTrue();
        }

        @Test
        @DisplayName("KAKAO는 비밀번호가 필요하지 않다")
        void kakaoDoesNotRequirePassword() {
            assertThat(AuthProvider.KAKAO.requiresPassword()).isFalse();
        }

        @Test
        @DisplayName("각 provider의 displayName이 올바르다")
        void displayNames() {
            assertThat(AuthProvider.PHONE.displayName()).isEqualTo("전화번호");
            assertThat(AuthProvider.KAKAO.displayName()).isEqualTo("카카오");
        }

        @Test
        @DisplayName("모든 AuthProvider 값이 존재한다")
        void allValuesExist() {
            assertThat(AuthProvider.values())
                    .containsExactlyInAnyOrder(AuthProvider.PHONE, AuthProvider.KAKAO);
        }
    }

    // ===== MemberStatus =====
    @Nested
    @DisplayName("MemberStatus enum 테스트")
    class MemberStatusTest {

        @Test
        @DisplayName("ACTIVE만 canLogin()이 true다")
        void onlyActiveCanLogin() {
            assertThat(MemberStatus.ACTIVE.canLogin()).isTrue();
            assertThat(MemberStatus.INACTIVE.canLogin()).isFalse();
            assertThat(MemberStatus.SUSPENDED.canLogin()).isFalse();
            assertThat(MemberStatus.WITHDRAWN.canLogin()).isFalse();
        }

        @Test
        @DisplayName("각 상태의 displayName이 올바르다")
        void displayNames() {
            assertThat(MemberStatus.ACTIVE.displayName()).isEqualTo("활성");
            assertThat(MemberStatus.INACTIVE.displayName()).isEqualTo("비활성");
            assertThat(MemberStatus.SUSPENDED.displayName()).isEqualTo("정지");
            assertThat(MemberStatus.WITHDRAWN.displayName()).isEqualTo("탈퇴");
        }

        @Test
        @DisplayName("모든 MemberStatus 값이 존재한다")
        void allValuesExist() {
            assertThat(MemberStatus.values())
                    .containsExactlyInAnyOrder(
                            MemberStatus.ACTIVE,
                            MemberStatus.INACTIVE,
                            MemberStatus.SUSPENDED,
                            MemberStatus.WITHDRAWN);
        }
    }

    // ===== Gender =====
    @Nested
    @DisplayName("Gender enum 테스트")
    class GenderTest {

        @Test
        @DisplayName("각 Gender의 displayName이 올바르다")
        void displayNames() {
            assertThat(Gender.MALE.displayName()).isEqualTo("남성");
            assertThat(Gender.FEMALE.displayName()).isEqualTo("여성");
            assertThat(Gender.OTHER.displayName()).isEqualTo("기타");
        }

        @Test
        @DisplayName("모든 Gender 값이 존재한다")
        void allValuesExist() {
            assertThat(Gender.values())
                    .containsExactlyInAnyOrder(Gender.MALE, Gender.FEMALE, Gender.OTHER);
        }
    }
}
