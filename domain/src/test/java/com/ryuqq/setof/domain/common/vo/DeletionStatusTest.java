package com.ryuqq.setof.domain.common.vo;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DeletionStatus Value Object 테스트")
class DeletionStatusTest {

    @Nested
    @DisplayName("active() - 활성 상태 생성")
    class ActiveTest {

        @Test
        @DisplayName("활성 상태를 생성한다")
        void createActiveStatus() {
            // when
            DeletionStatus status = DeletionStatus.active();

            // then
            assertThat(status.isActive()).isTrue();
            assertThat(status.isDeleted()).isFalse();
            assertThat(status.deletedAt()).isNull();
        }

        @Test
        @DisplayName("active()는 항상 같은 인스턴스를 반환한다 (캐싱)")
        void activeReturnsCachedInstance() {
            // when
            DeletionStatus status1 = DeletionStatus.active();
            DeletionStatus status2 = DeletionStatus.active();

            // then
            assertThat(status1).isSameAs(status2);
        }
    }

    @Nested
    @DisplayName("deletedAt() - 삭제 상태 생성")
    class DeletedAtTest {

        @Test
        @DisplayName("삭제 상태를 생성한다")
        void createDeletedStatus() {
            // given
            Instant now = Instant.now();

            // when
            DeletionStatus status = DeletionStatus.deletedAt(now);

            // then
            assertThat(status.isDeleted()).isTrue();
            assertThat(status.isActive()).isFalse();
            assertThat(status.deletedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("null 시간으로 생성하면 예외가 발생한다")
        void deletedAtWithNullThrowsException() {
            // when & then
            assertThatThrownBy(() -> DeletionStatus.deletedAt(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("null");
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("삭제된 상태를 복원한다")
        void reconstituteDeletedStatus() {
            // given
            Instant deletedAt = Instant.now();

            // when
            DeletionStatus status = DeletionStatus.reconstitute(true, deletedAt);

            // then
            assertThat(status.isDeleted()).isTrue();
            assertThat(status.deletedAt()).isEqualTo(deletedAt);
        }

        @Test
        @DisplayName("활성 상태를 복원한다")
        void reconstituteActiveStatus() {
            // when
            DeletionStatus status = DeletionStatus.reconstitute(false, null);

            // then
            assertThat(status.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("생성자 유효성 검증")
    class ValidationTest {

        @Test
        @DisplayName("deleted=true이고 deletedAt=null이면 예외가 발생한다")
        void deletedWithoutTimestampThrowsException() {
            // when & then
            assertThatThrownBy(() -> new DeletionStatus(true, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("deletedAt must not be null when deleted is true");
        }

        @Test
        @DisplayName("deleted=false이고 deletedAt이 존재하면 예외가 발생한다")
        void activeWithTimestampThrowsException() {
            // given
            Instant now = Instant.now();

            // when & then
            assertThatThrownBy(() -> new DeletionStatus(false, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("deletedAt must be null when deleted is false");
        }
    }

    @Nested
    @DisplayName("동등성 테스트")
    class EqualityTest {

        @Test
        @DisplayName("같은 상태의 DeletionStatus는 동등하다")
        void sameStatusAreEqual() {
            // given
            DeletionStatus status1 = DeletionStatus.active();
            DeletionStatus status2 = DeletionStatus.active();

            // then
            assertThat(status1).isEqualTo(status2);
        }

        @Test
        @DisplayName("같은 삭제 시간의 DeletionStatus는 동등하다")
        void sameDeletedAtAreEqual() {
            // given
            Instant now = Instant.now();
            DeletionStatus status1 = DeletionStatus.deletedAt(now);
            DeletionStatus status2 = DeletionStatus.deletedAt(now);

            // then
            assertThat(status1).isEqualTo(status2);
        }
    }
}
