package com.ryuqq.setof.domain.productgroup.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@Tag("unit")
@DisplayName("ProductGroup VO 테스트")
class ProductGroupVoTest {

    @Nested
    @DisplayName("ProductGroupName 테스트")
    class ProductGroupNameTest {

        @Test
        @DisplayName("유효한 상품그룹명을 생성한다")
        void createValidProductGroupName() {
            var name = ProductGroupName.of("테스트 상품그룹");
            assertThat(name.value()).isEqualTo("테스트 상품그룹");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var name = ProductGroupName.of("  테스트 상품그룹  ");
            assertThat(name.value()).isEqualTo("테스트 상품그룹");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> ProductGroupName.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("상품그룹명");
        }

        @Test
        @DisplayName("200자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longName = "가".repeat(201);
            assertThatThrownBy(() -> ProductGroupName.of(longName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("200");
        }

        @Test
        @DisplayName("200자는 유효하다")
        void exactMaxLengthIsValid() {
            String maxLengthName = "가".repeat(200);
            var name = ProductGroupName.of(maxLengthName);
            assertThat(name.value()).hasSize(200);
        }

        @Test
        @DisplayName("동일한 값의 ProductGroupName은 동등하다")
        void equalityTest() {
            var name1 = ProductGroupName.of("테스트");
            var name2 = ProductGroupName.of("테스트");
            assertThat(name1).isEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("ImageUrl 테스트")
    class ImageUrlTest {

        @Test
        @DisplayName("유효한 이미지 URL을 생성한다")
        void createValidImageUrl() {
            var url = ImageUrl.of("https://example.com/image.png");
            assertThat(url.value()).isEqualTo("https://example.com/image.png");
        }

        @Test
        @DisplayName("공백은 트림된다")
        void trimWhitespace() {
            var url = ImageUrl.of("  https://example.com/image.png  ");
            assertThat(url.value()).isEqualTo("https://example.com/image.png");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("null 또는 빈 값이면 예외가 발생한다")
        void throwExceptionForNullOrEmpty(String value) {
            assertThatThrownBy(() -> ImageUrl.of(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미지 URL");
        }

        @Test
        @DisplayName("500자 초과 시 예외가 발생한다")
        void throwExceptionForTooLong() {
            String longUrl = "https://example.com/" + "a".repeat(490);
            assertThatThrownBy(() -> ImageUrl.of(longUrl))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("500");
        }

        @Test
        @DisplayName("500자는 유효하다")
        void exactMaxLengthIsValid() {
            String maxLengthUrl = "https://" + "a".repeat(492);
            var url = ImageUrl.of(maxLengthUrl);
            assertThat(url.value()).hasSize(500);
        }

        @Test
        @DisplayName("동일한 값의 ImageUrl은 동등하다")
        void equalityTest() {
            var url1 = ImageUrl.of("https://example.com/image.png");
            var url2 = ImageUrl.of("https://example.com/image.png");
            assertThat(url1).isEqualTo(url2);
        }
    }

    @Nested
    @DisplayName("OptionType 테스트")
    class OptionTypeTest {

        @Nested
        @DisplayName("fromLegacy() - 레거시 매핑")
        class FromLegacyTest {

            @Test
            @DisplayName("'SINGLE'을 SINGLE로 변환한다")
            void mapsSingle() {
                assertThat(OptionType.fromLegacy("SINGLE")).isEqualTo(OptionType.SINGLE);
            }

            @Test
            @DisplayName("'COMBINATION'을 COMBINATION으로 변환한다")
            void mapsCombination() {
                assertThat(OptionType.fromLegacy("COMBINATION")).isEqualTo(OptionType.COMBINATION);
            }

            @Test
            @DisplayName("소문자도 매핑된다")
            void mapsLowerCase() {
                assertThat(OptionType.fromLegacy("single")).isEqualTo(OptionType.SINGLE);
                assertThat(OptionType.fromLegacy("combination")).isEqualTo(OptionType.COMBINATION);
            }

            @Test
            @DisplayName("null이면 NONE을 반환한다")
            void returnsNoneForNull() {
                assertThat(OptionType.fromLegacy(null)).isEqualTo(OptionType.NONE);
            }

            @Test
            @DisplayName("빈 값이면 NONE을 반환한다")
            void returnsNoneForEmpty() {
                assertThat(OptionType.fromLegacy("")).isEqualTo(OptionType.NONE);
                assertThat(OptionType.fromLegacy("  ")).isEqualTo(OptionType.NONE);
            }

            @Test
            @DisplayName("알 수 없는 값이면 NONE을 반환한다")
            void returnsNoneForUnknown() {
                assertThat(OptionType.fromLegacy("UNKNOWN")).isEqualTo(OptionType.NONE);
            }
        }

        @Nested
        @DisplayName("hasOptions() 테스트")
        class HasOptionsTest {

            @Test
            @DisplayName("NONE은 false를 반환한다")
            void noneReturnsFalse() {
                assertThat(OptionType.NONE.hasOptions()).isFalse();
            }

            @Test
            @DisplayName("SINGLE은 true를 반환한다")
            void singleReturnsTrue() {
                assertThat(OptionType.SINGLE.hasOptions()).isTrue();
            }

            @Test
            @DisplayName("COMBINATION은 true를 반환한다")
            void combinationReturnsTrue() {
                assertThat(OptionType.COMBINATION.hasOptions()).isTrue();
            }
        }

        @Nested
        @DisplayName("isCombination() 테스트")
        class IsCombinationTest {

            @Test
            @DisplayName("COMBINATION은 true를 반환한다")
            void combinationReturnsTrue() {
                assertThat(OptionType.COMBINATION.isCombination()).isTrue();
            }

            @Test
            @DisplayName("SINGLE은 false를 반환한다")
            void singleReturnsFalse() {
                assertThat(OptionType.SINGLE.isCombination()).isFalse();
            }

            @Test
            @DisplayName("NONE은 false를 반환한다")
            void noneReturnsFalse() {
                assertThat(OptionType.NONE.isCombination()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("ProductGroupStatus 테스트")
    class ProductGroupStatusTest {

        @Nested
        @DisplayName("canActivate() 테스트")
        class CanActivateTest {

            @Test
            @DisplayName("DRAFT에서 활성화할 수 있다")
            void draftCanActivate() {
                assertThat(ProductGroupStatus.DRAFT.canActivate()).isTrue();
            }

            @Test
            @DisplayName("INACTIVE에서 활성화할 수 있다")
            void inactiveCanActivate() {
                assertThat(ProductGroupStatus.INACTIVE.canActivate()).isTrue();
            }

            @Test
            @DisplayName("SOLDOUT에서 활성화할 수 있다")
            void soldOutCanActivate() {
                assertThat(ProductGroupStatus.SOLDOUT.canActivate()).isTrue();
            }

            @Test
            @DisplayName("ACTIVE에서는 활성화할 수 없다")
            void activeCannotActivate() {
                assertThat(ProductGroupStatus.ACTIVE.canActivate()).isFalse();
            }

            @Test
            @DisplayName("DELETED에서는 활성화할 수 없다")
            void deletedCannotActivate() {
                assertThat(ProductGroupStatus.DELETED.canActivate()).isFalse();
            }
        }

        @Nested
        @DisplayName("canDeactivate() 테스트")
        class CanDeactivateTest {

            @Test
            @DisplayName("ACTIVE에서 비활성화할 수 있다")
            void activeCanDeactivate() {
                assertThat(ProductGroupStatus.ACTIVE.canDeactivate()).isTrue();
            }

            @Test
            @DisplayName("DRAFT에서는 비활성화할 수 없다")
            void draftCannotDeactivate() {
                assertThat(ProductGroupStatus.DRAFT.canDeactivate()).isFalse();
            }

            @Test
            @DisplayName("INACTIVE에서는 비활성화할 수 없다")
            void inactiveCannotDeactivate() {
                assertThat(ProductGroupStatus.INACTIVE.canDeactivate()).isFalse();
            }

            @Test
            @DisplayName("SOLDOUT에서는 비활성화할 수 없다")
            void soldOutCannotDeactivate() {
                assertThat(ProductGroupStatus.SOLDOUT.canDeactivate()).isFalse();
            }

            @Test
            @DisplayName("DELETED에서는 비활성화할 수 없다")
            void deletedCannotDeactivate() {
                assertThat(ProductGroupStatus.DELETED.canDeactivate()).isFalse();
            }
        }

        @Nested
        @DisplayName("canMarkSoldOut() 테스트")
        class CanMarkSoldOutTest {

            @Test
            @DisplayName("ACTIVE에서 품절 처리할 수 있다")
            void activeCanMarkSoldOut() {
                assertThat(ProductGroupStatus.ACTIVE.canMarkSoldOut()).isTrue();
            }

            @Test
            @DisplayName("DRAFT에서는 품절 처리할 수 없다")
            void draftCannotMarkSoldOut() {
                assertThat(ProductGroupStatus.DRAFT.canMarkSoldOut()).isFalse();
            }

            @Test
            @DisplayName("INACTIVE에서는 품절 처리할 수 없다")
            void inactiveCannotMarkSoldOut() {
                assertThat(ProductGroupStatus.INACTIVE.canMarkSoldOut()).isFalse();
            }

            @Test
            @DisplayName("SOLDOUT에서는 품절 처리할 수 없다")
            void soldOutCannotMarkSoldOut() {
                assertThat(ProductGroupStatus.SOLDOUT.canMarkSoldOut()).isFalse();
            }

            @Test
            @DisplayName("DELETED에서는 품절 처리할 수 없다")
            void deletedCannotMarkSoldOut() {
                assertThat(ProductGroupStatus.DELETED.canMarkSoldOut()).isFalse();
            }
        }

        @Nested
        @DisplayName("canDelete() 테스트")
        class CanDeleteTest {

            @Test
            @DisplayName("DRAFT에서 삭제할 수 있다")
            void draftCanDelete() {
                assertThat(ProductGroupStatus.DRAFT.canDelete()).isTrue();
            }

            @Test
            @DisplayName("ACTIVE에서 삭제할 수 있다")
            void activeCanDelete() {
                assertThat(ProductGroupStatus.ACTIVE.canDelete()).isTrue();
            }

            @Test
            @DisplayName("INACTIVE에서 삭제할 수 있다")
            void inactiveCanDelete() {
                assertThat(ProductGroupStatus.INACTIVE.canDelete()).isTrue();
            }

            @Test
            @DisplayName("SOLDOUT에서 삭제할 수 있다")
            void soldOutCanDelete() {
                assertThat(ProductGroupStatus.SOLDOUT.canDelete()).isTrue();
            }

            @Test
            @DisplayName("DELETED에서는 삭제할 수 없다")
            void deletedCannotDelete() {
                assertThat(ProductGroupStatus.DELETED.canDelete()).isFalse();
            }
        }

        @Nested
        @DisplayName("상태 확인 메서드 테스트")
        class StatusCheckTest {

            @Test
            @DisplayName("isDisplayed() - ACTIVE이면 true를 반환한다")
            void isDisplayedReturnsTrue() {
                assertThat(ProductGroupStatus.ACTIVE.isDisplayed()).isTrue();
            }

            @Test
            @DisplayName("isDisplayed() - ACTIVE가 아니면 false를 반환한다")
            void isDisplayedReturnsFalse() {
                assertThat(ProductGroupStatus.DRAFT.isDisplayed()).isFalse();
                assertThat(ProductGroupStatus.INACTIVE.isDisplayed()).isFalse();
                assertThat(ProductGroupStatus.SOLDOUT.isDisplayed()).isFalse();
                assertThat(ProductGroupStatus.DELETED.isDisplayed()).isFalse();
            }

            @Test
            @DisplayName("isSoldOut() - SOLDOUT이면 true를 반환한다")
            void isSoldOutReturnsTrue() {
                assertThat(ProductGroupStatus.SOLDOUT.isSoldOut()).isTrue();
            }

            @Test
            @DisplayName("isSoldOut() - SOLDOUT이 아니면 false를 반환한다")
            void isSoldOutReturnsFalse() {
                assertThat(ProductGroupStatus.DRAFT.isSoldOut()).isFalse();
                assertThat(ProductGroupStatus.ACTIVE.isSoldOut()).isFalse();
                assertThat(ProductGroupStatus.INACTIVE.isSoldOut()).isFalse();
                assertThat(ProductGroupStatus.DELETED.isSoldOut()).isFalse();
            }

            @Test
            @DisplayName("isDeleted() - DELETED이면 true를 반환한다")
            void isDeletedReturnsTrue() {
                assertThat(ProductGroupStatus.DELETED.isDeleted()).isTrue();
            }

            @Test
            @DisplayName("isDeleted() - DELETED가 아니면 false를 반환한다")
            void isDeletedReturnsFalse() {
                assertThat(ProductGroupStatus.DRAFT.isDeleted()).isFalse();
                assertThat(ProductGroupStatus.ACTIVE.isDeleted()).isFalse();
                assertThat(ProductGroupStatus.INACTIVE.isDeleted()).isFalse();
                assertThat(ProductGroupStatus.SOLDOUT.isDeleted()).isFalse();
            }
        }

        @Nested
        @DisplayName("fromLegacyFlags() - 레거시 플래그 매핑")
        class FromLegacyFlagsTest {

            @Test
            @DisplayName("deleteYn=Y이면 DELETED를 반환한다")
            void returnsDeletedWhenDeleteYnIsY() {
                assertThat(ProductGroupStatus.fromLegacyFlags("Y", "N", "Y"))
                        .isEqualTo(ProductGroupStatus.DELETED);
            }

            @Test
            @DisplayName("soldOutYn=Y이면 SOLDOUT을 반환한다")
            void returnsSoldOutWhenSoldOutYnIsY() {
                assertThat(ProductGroupStatus.fromLegacyFlags("N", "Y", "Y"))
                        .isEqualTo(ProductGroupStatus.SOLDOUT);
            }

            @Test
            @DisplayName("displayYn=N이면 INACTIVE를 반환한다")
            void returnsInactiveWhenDisplayYnIsN() {
                assertThat(ProductGroupStatus.fromLegacyFlags("N", "N", "N"))
                        .isEqualTo(ProductGroupStatus.INACTIVE);
            }

            @Test
            @DisplayName("기본 조건이면 ACTIVE를 반환한다")
            void returnsActiveByDefault() {
                assertThat(ProductGroupStatus.fromLegacyFlags("N", "N", "Y"))
                        .isEqualTo(ProductGroupStatus.ACTIVE);
            }

            @Test
            @DisplayName("deleteYn이 우선순위가 가장 높다")
            void deleteYnHasHighestPriority() {
                assertThat(ProductGroupStatus.fromLegacyFlags("Y", "Y", "N"))
                        .isEqualTo(ProductGroupStatus.DELETED);
            }

            @Test
            @DisplayName("soldOutYn이 displayYn보다 우선순위가 높다")
            void soldOutYnHigherThanDisplayYn() {
                assertThat(ProductGroupStatus.fromLegacyFlags("N", "Y", "N"))
                        .isEqualTo(ProductGroupStatus.SOLDOUT);
            }

            @Test
            @DisplayName("소문자 y도 매핑된다")
            void lowerCaseAlsoWorks() {
                assertThat(ProductGroupStatus.fromLegacyFlags("y", "n", "y"))
                        .isEqualTo(ProductGroupStatus.DELETED);
            }
        }
    }

    @Nested
    @DisplayName("ImageType 테스트")
    class ImageTypeTest {

        @Test
        @DisplayName("isThumbnail() - THUMBNAIL이면 true를 반환한다")
        void thumbnailReturnsTrue() {
            assertThat(ImageType.THUMBNAIL.isThumbnail()).isTrue();
        }

        @Test
        @DisplayName("isThumbnail() - DETAIL이면 false를 반환한다")
        void detailReturnsFalse() {
            assertThat(ImageType.DETAIL.isThumbnail()).isFalse();
        }

        @Test
        @DisplayName("isDetail() - DETAIL이면 true를 반환한다")
        void detailReturnsTrue() {
            assertThat(ImageType.DETAIL.isDetail()).isTrue();
        }

        @Test
        @DisplayName("isDetail() - THUMBNAIL이면 false를 반환한다")
        void thumbnailReturnsFalseForIsDetail() {
            assertThat(ImageType.THUMBNAIL.isDetail()).isFalse();
        }
    }
}
