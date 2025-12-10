package com.ryuqq.setof.adapter.out.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SoftDeletableEntity 단위 테스트")
class SoftDeletableEntityTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("감사 정보만으로 생성하면 deletedAt은 null이다")
        void create_WithAuditInfoOnly_ShouldHaveNullDeletedAt() {
            // given
            Instant createdAt = Instant.parse("2025-11-26T10:00:00Z");
            Instant updatedAt = Instant.parse("2025-11-26T10:05:00Z");

            // when
            TestSoftDeletableEntity entity = new TestSoftDeletableEntity(createdAt, updatedAt);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("전체 필드로 생성하면 deletedAt이 설정된다")
        void create_WithAllFields_ShouldHaveDeletedAt() {
            // given
            Instant createdAt = Instant.parse("2025-11-26T10:00:00Z");
            Instant updatedAt = Instant.parse("2025-11-26T10:05:00Z");
            Instant deletedAt = Instant.parse("2025-11-26T11:00:00Z");

            // when
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(createdAt, updatedAt, deletedAt);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(entity.getDeletedAt()).isEqualTo(deletedAt);
        }

        @Test
        @DisplayName("deletedAt을 null로 전달하면 활성 상태이다")
        void create_WithNullDeletedAt_ShouldBeActive() {
            // given
            Instant createdAt = Instant.now();
            Instant updatedAt = Instant.now();

            // when
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(createdAt, updatedAt, null);

            // then
            assertThat(entity.getDeletedAt()).isNull();
            assertThat(entity.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("삭제 상태 확인 테스트")
    class DeleteStatusTest {

        @Test
        @DisplayName("deletedAt이 null이면 isDeleted는 false")
        void isDeleted_WhenDeletedAtNull_ShouldReturnFalse() {
            // given
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now());

            // when & then
            assertThat(entity.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("deletedAt이 있으면 isDeleted는 true")
        void isDeleted_WhenDeletedAtNotNull_ShouldReturnTrue() {
            // given
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now(), Instant.now());

            // when & then
            assertThat(entity.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("deletedAt이 null이면 isActive는 true")
        void isActive_WhenDeletedAtNull_ShouldReturnTrue() {
            // given
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now());

            // when & then
            assertThat(entity.isActive()).isTrue();
        }

        @Test
        @DisplayName("deletedAt이 있으면 isActive는 false")
        void isActive_WhenDeletedAtNotNull_ShouldReturnFalse() {
            // given
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now(), Instant.now());

            // when & then
            assertThat(entity.isActive()).isFalse();
        }

        @Test
        @DisplayName("isDeleted와 isActive는 항상 반대 결과")
        void isDeletedAndIsActive_ShouldBeOpposite() {
            // given
            TestSoftDeletableEntity activeEntity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now());
            TestSoftDeletableEntity deletedEntity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now(), Instant.now());

            // when & then
            assertThat(activeEntity.isDeleted()).isNotEqualTo(activeEntity.isActive());
            assertThat(deletedEntity.isDeleted()).isNotEqualTo(deletedEntity.isActive());
        }
    }

    @Nested
    @DisplayName("상속 테스트")
    class InheritanceTest {

        @Test
        @DisplayName("BaseAuditEntity를 상속받아 감사 필드를 사용할 수 있다")
        void inheritance_ShouldProvideAuditFields() {
            // given
            Instant createdAt = Instant.parse("2025-01-01T00:00:00Z");
            Instant updatedAt = Instant.parse("2025-06-01T00:00:00Z");

            // when
            TestSoftDeletableEntity entity = new TestSoftDeletableEntity(createdAt, updatedAt);

            // then
            assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
            assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
        }
    }

    @Nested
    @DisplayName("UTC 일관성 테스트")
    class UtcConsistencyTest {

        @Test
        @DisplayName("deletedAt도 UTC Instant로 저장된다")
        void deletedAt_ShouldBeStoredAsUtcInstant() {
            // given
            Instant deletedAt = Instant.parse("2025-11-26T15:30:00Z");

            // when
            TestSoftDeletableEntity entity =
                    new TestSoftDeletableEntity(Instant.now(), Instant.now(), deletedAt);

            // then
            assertThat(entity.getDeletedAt()).isEqualTo(deletedAt);
            assertThat(entity.getDeletedAt().getEpochSecond())
                    .isEqualTo(deletedAt.getEpochSecond());
        }
    }

    /**
     * 테스트용 구체 클래스
     *
     * <p>SoftDeletableEntity는 추상 클래스이므로 테스트용 구체 클래스 필요
     */
    private static class TestSoftDeletableEntity extends SoftDeletableEntity {

        TestSoftDeletableEntity(Instant createdAt, Instant updatedAt) {
            super(createdAt, updatedAt, null);
        }

        TestSoftDeletableEntity(Instant createdAt, Instant updatedAt, Instant deletedAt) {
            super(createdAt, updatedAt, deletedAt);
        }
    }
}
