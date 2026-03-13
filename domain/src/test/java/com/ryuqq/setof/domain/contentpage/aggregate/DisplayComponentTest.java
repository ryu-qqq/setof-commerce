package com.ryuqq.setof.domain.contentpage.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.contentpage.id.DisplayComponentId;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.BrandFilter;
import com.ryuqq.setof.domain.contentpage.vo.ComponentSpec;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayTab;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtension;
import com.ryuqq.setof.domain.contentpage.vo.ViewExtensionType;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("DisplayComponent Aggregate 단위 테스트")
class DisplayComponentTest {

    @Nested
    @DisplayName("forNew() - 신규 디스플레이 컴포넌트 생성")
    class ForNewTest {

        @Test
        @DisplayName("필수 정보로 신규 디스플레이 컴포넌트를 생성한다")
        void createNewDisplayComponentWithRequiredFields() {
            // given
            long contentPageId = 1L;
            String name = "테스트 컴포넌트";
            int displayOrder = 1;
            ComponentType componentType = ComponentType.PRODUCT;
            Instant now = Instant.now();

            // when
            DisplayComponent component =
                    DisplayComponent.forNew(
                            contentPageId,
                            name,
                            displayOrder,
                            componentType,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            now);

            // then
            assertThat(component.id().isNew()).isTrue();
            assertThat(component.contentPageId()).isEqualTo(contentPageId);
            assertThat(component.name()).isEqualTo(name);
            assertThat(component.displayOrder()).isEqualTo(displayOrder);
            assertThat(component.componentType()).isEqualTo(componentType);
            assertThat(component.isActive()).isTrue();
            assertThat(component.isDeleted()).isFalse();
            assertThat(component.createdAt()).isEqualTo(now);
            assertThat(component.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("신규 생성 시 DeletionStatus는 active 상태이다")
        void newDisplayComponentHasActiveDeletionStatus() {
            // given
            Instant now = Instant.now();

            // when
            DisplayComponent component =
                    DisplayComponent.forNew(
                            1L,
                            "컴포넌트",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            now);

            // then
            assertThat(component.deletionStatus().isDeleted()).isFalse();
        }

        @Test
        @DisplayName("ComponentType은 생성 후 변경되지 않는다 (불변)")
        void componentTypeIsImmutable() {
            // given
            Instant now = Instant.now();
            DisplayComponent component =
                    DisplayComponent.forNew(
                            1L,
                            "컴포넌트",
                            1,
                            ComponentType.IMAGE,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            now);

            // then
            assertThat(component.componentType()).isEqualTo(ComponentType.IMAGE);
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 디스플레이 컴포넌트를 복원한다")
        void reconstituteActiveDisplayComponent() {
            // given
            DisplayComponentId id = DisplayComponentId.of(1L);
            Instant createdAt = Instant.now().minusSeconds(86400);

            // when
            DisplayComponent component =
                    DisplayComponent.reconstitute(
                            id,
                            1L,
                            "복원된 컴포넌트",
                            1,
                            ComponentType.PRODUCT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            DeletionStatus.active(),
                            createdAt,
                            createdAt);

            // then
            assertThat(component.id()).isEqualTo(id);
            assertThat(component.id().isNew()).isFalse();
            assertThat(component.isActive()).isTrue();
            assertThat(component.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("update() - 디스플레이 컴포넌트 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("디스플레이 컴포넌트 정보를 수정한다")
        void updateDisplayComponentInfo() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            DisplayComponentUpdateData updateData =
                    ContentPageFixtures.displayComponentUpdateData();

            // when
            component.update(updateData);

            // then
            assertThat(component.name()).isEqualTo(updateData.name());
            assertThat(component.displayOrder()).isEqualTo(updateData.displayOrder());
            assertThat(component.displayConfig()).isEqualTo(updateData.displayConfig());
            assertThat(component.isActive()).isEqualTo(updateData.active());
            assertThat(component.updatedAt()).isEqualTo(updateData.updatedAt());
        }
    }

    @Nested
    @DisplayName("remove() - 소프트 삭제")
    class RemoveTest {

        @Test
        @DisplayName("디스플레이 컴포넌트를 소프트 삭제한다")
        void removeDisplayComponent() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            Instant now = Instant.now();

            // when
            component.remove(now);

            // then
            assertThat(component.isDeleted()).isTrue();
            assertThat(component.isActive()).isFalse();
            assertThat(component.updatedAt()).isEqualTo(now);
            assertThat(component.deletionStatus().deletedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("isDisplayable() - 노출 가능 여부 판단")
    class IsDisplayableTest {

        @Test
        @DisplayName("활성 상태이고 삭제되지 않았으며 노출 기간 내이면 노출 가능하다")
        void displayableWhenActiveAndNotDeletedAndWithinPeriod() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            Instant now = Instant.now();

            // when
            boolean displayable = component.isDisplayable(now);

            // then
            assertThat(displayable).isTrue();
        }

        @Test
        @DisplayName("삭제된 컴포넌트는 노출 불가이다")
        void notDisplayableWhenDeleted() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            Instant now = Instant.now();
            component.remove(now);

            // when
            boolean displayable = component.isDisplayable(now);

            // then
            assertThat(displayable).isFalse();
        }
    }

    @Nested
    @DisplayName("isProductComponent() - 상품 관련 컴포넌트 여부")
    class IsProductComponentTest {

        @Test
        @DisplayName("PRODUCT 타입은 상품 컴포넌트이다")
        void productTypeIsProductComponent() {
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);
            assertThat(component.isProductComponent()).isTrue();
        }

        @Test
        @DisplayName("TAB 타입은 상품 컴포넌트이다")
        void tabTypeIsProductComponent() {
            DisplayComponent component =
                    ContentPageFixtures.tabComponent(
                            1L,
                            com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE,
                            List.of(ContentPageFixtures.displayTab(10L, "탭A", 0, 0)));
            assertThat(component.isProductComponent()).isTrue();
        }

        @Test
        @DisplayName("TEXT 타입은 상품 컴포넌트가 아니다")
        void textTypeIsNotProductComponent() {
            DisplayComponent component = ContentPageFixtures.textComponent(1L);
            assertThat(component.isProductComponent()).isFalse();
        }
    }

    @Nested
    @DisplayName("resolvePageSize() - 페이지 사이즈 계산")
    class ResolvePageSizeTest {

        @Test
        @DisplayName("exposedProducts가 0이면 Integer.MAX_VALUE를 반환한다")
        void zeroExposedProductsReturnsMaxValue() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 0);

            // when
            int pageSize = component.resolvePageSize();

            // then
            assertThat(pageSize).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("exposedProducts가 10이면 10을 반환한다")
        void exposedProductsTenReturnsTen() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);

            // when
            int pageSize = component.resolvePageSize();

            // then
            assertThat(pageSize).isEqualTo(10);
        }

        @Test
        @DisplayName(
                "ViewExtension PRODUCT 타입이면 exposed + maxClickCount * productCountPerClick을 반환한다")
        void viewExtensionProductTypeAddsClickProducts() {
            // given: exposed=5, maxClickCount=3, productCountPerClick=2 → 5 + 3*2 = 11
            ViewExtension viewExtension =
                    new ViewExtension(
                            1L,
                            ViewExtensionType.PRODUCT,
                            null,
                            "더보기",
                            2,
                            3,
                            ViewExtensionType.NONE,
                            null);
            ComponentSpec spec = new ComponentSpec.ProductSpec(100L, 5, List.of(), List.of());
            DisplayComponent component =
                    DisplayComponent.reconstitute(
                            DisplayComponentId.of(1L),
                            1L,
                            "컴포넌트",
                            1,
                            ComponentType.PRODUCT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            viewExtension,
                            spec,
                            DeletionStatus.active(),
                            Instant.now(),
                            Instant.now());

            // when
            int pageSize = component.resolvePageSize();

            // then
            assertThat(pageSize).isEqualTo(11);
        }
    }

