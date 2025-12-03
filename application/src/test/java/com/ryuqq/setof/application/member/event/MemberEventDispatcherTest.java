package com.ryuqq.setof.application.member.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ryuqq.setof.domain.core.member.aggregate.Member;
import com.ryuqq.setof.domain.core.member.event.MemberRegisteredEvent;
import com.ryuqq.setof.domain.core.member.vo.AuthProvider;
import com.ryuqq.setof.domain.core.member.vo.Consent;
import com.ryuqq.setof.domain.core.member.vo.Email;
import com.ryuqq.setof.domain.core.member.vo.Gender;
import com.ryuqq.setof.domain.core.member.vo.MemberName;
import com.ryuqq.setof.domain.core.member.vo.Password;
import com.ryuqq.setof.domain.core.member.vo.PhoneNumber;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("MemberEventDispatcher")
@ExtendWith(MockitoExtension.class)
class MemberEventDispatcherTest {

    @Mock private ApplicationEventPublisher eventPublisher;

    private MemberEventDispatcher memberEventDispatcher;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        memberEventDispatcher = new MemberEventDispatcher(eventPublisher);
        fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("UTC"));
    }

    @Test
    @DisplayName("publishAndClear 호출 시 이벤트 발행 후 클리어")
    void shouldPublishEventsAndClear() {
        // Given
        Member member = createMemberWithEvent();
        int eventCount = member.getDomainEvents().size();
        assertEquals(1, eventCount);

        // When
        memberEventDispatcher.publishAndClear(member);

        // Then
        verify(eventPublisher, times(eventCount)).publishEvent(any(MemberRegisteredEvent.class));
        assertTrue(member.getDomainEvents().isEmpty());
    }

    @Test
    @DisplayName("publishOnly 호출 시 이벤트 발행만 수행 (클리어하지 않음)")
    void shouldPublishEventsWithoutClear() {
        // Given
        Member member = createMemberWithEvent();
        int eventCount = member.getDomainEvents().size();
        assertEquals(1, eventCount);

        // When
        memberEventDispatcher.publishOnly(member);

        // Then
        verify(eventPublisher, times(eventCount)).publishEvent(any(MemberRegisteredEvent.class));
        assertFalse(member.getDomainEvents().isEmpty());
    }

    @Test
    @DisplayName("이벤트가 없는 경우 아무것도 발행하지 않음")
    void shouldNotPublishWhenNoEvents() {
        // Given
        Member member = createMemberWithEvent();
        member.clearDomainEvents();

        // When
        memberEventDispatcher.publishAndClear(member);

        // Then
        verify(eventPublisher, never()).publishEvent(any());
    }

    private Member createMemberWithEvent() {
        return Member.forNew(
                PhoneNumber.of("01012345678"),
                Email.of("test@example.com"),
                Password.of("$2a$10$hashedPassword"),
                MemberName.of("테스트"),
                LocalDate.of(1990, 1, 1),
                Gender.M,
                AuthProvider.LOCAL,
                null,
                Consent.of(true, true, false),
                fixedClock);
    }
}
