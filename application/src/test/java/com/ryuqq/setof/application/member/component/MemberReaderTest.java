package com.ryuqq.setof.application.member.component;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.application.member.port.out.query.MemberQueryPort;
import com.ryuqq.setof.domain.core.member.MemberFixture;
import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.exception.MemberNotFoundException;
import com.ryuqq.setof.domain.core.member.vo.MemberId;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import com.ryuqq.setof.domain.core.member.vo.SocialId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberReader")
@ExtendWith(MockitoExtension.class)
class MemberReaderTest {

    @Mock private MemberQueryPort memberQueryPort;

    private MemberReader memberReader;

    @BeforeEach
    void setUp() {
        memberReader = new MemberReader(memberQueryPort);
    }

    @Nested
    @DisplayName("getById")
    class GetByIdTest {

        @Test
        @DisplayName("회원 ID로 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String memberId = "01936ddc-8d37-7c6e-8ad6-18c76adc9dfa";
            Member expectedMember = MemberFixture.createLocalMemberWithId(memberId);
            when(memberQueryPort.findById(any(MemberId.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Member result = memberReader.getById(memberId);

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
            assertThrows(MemberNotFoundException.class, () -> memberReader.getById(memberId));
            verify(memberQueryPort).findById(any(MemberId.class));
        }
    }

    @Nested
    @DisplayName("getByPhoneNumber")
    class GetByPhoneNumberTest {

        @Test
        @DisplayName("핸드폰 번호로 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String phoneNumber = "01012345678";
            Member expectedMember = MemberFixture.createLocalMember();
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Member result = memberReader.getByPhoneNumber(phoneNumber);

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
                    () -> memberReader.getByPhoneNumber(phoneNumber));
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
            Optional<Member> result = memberReader.findBySocialId(socialId);

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
            Optional<Member> result = memberReader.findBySocialId(socialId);

            // Then
            assertTrue(result.isEmpty());
            verify(memberQueryPort).findBySocialId(any(SocialId.class));
        }
    }

    @Nested
    @DisplayName("findByPhoneNumber")
    class FindByPhoneNumberTest {

        @Test
        @DisplayName("핸드폰 번호로 Optional 조회 성공")
        void shouldReturnMemberWhenFound() {
            // Given
            String phoneNumber = "01012345678";
            Member expectedMember = MemberFixture.createLocalMember();
            when(memberQueryPort.findByPhoneNumber(any(PhoneNumber.class)))
                    .thenReturn(Optional.of(expectedMember));

            // When
            Optional<Member> result = memberReader.findByPhoneNumber(phoneNumber);

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
            Optional<Member> result = memberReader.findByPhoneNumber(phoneNumber);

            // Then
            assertTrue(result.isEmpty());
            verify(memberQueryPort).findByPhoneNumber(any(PhoneNumber.class));
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
            boolean result = memberReader.existsByPhoneNumber(phoneNumber);

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
            boolean result = memberReader.existsByPhoneNumber(phoneNumber);

            // Then
            assertFalse(result);
            verify(memberQueryPort).existsByPhoneNumber(any(PhoneNumber.class));
        }
    }
}