    @Nested
    @DisplayName("resolveAutoProductCriteria() - AUTO 상품 조회 조건 추출")
    class ResolveAutoProductCriteriaTest {

        @Test
        @DisplayName("ComponentSpec이 null이면 빈 리스트를 반환한다")
        void nullSpecReturnsEmpty() {
            // given
            DisplayComponent component =
                    DisplayComponent.reconstitute(
                            DisplayComponentId.of(1L),
                            1L,
                            "컴포넌트",
                            1,
                            ComponentType.TEXT,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            DeletionStatus.active(),
                            Instant.now(),
                            Instant.now());

            // when
            List<AutoProductCriteria> criteria = component.resolveAutoProductCriteria();

            // then
            assertThat(criteria).isEmpty();
        }

        @Test
        @DisplayName("ProductSpec이면 단일 AutoProductCriteria를 반환한다")
        void productSpecReturnsSingleCriteria() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);

            // when
            List<AutoProductCriteria> criteria = component.resolveAutoProductCriteria();

            // then
            assertThat(criteria).hasSize(1);
            assertThat(criteria.get(0).componentId()).isEqualTo(1L);
            assertThat(criteria.get(0).isTabLevel()).isFalse();
        }

        @Test
        @DisplayName("CategorySpec이면 categoryId를 포함한 AutoProductCriteria를 반환한다")
        void categorySpecReturnsCriteriaWithCategoryId() {
            // given
            ComponentSpec spec =
                    new ComponentSpec.CategorySpec(100L, 50L, 10, List.of(), List.of());
            DisplayComponent component =
                    DisplayComponent.reconstitute(
                            DisplayComponentId.of(2L),
                            1L,
                            "카테고리 컴포넌트",
                            1,
                            ComponentType.CATEGORY,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            spec,
                            DeletionStatus.active(),
                            Instant.now(),
                            Instant.now());

            // when
            List<AutoProductCriteria> criteria = component.resolveAutoProductCriteria();

            // then
            assertThat(criteria).hasSize(1);
            assertThat(criteria.get(0).categoryId()).isEqualTo(50L);
            assertThat(criteria.get(0).hasCategoryFilter()).isTrue();
        }

