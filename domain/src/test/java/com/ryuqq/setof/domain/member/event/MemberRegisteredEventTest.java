package com.ryuqq.setof.domain.member.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ryuqq.setof.domain.member.MemberFixture;
import com.ryuqq.setof.domain.member.aggregate.Member;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MemberRegisteredEvent")
class MemberRegisteredEventTest {

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-15T10:00:00Z");

    @Nested
    @DisplayName("of 팩토리 메서드")
    class OfMethodTest {

        @Test
        @DisplayName("모든 필드가 올바르게 설정됨")
        void shouldCreateEventWithAllFields() {
            // When
            MemberRegisteredEvent event = MemberRegisteredEvent.of(
                    "member-123",
                    "01012345678",
                    "test@example.com",
                    "홍길동",
                    "LOCAL",
                    FIXED_INSTANT
            );

            // Then
            assertEquals("member-123", event.memberId());
            assertEquals("01012345678", event.phoneNumber());
            assertEquals("test@example.com", event.email());
            assertEquals("홍길동", event.name());
            assertEquals("LOCAL", event.authProvider());
            assertEquals(FIXED_INSTANT, event.occurredAt());
        }
    }

    @Nested
    @DisplayName("from 팩토리 메서드")
    class FromMethodTest {

        @Test
        @DisplayName("Member Aggregate에서 이벤트 생성")
        void shouldCreateEventFromMemberAggregate() {
            // Given
            Member member = MemberFixture.createLocalMember();

            // When
            MemberRegisteredEvent event = MemberRegisteredEvent.from(member, FIXED_INSTANT);

            // Then
            assertNotNull(event);
            assertEquals(member.getIdValue(), event.memberId());
            assertEquals(member.getPhoneNumberValue(), event.phoneNumber());
            assertEquals(member.getEmailValue(), event.email());
            assertEquals(member.getNameValue(), event.name());
            assertEquals(member.getProvider().name(), event.authProvider());
            assertEquals(FIXED_INSTANT, event.occurredAt());
        }

        @Test
        @DisplayName("이메일이 null인 Member에서 이벤트 생성")
        void shouldCreateEventFromMemberWithNullEmail() {
            // Given
            Member member = MemberFixture.createKakaoMember(); // Kakao member has null email

            // When
            MemberRegisteredEvent event = MemberRegisteredEvent.from(member, FIXED_INSTANT);

            // Then
            assertNotNull(event);
            assertEquals(member.getIdValue(), event.memberId());
            assertNull(event.email());
        }
    }
}
