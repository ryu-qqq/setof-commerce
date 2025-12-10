package com.ryuqq.setof.application.member.event;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.event.MemberRegisteredEvent;
import com.ryuqq.setof.domain.member.vo.AuthProvider;
import com.ryuqq.setof.domain.member.vo.Consent;
import com.ryuqq.setof.domain.member.vo.Email;
import com.ryuqq.setof.domain.member.vo.Gender;
import com.ryuqq.setof.domain.member.vo.MemberIdFixture;
import com.ryuqq.setof.domain.member.vo.MemberName;
import com.ryuqq.setof.domain.member.vo.Password;
import com.ryuqq.setof.domain.member.vo.PhoneNumber;
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
    @DisplayName("publish 호출 시 이벤트 발행 후 클리어 (pullDomainEvents 패턴)")
    void shouldPublishEventsAndClear() {
        // Given
        Member member = createMemberWithEvent();

        // When
        memberEventDispatcher.publish(member);

        // Then
        verify(eventPublisher, times(1)).publishEvent(any(MemberRegisteredEvent.class));
        // pullDomainEvents()가 호출되면 내부적으로 클리어됨
        assertTrue(member.pullDomainEvents().isEmpty());
    }

    @Test
    @DisplayName("이벤트가 없는 경우 아무것도 발행하지 않음")
    void shouldNotPublishWhenNoEvents() {
        // Given
        Member member = createMemberWithEvent();
        member.pullDomainEvents(); // 이벤트 클리어

        // When
        memberEventDispatcher.publish(member);

        // Then
        verify(eventPublisher, never()).publishEvent(any());
    }

    private Member createMemberWithEvent() {
        return Member.forNew(
                MemberIdFixture.createNew(),
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