        @Test
        @DisplayName("BrandSpec이면 brandIds를 포함한 AutoProductCriteria를 반환한다")
        void brandSpecReturnsCriteriaWithBrandIds() {
            // given
            List<BrandFilter> brandFilters =
                    List.of(new BrandFilter(10L, "브랜드A"), new BrandFilter(20L, "브랜드B"));
            ComponentSpec spec =
                    new ComponentSpec.BrandSpec(100L, 50L, 10, brandFilters, List.of(), List.of());
            DisplayComponent component =
                    DisplayComponent.reconstitute(
                            DisplayComponentId.of(3L),
                            1L,
                            "브랜드 컴포넌트",
                            1,
                            ComponentType.BRAND,
                            ContentPageFixtures.defaultDisplayConfig(),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            spec,
                            DeletionStatus.active(),
                            Instant.now(),
                            Instant.now());

            // when
            List<AutoProductCriteria> criteria = component.resolveAutoProductCriteria();

            // then
            assertThat(criteria).hasSize(1);
            assertThat(criteria.get(0).brandIds()).containsExactlyInAnyOrder(10L, 20L);
            assertThat(criteria.get(0).hasBrandFilter()).isTrue();
        }

        @Test
        @DisplayName("TabSpec이면 탭별로 AutoProductCriteria를 반환한다")
        void tabSpecReturnsCriteriaPerTab() {
            // given
            DisplayTab tab1 = ContentPageFixtures.displayTab(10L, "탭A", 1, 2);
            DisplayTab tab2 = ContentPageFixtures.displayTab(20L, "탭B", 0, 3);
            DisplayComponent component =
                    ContentPageFixtures.tabComponent(
                            1L,
                            com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE,
                            List.of(tab1, tab2));

            // when
            List<AutoProductCriteria> criteria = component.resolveAutoProductCriteria();

            // then
            assertThat(criteria).hasSize(2);
            assertThat(criteria)
                    .extracting(AutoProductCriteria::tabId)
                    .containsExactlyInAnyOrder(10L, 20L);
            assertThat(criteria.get(0).isTabLevel()).isTrue();
        }
    }

    @Nested
    @DisplayName("resolveTabPageSize() - 탭 페이지 사이즈 계산")
    class ResolveTabPageSizeTest {

        @Test
        @DisplayName("tabId가 일치하는 탭의 (fixedProducts + autoProducts) 수를 반환한다")
        void returnsSumOfFixedAndAutoProductsForMatchingTab() {
            // given: fixedCount=2, autoCount=3 → total=5
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 2, 3);
            DisplayComponent component =
                    ContentPageFixtures.tabComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, List.of(tab));

            // when
            int pageSize = component.resolveTabPageSize(10L);

            // then
            assertThat(pageSize).isEqualTo(5);
        }

        @Test
        @DisplayName("fixedProducts + autoProducts가 0이면 기본값 20을 반환한다")
        void returnsDefaultTwentyWhenNoProducts() {
            // given
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 0, 0);
            DisplayComponent component =
                    ContentPageFixtures.tabComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, List.of(tab));

            // when
            int pageSize = component.resolveTabPageSize(10L);

            // then
            assertThat(pageSize).isEqualTo(20);
        }

        @Test
        @DisplayName("일치하는 탭이 없으면 기본값 20을 반환한다")
        void returnsDefaultTwentyWhenTabNotFound() {
            // given
            DisplayTab tab = ContentPageFixtures.displayTab(10L, "탭A", 2, 3);
            DisplayComponent component =
                    ContentPageFixtures.tabComponent(
                            1L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, List.of(tab));

            // when
            int pageSize = component.resolveTabPageSize(99L);

            // then
            assertThat(pageSize).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 값을 반환한다")
        void idValueReturnsIdValue() {
            // given
            DisplayComponent component =
                    ContentPageFixtures.productComponent(
                            5L, com.ryuqq.setof.domain.contentpage.vo.OrderType.NONE, 10);

            // when
            Long idValue = component.idValue();

            // then
            assertThat(idValue).isEqualTo(5L);
        }
    }
}
