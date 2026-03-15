package com.ryuqq.setof.adapter.out.persistence.banner.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BannerGroupJpaEntityMapperTest - 배너 그룹 Entity-Domain 매퍼 단위 테스트.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("BannerGroupJpaEntityMapper 단위 테스트")
class BannerGroupJpaEntityMapperTest {

    private BannerGroupJpaEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BannerGroupJpaEntityMapper();
    }

    // ========================================================================
    // 1. toEntity 테스트
    // ========================================================================

    @Nested
    @DisplayName("toEntity 메서드 테스트")
    class ToEntityTest {

        @Test
        @DisplayName("활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithActiveBannerGroup_ConvertsCorrectly() {
            // given
            BannerGroup domain = BannerFixtures.activeBannerGroup();
            List<BannerSlide> slides = BannerFixtures.defaultSlides();

            // when
            BannerGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.idValue());
            assertThat(entity.getTitle()).isEqualTo(domain.title());
            assertThat(entity.getBannerType()).isEqualTo(domain.bannerType().name());
            assertThat(entity.getDisplayStartAt()).isEqualTo(domain.displayPeriod().startDate());
            assertThat(entity.getDisplayEndAt()).isEqualTo(domain.displayPeriod().endDate());
            assertThat(entity.isActive()).isTrue();
            assertThat(entity.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("비활성 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithInactiveBannerGroup_ConvertsCorrectly() {
            // given
            BannerGroup domain = BannerFixtures.inactiveBannerGroup();

            // when
            BannerGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.isActive()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Domain을 Entity로 변환합니다")
        void toEntity_WithDeletedBannerGroup_ConvertsCorrectly() {
            // given
            BannerGroup domain = BannerFixtures.deletedBannerGroup();

            // when
            BannerGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("새로운 Domain을 Entity로 변환합니다")
        void toEntity_WithNewBannerGroup_ConvertsCorrectly() {
            // given
            BannerGroup domain = BannerFixtures.newBannerGroup();

            // when
            BannerGroupJpaEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isNull();
            assertThat(entity.getTitle()).isEqualTo(domain.title());
            assertThat(entity.getBannerType()).isEqualTo(domain.bannerType().name());
        }
    }

    // ========================================================================
    // 2. toDomain 테스트
    // ========================================================================

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithActiveEntity_ConvertsCorrectly() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity();
            List<BannerSlide> slides = BannerFixtures.defaultSlides();

            // when
            BannerGroup domain = mapper.toDomain(entity, slides);

            // then
            assertThat(domain.idValue()).isEqualTo(entity.getId());
            assertThat(domain.title()).isEqualTo(entity.getTitle());
            assertThat(domain.bannerType().name()).isEqualTo(entity.getBannerType());
            assertThat(domain.displayPeriod().startDate()).isEqualTo(entity.getDisplayStartAt());
            assertThat(domain.displayPeriod().endDate()).isEqualTo(entity.getDisplayEndAt());
            assertThat(domain.isActive()).isTrue();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("비활성 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithInactiveEntity_ConvertsCorrectly() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.inactiveGroupEntity();
            List<BannerSlide> slides = BannerFixtures.defaultSlides();

            // when
            BannerGroup domain = mapper.toDomain(entity, slides);

            // then
            assertThat(domain.isActive()).isFalse();
            assertThat(domain.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 상태 Entity를 Domain으로 변환합니다")
        void toDomain_WithDeletedEntity_ConvertsCorrectly() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.deletedGroupEntity();
            List<BannerSlide> slides = BannerFixtures.emptySlides();

            // when
            BannerGroup domain = mapper.toDomain(entity, slides);

            // then
            assertThat(domain.isDeleted()).isTrue();
            assertThat(domain.deletionStatus().deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("슬라이드 목록이 함께 매핑됩니다")
        void toDomain_WithSlides_MapsSlidesCorrectly() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity();
            List<BannerSlide> slides = BannerFixtures.defaultSlides();

            // when
            BannerGroup domain = mapper.toDomain(entity, slides);

            // then
            assertThat(domain.slides()).hasSize(slides.size());
        }

        @Test
        @DisplayName("슬라이드 목록이 비어있어도 Domain으로 변환됩니다")
        void toDomain_WithEmptySlides_ConvertsCorrectly() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity();
            List<BannerSlide> emptySlides = BannerFixtures.emptySlides();

            // when
            BannerGroup domain = mapper.toDomain(entity, emptySlides);

            // then
            assertThat(domain.slides()).isEmpty();
        }
    }

    // ========================================================================
    // 3. 양방향 변환 테스트
    // ========================================================================

    @Nested
    @DisplayName("양방향 변환 테스트")
    class BidirectionalConversionTest {

        @Test
        @DisplayName("Domain -> Entity -> Domain 변환 시 데이터가 보존됩니다")
        void roundTrip_DomainToEntityToDomain_PreservesData() {
            // given
            BannerGroup original = BannerFixtures.activeBannerGroup();
            List<BannerSlide> slides = original.slides();

            // when
            BannerGroupJpaEntity entity = mapper.toEntity(original);
            BannerGroup converted = mapper.toDomain(entity, slides);

            // then
            assertThat(converted.idValue()).isEqualTo(original.idValue());
            assertThat(converted.title()).isEqualTo(original.title());
            assertThat(converted.bannerType()).isEqualTo(original.bannerType());
            assertThat(converted.displayPeriod().startDate())
                    .isEqualTo(original.displayPeriod().startDate());
            assertThat(converted.displayPeriod().endDate())
                    .isEqualTo(original.displayPeriod().endDate());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }

        @Test
        @DisplayName("Entity -> Domain -> Entity 변환 시 데이터가 보존됩니다")
        void roundTrip_EntityToDomainToEntity_PreservesData() {
            // given
            BannerGroupJpaEntity original = BannerJpaEntityFixtures.activeGroupEntity();
            List<BannerSlide> slides = BannerFixtures.defaultSlides();

            // when
            BannerGroup domain = mapper.toDomain(original, slides);
            BannerGroupJpaEntity converted = mapper.toEntity(domain);

            // then
            assertThat(converted.getId()).isEqualTo(original.getId());
            assertThat(converted.getTitle()).isEqualTo(original.getTitle());
            assertThat(converted.getBannerType()).isEqualTo(original.getBannerType());
            assertThat(converted.getDisplayStartAt()).isEqualTo(original.getDisplayStartAt());
            assertThat(converted.getDisplayEndAt()).isEqualTo(original.getDisplayEndAt());
            assertThat(converted.isActive()).isEqualTo(original.isActive());
        }
    }
}
