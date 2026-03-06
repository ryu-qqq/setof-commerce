package com.ryuqq.setof.domain.productnotice.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.setof.commerce.domain.productnotice.ProductNoticeFixtures;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductNoticeUpdateData Value Object 테스트")
class ProductNoticeUpdateDataTest {

    @Nested
    @DisplayName("of() - 생성")
    class OfTest {

        @Test
        @DisplayName("엔트리 목록과 수정 시각으로 생성한다")
        void createWithEntriesAndUpdatedAt() {
            // given
            List<ProductNoticeEntry> entries = ProductNoticeFixtures.defaultEntries();
            Instant updatedAt = Instant.now();

            // when
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(entries, updatedAt);

            // then
            assertThat(data.entries()).hasSize(entries.size());
            assertThat(data.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("빈 엔트리 목록으로 생성한다")
        void createWithEmptyEntries() {
            // given
            List<ProductNoticeEntry> entries = List.of();
            Instant updatedAt = Instant.now();

            // when
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(entries, updatedAt);

            // then
            assertThat(data.entries()).isEmpty();
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTest {

        @Test
        @DisplayName("entries()로 반환된 목록은 수정할 수 없다")
        void returnedEntriesListIsUnmodifiable() {
            // given
            List<ProductNoticeEntry> entries =
                    new ArrayList<>(ProductNoticeFixtures.defaultEntries());
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(entries, Instant.now());

            // when & then
            assertThatThrownBy(() -> data.entries().add(ProductNoticeFixtures.defaultNoticeEntry()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("원본 목록을 수정해도 ProductNoticeUpdateData의 엔트리가 변경되지 않는다")
        void originalListModificationDoesNotAffectData() {
            // given
            List<ProductNoticeEntry> original =
                    new ArrayList<>(ProductNoticeFixtures.defaultEntries());
            int originalSize = original.size();
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(original, Instant.now());

            // when
            original.add(ProductNoticeFixtures.defaultNoticeEntry());

            // then
            assertThat(data.entries()).hasSize(originalSize);
        }
    }

    @Nested
    @DisplayName("entries() - 엔트리 목록 조회")
    class EntriesTest {

        @Test
        @DisplayName("저장한 엔트리 목록을 반환한다")
        void returnsSavedEntries() {
            // given
            List<ProductNoticeEntry> entries = ProductNoticeFixtures.reconstitutedDefaultEntries();
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(entries, Instant.now());

            // when
            List<ProductNoticeEntry> result = data.entries();

            // then
            assertThat(result).hasSize(entries.size());
        }
    }

    @Nested
    @DisplayName("updatedAt() - 수정 시각 조회")
    class UpdatedAtTest {

        @Test
        @DisplayName("저장한 수정 시각을 반환한다")
        void returnsSavedUpdatedAt() {
            // given
            Instant updatedAt = Instant.parse("2025-01-01T00:00:00Z");
            ProductNoticeUpdateData data = ProductNoticeUpdateData.of(List.of(), updatedAt);

            // when
            Instant result = data.updatedAt();

            // then
            assertThat(result).isEqualTo(updatedAt);
        }
    }
}
