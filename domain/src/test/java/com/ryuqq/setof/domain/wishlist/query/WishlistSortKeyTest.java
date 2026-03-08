package com.ryuqq.setof.domain.wishlist.query;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.SortKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("WishlistSortKey 테스트")
class WishlistSortKeyTest {

    @Nested
    @DisplayName("SortKey 인터페이스 구현 테스트")
    class SortKeyInterfaceTest {

        @Test
        @DisplayName("SortKey 인터페이스를 구현한다")
        void implementsSortKey() {
            // then
            assertThat(WishlistSortKey.CREATED_AT).isInstanceOf(SortKey.class);
        }

        @Test
        @DisplayName("CREATED_AT의 fieldName은 'createdAt'이다")
        void createdAtFieldName() {
            // then
            assertThat(WishlistSortKey.CREATED_AT.fieldName()).isEqualTo("createdAt");
        }
    }

    @Nested
    @DisplayName("defaultKey() 테스트")
    class DefaultKeyTest {

        @Test
        @DisplayName("defaultKey()는 CREATED_AT을 반환한다")
        void defaultKeyReturnsCreatedAt() {
            // when
            WishlistSortKey defaultKey = WishlistSortKey.defaultKey();

            // then
            assertThat(defaultKey).isEqualTo(WishlistSortKey.CREATED_AT);
        }
    }

    @Nested
    @DisplayName("enum 값 테스트")
    class EnumValuesTest {

        @Test
        @DisplayName("모든 정렬 키 값이 존재한다")
        void allValuesExist() {
            // then
            assertThat(WishlistSortKey.values()).containsExactly(WishlistSortKey.CREATED_AT);
        }

        @Test
        @DisplayName("valueOf로 enum 값을 조회한다")
        void valueOfReturnsCorrectEnum() {
            // then
            assertThat(WishlistSortKey.valueOf("CREATED_AT")).isEqualTo(WishlistSortKey.CREATED_AT);
        }
    }
}
