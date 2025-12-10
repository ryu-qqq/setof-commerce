package com.ryuqq.setof.application.member.manager.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.exception.MemberNotFoundException;
import com.ryuqq.setof.domain.member.vo.MemberId;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.member.vo.SocialId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberReadManager")
@ExtendWith(MockitoExtension.class)
class MemberReadManagerTest {

    @Mock private MemberQueryPort memberQueryPort;

    private MemberReadManager memberReadManager;

    @BeforeEach
    void setUp() {
        memberReadManager = new MemberReadManager(memberQueryPort);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTest {

        @Test
        @DisplayName("회원 ID로 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member expectedMember = MemberFixture.createLocalMemberWithId(memberId);
            when(memberQueryPort.findById(any(MemberId.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Member result = memberReadManager.findById(memberId);

            // Then
            assertNotNull(result);
            assertEquals(expectedMember, result);
            verify(memberQueryPort).findById(any(MemberId.class));
        }

        @Test
        @DisplayName("회원 ID로 조회 실패 시 예외 발생")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            when(memberQueryPort.findById(any(MemberId.class))).thenReturn(Optional.empty());

            // When & Then
            assertThrows(MemberNotFoundException.class, () -> memberReadManager.findById(memberId));
            verify(memberQueryPort).findById(any(MemberId.class));
        }
    }

    @Nested
    @DisplayName("findByIdOptional")
    class FindByIdOptionalTest {

        @Test
        @DisplayName("회원 ID로 Optional 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member expectedMember = MemberFixture.createLocalMemberWithId(memberId);
            when(memberQueryPort.findById(any(MemberId.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Optional<Member> result = memberReadManager.findByIdOptional(memberId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(expectedMember, result.get());
            verify(memberQueryPort).findById(any(MemberId.class));
        }

        @Test
        @DisplayName("회원 ID로 Optional 조회 실패 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            when(memberQueryPort.findById(any(MemberId.class))).thenReturn(Optional.empty());

            // When
            Optional<Member> result = memberReadManager.findByIdOptional(memberId);

            // Then
            assertTrue(result.isEmpty());
            verify(memberQueryPort).findById(any(MemberId.class));
        }
    }

    @Nested
    @DisplayName("findByPhoneNumber")
    class FindByPhoneNumberTest {

        @Test
        @DisplayName("핸드폰 번호로 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String phoneNumber = "01012345678";
            Member expectedMember = MemberFixture.createLocalMember();
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Member result = memberReadManager.findByPhoneNumber(phoneNumber);

            // Then
            assertNotNull(result);
            assertEquals(expectedMember, result);
            verify(memberQueryPort).findByPhoneNumber(any(PhoneNumber.class));
        }

        @Test
        @DisplayName("핸드폰 번호로 조회 실패 시 예외 발생")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            String phoneNumber = "01012345678";
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.empty());

            // When & Then
            assertThrows(
                    MemberNotFoundException.class,
                    () -> memberReadManager.findByPhoneNumber(phoneNumber));
            verify(memberQueryPort).findByPhoneNumber(any(PhoneNumber.class));
        }
    }

    @Nested
    @DisplayName("findByPhoneNumberOptional")
    class FindByPhoneNumberOptionalTest {

        @Test
        @DisplayName("핸드폰 번호로 Optional 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String phoneNumber = "01012345678";
            Member expectedMember = MemberFixture.createLocalMember();
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Optional<Member> result = memberReadManager.findByPhoneNumberOptional(phoneNumber);

            // Then
            assertTrue(result.isPresent());
            assertEquals(expectedMember, result.get());
            verify(memberQueryPort).findByPhoneNumber(any(PhoneNumber.class));
        }

        @Test
        @DisplayName("핸드폰 번호로 Optional 조회 실패 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            String phoneNumber = "01012345678";
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.empty());

            // When
            Optional<Member> result = memberReadManager.findByPhoneNumberOptional(phoneNumber);

            // Then
            assertTrue(result.isEmpty());
            verify(memberQueryPort).findByPhoneNumber(any(PhoneNumber.class));
        }
    }

    @Nested
    @DisplayName("findBySocialId")
    class FindBySocialIdTest {

        @Test
        @DisplayName("소셜 ID로 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String socialId = "kakao_12345";
            Member expectedMember = MemberFixture.createKakaoMemberWithSocialId(socialId);
            when(memberQueryPort.findBySocialId(any(SocialId.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Optional<Member> result = memberReadManager.findBySocialId(socialId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(expectedMember, result.get());
            verify(memberQueryPort).findBySocialId(any(SocialId.class));
        }

        @Test
        @DisplayName("소셜 ID로 조회 실패 시 빈 Optional 반환")
        void shouldReturnEmptyOptionalWhenNotFound() {
            // Given
            String socialId = "kakao_12345";
            when(memberQueryPort.findBySocialId(any(SocialId.class))).thenReturn(Optional.empty());

            // When
            Optional<Member> result = memberReadManager.findBySocialId(socialId);

            // Then
            assertTrue(result.isEmpty());
            verify(memberQueryPort).findBySocialId(any(SocialId.class));
        }
    }

    @Nested
    @DisplayName("existsByPhoneNumber")
    class ExistsByPhoneNumberTest {

        @Test
        @DisplayName("핸드폰 번호 존재 시 true 반환")
        void shouldReturnTrueWhenExists() {
            // Given
            String phoneNumber = "01012345678";
            when(memberQueryPort.existsByPhoneNumber(any(PhoneNumber.class))).thenReturn(true);

            // When
            boolean result = memberReadManager.existsByPhoneNumber(phoneNumber);

            // Then
            assertTrue(result);
            verify(memberQueryPort).existsByPhoneNumber(any(PhoneNumber.class));
        }

        @Test
        @DisplayName("핸드폰 번호 미존재 시 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // Given
            String phoneNumber = "01012345678";
            when(memberQueryPort.existsByPhoneNumber(any(PhoneNumber.class))).thenReturn(false);

            // When
            boolean result = memberReadManager.existsByPhoneNumber(phoneNumber);

            // Then
            assertFalse(result);
            verify(memberQueryPort).existsByPhoneNumber(any(PhoneNumber.class));
        }
    }
}
