package com.ryuqq.setof.adapter.out.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.entity.BaseAuditEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BaseAuditEntity 단위 테스트")
class BaseAuditEntityTest {

    @Nested
    @DisplayName("생성자 테스트")
    class ConstructorTest {

        @Test
        @DisplayName("감사 정보 생성자로 createdAt과 updatedAt을 설정할 수 있다")
        void constructor_WithAuditFields_ShouldSetFields() {
            // given
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2025-06-01T12:30:45Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(createdAt, updatedAt);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("기본 생성자로 생성 시 감사 필드는 null이다")
        void defaultConstructor_ShouldHaveNullFields() {
            // when
            TestAuditEntity entity = new TestAuditEntity();

            // then
            assertThat(entity.getCreatedAt()).isNull();
            assertThat(entity.getUpdatedAt()).isNull();
        }

        @Test
        @DisplayName("createdAt과 updatedAt이 동일한 시각으로 설정될 수 있다")
        void constructor_WithSameTimestamps_ShouldSetBothFields() {
            // given
            Instant sameTime = Instant.parse("2025-11-26T10:00:00Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(sameTime, sameTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(sameTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(sameTime);
        }
    }

    @Nested
    @DisplayName("Getter 테스트")
    class GetterTest {

        @Test
        @DisplayName("getCreatedAt()은 생성 일시를 반환한다")
        void getCreatedAt_ShouldReturnCreatedAt() {
            // given
            Instant createdAt = Instant.parse("2025-03-15T09:30:00Z");
            TestAuditEntity entity = new TestAuditEntity(createdAt, Instant.now());

            // when
            Instant result = entity.getCreatedAt();

            // then
            assertThat(result).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("getUpdatedAt()은 수정 일시를 반환한다")
        void getUpdatedAt_ShouldReturnUpdatedAt() {
            // given
            Instant updatedAt = Instant.parse("2025-07-20T14:45:00Z");
            TestAuditEntity entity = new TestAuditEntity(Instant.now(), updatedAt);

            // when
            Instant result = entity.getUpdatedAt();

            // then
            assertThat(result).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("updatedAt은 createdAt보다 나중일 수 있다")
        void updatedAt_CanBeAfterCreatedAt() {
            // given
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2025-12-31T23:59:59Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(createdAt, updatedAt);

            // then
            assertThat(entity.getUpdatedAt()).isAfter(entity.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("시간 정밀도 테스트")
    class TimestampPrecisionTest {

        @Test
        @DisplayName("나노초 정밀도의 시간을 저장할 수 있다")
        void constructor_WithNanoseconds_ShouldPreservePrecision() {
            // given
            Instant preciseTime = Instant.parse("2025-11-26T10:30:45.123456789Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(preciseTime, preciseTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(preciseTime);
            assertThat(entity.getCreatedAt().getNano()).isEqualTo(123456789);
        }

        @Test
        @DisplayName("자정 시각(00:00:00 UTC)을 저장할 수 있다")
        void constructor_WithMidnight_ShouldStoreCorrectly() {
            // given
            Instant midnight = Instant.parse("2025-11-26T00:00:00Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(midnight, midnight);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(midnight);
            assertThat(entity.getCreatedAt().truncatedTo(ChronoUnit.DAYS)).isEqualTo(midnight);
        }

        @Test
        @DisplayName("23:59:59 UTC 시각을 저장할 수 있다")
        void constructor_WithEndOfDay_ShouldStoreCorrectly() {
            // given
            Instant endOfDay = Instant.parse("2025-11-26T23:59:59Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(endOfDay, endOfDay);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(endOfDay);
        }
    }

    @Nested
    @DisplayName("날짜 경계 테스트")
    class DateBoundaryTest {

        @Test
        @DisplayName("연초(1월 1일 UTC)를 저장할 수 있다")
        void constructor_WithStartOfYear_ShouldStoreCorrectly() {
            // given
            Instant startOfYear = Instant.parse("2025-01-01T00:00:00Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(startOfYear, startOfYear);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(startOfYear);
        }

        @Test
        @DisplayName("연말(12월 31일 UTC)을 저장할 수 있다")
        void constructor_WithEndOfYear_ShouldStoreCorrectly() {
            // given
            Instant endOfYear = Instant.parse("2025-12-31T23:59:59Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(endOfYear, endOfYear);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(endOfYear);
        }

        @Test
        @DisplayName("윤년(2월 29일 UTC)을 저장할 수 있다")
        void constructor_WithLeapYear_ShouldStoreCorrectly() {
            // given - 2024년은 윤년
            Instant leapDay = Instant.parse("2024-02-29T12:00:00Z");

            // when
            TestAuditEntity entity = new TestAuditEntity(leapDay, leapDay);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(leapDay);
        }
    }

    @Nested
    @DisplayName("UTC 일관성 테스트")
    class UtcConsistencyTest {

        @Test
        @DisplayName("Instant는 타임존 독립적으로 저장된다")
        void instant_ShouldBeTimezoneIndependent() {
            // given
            Instant utcTime = Instant.now();

            // when
            TestAuditEntity entity = new TestAuditEntity(utcTime, utcTime);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(utcTime);
            assertThat(entity.getUpdatedAt()).isEqualTo(utcTime);
        }

        @Test
        @DisplayName("Epoch 시간 기준으로 정확하게 저장된다")
        void instant_ShouldPreserveEpochSeconds() {
            // given
            long epochSeconds = 1735689600L; // 2025-01-01T00:00:00Z
            Instant instant = Instant.ofEpochSecond(epochSeconds);

            // when
            TestAuditEntity entity = new TestAuditEntity(instant, instant);

            // then
            assertThat(entity.getCreatedAt().getEpochSecond()).isEqualTo(epochSeconds);
        }
    }

    // ==================== Test Helper Class ====================

    /**
     * BaseAuditEntity를 테스트하기 위한 구체 클래스.
     *
     * <p>BaseAuditEntity는 추상 클래스이므로 테스트를 위해 구체 클래스가 필요합니다.
     */
    private static class TestAuditEntity extends BaseAuditEntity {

        TestAuditEntity() {
            super();
        }

        TestAuditEntity(Instant createdAt, Instant updatedAt) {
            super(createdAt, updatedAt);
        }
    }
}
