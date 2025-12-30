package com.ryuqq.setof.domain.shipment.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** TrackingInfo Value Object 테스트 */
@DisplayName("TrackingInfo Value Object")
class TrackingInfoTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T10:00:00Z");
    private static final Instant DELIVERED_TIME = Instant.parse("2025-01-02T15:30:00Z");

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("empty()로 빈 추적 정보를 생성할 수 있다")
        void shouldCreateEmptyTrackingInfo() {
            // when
            TrackingInfo info = TrackingInfo.empty();

            // then
            assertNotNull(info);
            assertNull(info.lastLocation());
            assertNull(info.lastMessage());
            assertNull(info.lastTrackedAt());
            assertNull(info.deliveredAt());
            assertFalse(info.hasTrackingData());
            assertFalse(info.isDelivered());
        }

        @Test
        @DisplayName("of()로 추적 정보를 생성할 수 있다")
        void shouldCreateTrackingInfo() {
            // when
            TrackingInfo info = TrackingInfo.of("강남집배센터", "배송중", FIXED_TIME);

            // then
            assertNotNull(info);
            assertEquals("강남집배센터", info.lastLocation());
            assertEquals("배송중", info.lastMessage());
            assertEquals(FIXED_TIME, info.lastTrackedAt());
            assertNull(info.deliveredAt());
            assertTrue(info.hasTrackingData());
            assertFalse(info.isDelivered());
        }

        @Test
        @DisplayName("delivered()로 배송 완료 정보를 생성할 수 있다")
        void shouldCreateDeliveredTrackingInfo() {
            // when
            TrackingInfo info =
                    TrackingInfo.delivered("고객 주소지", "배송 완료", FIXED_TIME, DELIVERED_TIME);

            // then
            assertNotNull(info);
            assertEquals("고객 주소지", info.lastLocation());
            assertEquals("배송 완료", info.lastMessage());
            assertEquals(DELIVERED_TIME, info.deliveredAt());
            assertTrue(info.isDelivered());
        }
    }

    @Nested
    @DisplayName("updateTracking() - 추적 정보 업데이트")
    class UpdateTracking {

        @Test
        @DisplayName("추적 정보를 업데이트할 수 있다")
        void shouldUpdateTrackingInfo() {
            // given
            TrackingInfo info = TrackingInfo.of("물류센터", "입고", FIXED_TIME);
            Instant newTime = Instant.parse("2025-01-01T14:00:00Z");

            // when
            TrackingInfo updated = info.updateTracking("강남집배센터", "배송출발", newTime);

            // then
            assertEquals("강남집배센터", updated.lastLocation());
            assertEquals("배송출발", updated.lastMessage());
            assertEquals(newTime, updated.lastTrackedAt());
            assertNull(updated.deliveredAt()); // deliveredAt은 유지
        }

        @Test
        @DisplayName("빈 추적 정보를 업데이트할 수 있다")
        void shouldUpdateEmptyTrackingInfo() {
            // given
            TrackingInfo info = TrackingInfo.empty();

            // when
            TrackingInfo updated = info.updateTracking("물류센터", "입고", FIXED_TIME);

            // then
            assertEquals("물류센터", updated.lastLocation());
            assertTrue(updated.hasTrackingData());
        }
    }

    @Nested
    @DisplayName("markDelivered() - 배송 완료 처리")
    class MarkDelivered {

        @Test
        @DisplayName("배송 완료 시각을 기록할 수 있다")
        void shouldMarkAsDelivered() {
            // given
            TrackingInfo info = TrackingInfo.of("강남집배센터", "배송출발", FIXED_TIME);

            // when
            TrackingInfo delivered = info.markDelivered(DELIVERED_TIME);

            // then
            assertEquals(DELIVERED_TIME, delivered.deliveredAt());
            assertTrue(delivered.isDelivered());
            // 기존 정보는 유지
            assertEquals("강남집배센터", delivered.lastLocation());
            assertEquals("배송출발", delivered.lastMessage());
        }
    }

    @Nested
    @DisplayName("hasTrackingData() - 추적 데이터 존재 여부")
    class HasTrackingData {

        @Test
        @DisplayName("위치 정보가 있으면 true를 반환한다")
        void shouldReturnTrueWhenHasLocation() {
            // given
            TrackingInfo info = TrackingInfo.of("물류센터", null, null);

            // when & then
            assertTrue(info.hasTrackingData());
        }

        @Test
        @DisplayName("메시지만 있어도 true를 반환한다")
        void shouldReturnTrueWhenHasMessage() {
            // given
            TrackingInfo info = new TrackingInfo(null, "배송중", null, null);

            // when & then
            assertTrue(info.hasTrackingData());
        }

        @Test
        @DisplayName("모든 정보가 null이면 false를 반환한다")
        void shouldReturnFalseWhenEmpty() {
            // given
            TrackingInfo info = TrackingInfo.empty();

            // when & then
            assertFalse(info.hasTrackingData());
        }
    }
}
