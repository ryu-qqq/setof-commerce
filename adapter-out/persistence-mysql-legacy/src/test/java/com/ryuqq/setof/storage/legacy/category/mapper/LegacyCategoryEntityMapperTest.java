package com.ryuqq.setof.storage.legacy.category.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.vo.CategoryType;
import com.ryuqq.setof.domain.category.vo.TargetGroup;
import com.ryuqq.setof.storage.legacy.category.LegacyCategoryEntityFixtures;
import com.ryuqq.setof.storage.legacy.category.dto.LegacyCategoryTreeDto;
import com.ryuqq.setof.storage.legacy.category.entity.LegacyCategoryEntity;
import com.ryuqq.setof.storage.legacy.common.Yn;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * LegacyCategoryEntityMapper 단위 테스트.
 *
 * <p>레거시 카테고리 엔티티 → 도메인 변환 로직을 검증합니다.
 */
@DisplayName("레거시 카테고리 엔티티 Mapper 테스트")
@Tag("unit")
class LegacyCategoryEntityMapperTest {

    private LegacyCategoryEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LegacyCategoryEntityMapper();
    }

    @Nested
    @DisplayName("toDomain(Entity) 메서드 테스트")
    class ToDomainFromEntityTest {

        @Test
        @DisplayName("엔티티를 도메인으로 정상 변환")
        void shouldConvertEntityToDomain() {
            // given
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder()
                            .id(100L)
                            .categoryName("상의")
                            .categoryDepth(1)
                            .parentCategoryId(0L)
                            .displayName("상의")
                            .displayYn(Yn.Y)
                            .targetGroup(TargetGroup.MALE)
                            .categoryType(CategoryType.CLOTHING)
                            .path("/100")
                            .insertDate(LocalDateTime.of(2024, 1, 1, 10, 0))
                            .updateDate(LocalDateTime.of(2024, 1, 2, 10, 0))
                            .build();

            // when
            Category category = mapper.toDomain(entity);

            // then
            assertThat(category).isNotNull();
            assertThat(category.idValue()).isEqualTo(100L);
            assertThat(category.categoryNameValue()).isEqualTo("상의");
            assertThat(category.categoryDepthValue()).isEqualTo(1);
            assertThat(category.parentCategoryIdValue()).isEqualTo(0L);
            assertThat(category.displayNameValue()).isEqualTo("상의");
            assertThat(category.isDisplayed()).isTrue();
            assertThat(category.targetGroup()).isEqualTo(TargetGroup.MALE);
            assertThat(category.categoryType()).isEqualTo(CategoryType.CLOTHING);
            assertThat(category.pathValue()).isEqualTo("/100");
        }

        @Test
        @DisplayName("displayYn Y를 boolean true로 변환")
        void shouldConvertDisplayYnYToTrue() {
            // given
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder().displayYn(Yn.Y).build();

            // when
            Category category = mapper.toDomain(entity);

            // then
            assertThat(category.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("displayYn N을 boolean false로 변환")
        void shouldConvertDisplayYnNToFalse() {
            // given
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder().displayYn(Yn.N).build();

            // when
            Category category = mapper.toDomain(entity);

            // then
            assertThat(category.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("insertDate가 null이면 현재 시간으로 대체")
        void shouldUseCurrentTimeWhenInsertDateIsNull() {
            // given
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder().insertDate(null).build();

            // when
            Category category = mapper.toDomain(entity);

            // then
            assertThat(category.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updateDate가 null이면 현재 시간으로 대체")
        void shouldUseCurrentTimeWhenUpdateDateIsNull() {
            // given
            LegacyCategoryEntity entity =
                    LegacyCategoryEntityFixtures.builder().updateDate(null).build();

            // when
            Category category = mapper.toDomain(entity);

            // then
            assertThat(category.updatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toDomain(TreeDto) 메서드 테스트")
    class ToDomainFromTreeDtoTest {

        @Test
        @DisplayName("TreeDto를 도메인으로 정상 변환")
        void shouldConvertTreeDtoToDomain() {
            // given
            LegacyCategoryTreeDto dto =
                    new LegacyCategoryTreeDto(
                            200L,
                            "카테고리A",
                            2,
                            100L,
                            "카테고리A 표시명",
                            Yn.Y,
                            TargetGroup.FEMALE,
                            CategoryType.BAG,
                            "/100/200",
                            LocalDateTime.of(2024, 1, 1, 10, 0),
                            LocalDateTime.of(2024, 1, 2, 10, 0));

            // when
            Category category = mapper.toDomain(dto);

            // then
            assertThat(category).isNotNull();
            assertThat(category.idValue()).isEqualTo(200L);
            assertThat(category.categoryNameValue()).isEqualTo("카테고리A");
            assertThat(category.categoryDepthValue()).isEqualTo(2);
            assertThat(category.parentCategoryIdValue()).isEqualTo(100L);
            assertThat(category.displayNameValue()).isEqualTo("카테고리A 표시명");
            assertThat(category.isDisplayed()).isTrue();
            assertThat(category.targetGroup()).isEqualTo(TargetGroup.FEMALE);
            assertThat(category.categoryType()).isEqualTo(CategoryType.BAG);
            assertThat(category.pathValue()).isEqualTo("/100/200");
        }

        @Test
        @DisplayName("TreeDto의 displayYn이 null이면 false로 변환")
        void shouldConvertNullDisplayYnToFalse() {
            // given
            LegacyCategoryTreeDto dto =
                    new LegacyCategoryTreeDto(
                            300L,
                            "카테고리B",
                            1,
                            0L,
                            "카테고리B 표시명",
                            null,
                            TargetGroup.KIDS,
                            CategoryType.CLOTHING,
                            "/300",
                            LocalDateTime.of(2024, 1, 1, 10, 0),
                            LocalDateTime.of(2024, 1, 2, 10, 0));

            // when
            Category category = mapper.toDomain(dto);

            // then
            assertThat(category.isDisplayed()).isFalse();
        }

        @Test
        @DisplayName("TreeDto의 insertDate가 null이면 현재 시간으로 대체")
        void shouldUseCurrentTimeForNullInsertDate() {
            // given
            LegacyCategoryTreeDto dto =
                    new LegacyCategoryTreeDto(
                            400L,
                            "카테고리C",
                            1,
                            0L,
                            "카테고리C 표시명",
                            Yn.Y,
                            TargetGroup.LIFE,
                            CategoryType.ACC,
                            "/400",
                            null,
                            LocalDateTime.of(2024, 1, 2, 10, 0));

            // when
            Category category = mapper.toDomain(dto);

            // then
            assertThat(category.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("TreeDto의 updateDate가 null이면 현재 시간으로 대체")
        void shouldUseCurrentTimeForNullUpdateDate() {
            // given
            LegacyCategoryTreeDto dto =
                    new LegacyCategoryTreeDto(
                            500L,
                            "카테고리D",
                            1,
                            0L,
                            "카테고리D 표시명",
                            Yn.Y,
                            TargetGroup.MALE,
                            CategoryType.SHOSE,
                            "/500",
                            LocalDateTime.of(2024, 1, 1, 10, 0),
                            null);

            // when
            Category category = mapper.toDomain(dto);

            // then
            assertThat(category.updatedAt()).isNotNull();
        }
    }
}
