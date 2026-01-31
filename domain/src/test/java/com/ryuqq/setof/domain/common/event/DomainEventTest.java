package com.ryuqq.setof.domain.common.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DomainEvent 인터페이스 테스트")
class DomainEventTest {

    @Nested
    @DisplayName("eventType() - 이벤트 타입 식별자")
    class EventTypeTest {

        @Test
        @DisplayName("eventType()은 구현 클래스의 단순명을 반환한다")
        void eventTypeReturnsSimpleClassName() {
            // given
            DomainEvent event = new TestDomainEvent(Instant.now());

            // when
            String eventType = event.eventType();

            // then
            assertThat(eventType).isEqualTo("TestDomainEvent");
        }

        @Test
        @DisplayName("익명 클래스의 eventType()은 빈 문자열을 반환한다")
        void anonymousClassEventTypeReturnsEmptyString() {
            // given
            Instant now = Instant.now();
            DomainEvent event =
                    new DomainEvent() {
                        @Override
                        public Instant occurredAt() {
                            return now;
                        }
                    };

            // when
            String eventType = event.eventType();

            // then
            assertThat(eventType).isEmpty();
        }
    }

    /** 테스트용 DomainEvent 구현체 */
    private static class TestDomainEvent implements DomainEvent {

        private final Instant occurredAt;

        TestDomainEvent(Instant occurredAt) {
            this.occurredAt = occurredAt;
        }

        @Override
        public Instant occurredAt() {
            return occurredAt;
        }
    }
}
