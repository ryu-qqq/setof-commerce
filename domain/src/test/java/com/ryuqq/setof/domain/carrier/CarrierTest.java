package com.ryuqq.setof.domain.carrier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import com.ryuqq.setof.domain.carrier.vo.CarrierName;
import com.ryuqq.setof.domain.carrier.vo.CarrierStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Carrier Aggregate 테스트
 *
 * <p>택배사 정보 관리에 대한 도메인 로직을 테스트합니다.
 */
@DisplayName("Carrier Aggregate")
class CarrierTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant UPDATED_TIME = Instant.parse("2025-01-02T00:00:00Z");

    @Nested
    @DisplayName("forNew() - 신규 택배사 생성")
    class ForNew {

        @Test
        @DisplayName("신규 택배사를 생성할 수 있다")
        void shouldCreateNewCarrier() {
            // given
            CarrierCode code = CarrierCode.of("04");
            CarrierName name = CarrierName.of("CJ대한통운");
            String trackingUrl = "https://trace.cjlogistics.com/tracking?invoiceNo={invoiceNumber}";
            Integer displayOrder = 1;

            // when
            Carrier carrier = Carrier.forNew(code, name, trackingUrl, displayOrder, FIXED_TIME);

            // then
            assertNotNull(carrier);
            assertNull(carrier.getId());
            assertTrue(carrier.isNew());
            assertEquals("04", carrier.getCodeValue());
            assertEquals("CJ대한통운", carrier.getNameValue());
            assertEquals(CarrierStatus.ACTIVE, carrier.getStatus());
            assertTrue(carrier.isActive());
            assertTrue(carrier.hasTrackingUrl());
            assertEquals(1, carrier.getDisplayOrder());
        }

        @Test
        @DisplayName("배송 조회 URL 없이 택배사를 생성할 수 있다")
        void shouldCreateCarrierWithoutTrackingUrl() {
            // given
            CarrierCode code = CarrierCode.of("999");
            CarrierName name = CarrierName.of("수동처리");

            // when
            Carrier carrier = Carrier.forNew(code, name, null, null, FIXED_TIME);

            // then
            assertNotNull(carrier);
            assertFalse(carrier.hasTrackingUrl());
            assertNull(carrier.buildTrackingUrl("12345"));
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속화 데이터 복원")
    class Reconstitute {

        @Test
        @DisplayName("영속화된 데이터로 택배사를 복원할 수 있다")
        void shouldReconstituteCarrier() {
            // given
            CarrierId id = CarrierId.of(1L);
            CarrierCode code = CarrierCode.of("05");
            CarrierName name = CarrierName.of("한진택배");
            CarrierStatus status = CarrierStatus.ACTIVE;
            String trackingUrl =
                    "https://www.hanjin.com/kor/CMS/DeliveryMgr/WaybillResult.do?wbl_num={invoiceNumber}";

            // when
            Carrier carrier =
                    Carrier.reconstitute(
                            id, code, name, status, trackingUrl, 2, FIXED_TIME, FIXED_TIME);

            // then
            assertNotNull(carrier);
            assertEquals(1L, carrier.getIdValue());
            assertFalse(carrier.isNew());
            assertEquals("05", carrier.getCodeValue());
            assertEquals("한진택배", carrier.getNameValue());
            assertTrue(carrier.isActive());
        }
    }

    @Nested
    @DisplayName("update() - 택배사 정보 수정")
    class Update {

        @Test
        @DisplayName("택배사 정보를 수정할 수 있다")
        void shouldUpdateCarrierInfo() {
            // given
            Carrier carrier = createDefaultCarrier();
            CarrierName newName = CarrierName.of("CJ대한통운 (변경)");
            String newTrackingUrl = "https://new-trace.cjlogistics.com/{invoiceNumber}";

            // when
            Carrier updated = carrier.update(newName, newTrackingUrl, 10, UPDATED_TIME);

            // then
            assertEquals("CJ대한통운 (변경)", updated.getNameValue());
            assertEquals(newTrackingUrl, updated.getTrackingUrlTemplate());
            assertEquals(10, updated.getDisplayOrder());
            assertEquals(UPDATED_TIME, updated.getUpdatedAt());
            // 코드는 변경되지 않음
            assertEquals("04", updated.getCodeValue());
        }
    }

    @Nested
    @DisplayName("activate() / deactivate() - 상태 변경")
    class StatusChange {

        @Test
        @DisplayName("택배사를 비활성화할 수 있다")
        void shouldDeactivateCarrier() {
            // given
            Carrier carrier = createDefaultCarrier();
            assertTrue(carrier.isActive());

            // when
            Carrier deactivated = carrier.deactivate(UPDATED_TIME);

            // then
            assertFalse(deactivated.isActive());
            assertEquals(CarrierStatus.INACTIVE, deactivated.getStatus());
            assertEquals(UPDATED_TIME, deactivated.getUpdatedAt());
        }

        @Test
        @DisplayName("비활성화된 택배사를 활성화할 수 있다")
        void shouldActivateCarrier() {
            // given
            Carrier carrier = createDefaultCarrier().deactivate(FIXED_TIME);
            assertFalse(carrier.isActive());

            // when
            Carrier activated = carrier.activate(UPDATED_TIME);

            // then
            assertTrue(activated.isActive());
            assertEquals(CarrierStatus.ACTIVE, activated.getStatus());
        }

        @Test
        @DisplayName("이미 활성화된 택배사를 활성화하면 동일한 인스턴스를 반환한다")
        void shouldReturnSameInstanceWhenAlreadyActive() {
            // given
            Carrier carrier = createDefaultCarrier();
            assertTrue(carrier.isActive());

            // when
            Carrier result = carrier.activate(UPDATED_TIME);

            // then
            assertEquals(carrier, result);
        }
    }

    @Nested
    @DisplayName("buildTrackingUrl() - 배송 조회 URL 생성")
    class BuildTrackingUrl {

        @Test
        @DisplayName("운송장 번호로 배송 조회 URL을 생성할 수 있다")
        void shouldBuildTrackingUrl() {
            // given
            Carrier carrier =
                    Carrier.reconstitute(
                            CarrierId.of(1L),
                            CarrierCode.of("04"),
                            CarrierName.of("CJ대한통운"),
                            CarrierStatus.ACTIVE,
                            "https://trace.cjlogistics.com/tracking?invoiceNo={invoiceNumber}",
                            1,
                            FIXED_TIME,
                            FIXED_TIME);

            // when
            String trackingUrl = carrier.buildTrackingUrl("123456789");

            // then
            assertEquals("https://trace.cjlogistics.com/tracking?invoiceNo=123456789", trackingUrl);
        }

        @Test
        @DisplayName("배송 조회 URL 템플릿이 없으면 null을 반환한다")
        void shouldReturnNullWhenNoTemplate() {
            // given
            Carrier carrier =
                    Carrier.forNew(
                            CarrierCode.of("999"), CarrierName.of("수동처리"), null, null, FIXED_TIME);

            // when
            String trackingUrl = carrier.buildTrackingUrl("123456789");

            // then
            assertNull(trackingUrl);
        }
    }

    // ========== Helper Methods ==========

    private Carrier createDefaultCarrier() {
        return Carrier.reconstitute(
                CarrierId.of(1L),
                CarrierCode.of("04"),
                CarrierName.of("CJ대한통운"),
                CarrierStatus.ACTIVE,
                "https://trace.cjlogistics.com/tracking?invoiceNo={invoiceNumber}",
                1,
                FIXED_TIME,
                FIXED_TIME);
    }
}
