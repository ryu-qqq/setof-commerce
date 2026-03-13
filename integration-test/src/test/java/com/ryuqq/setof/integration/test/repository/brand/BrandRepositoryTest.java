package com.ryuqq.setof.integration.test.repository.brand;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.brand.BrandJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Brand JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.BRAND)
@DisplayName("브랜드 JPA Repository 테스트")
class BrandRepositoryTest extends RepositoryTestBase {

    @Autowired private BrandJpaRepository brandRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("새 브랜드 저장 성공")
        void shouldSaveNewBrand() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.newEntity();

            // when
            BrandJpaEntity saved = brandRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(BrandJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("브랜드 정보가 올바르게 저장됩니다")
        void shouldSaveBrandWithCorrectInfo() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.newEntity();

            // when
            BrandJpaEntity saved = brandRepository.save(entity);
            flushAndClear();

            // then
            BrandJpaEntity found = find(BrandJpaEntity.class, saved.getId());
            assertThat(found.getBrandName()).isEqualTo(BrandJpaEntityFixtures.DEFAULT_BRAND_NAME);
            assertThat(found.getDisplayKoreanName())
                    .isEqualTo(BrandJpaEntityFixtures.DEFAULT_DISPLAY_KOREAN_NAME);
            assertThat(found.getBrandIconImageUrl())
                    .isEqualTo(BrandJpaEntityFixtures.DEFAULT_ICON_URL);
            assertThat(found.getDisplayOrder())
                    .isEqualTo(BrandJpaEntityFixtures.DEFAULT_DISPLAY_ORDER);
            assertThat(found.isDisplayed()).isTrue();
        }

        @Test
        @DisplayName("비활성 브랜드 저장 성공")
        void shouldSaveInactiveBrand() {
            // given
            BrandJpaEntity entity = BrandJpaEntityFixtures.newInactiveEntity();

            // when
            BrandJpaEntity saved = brandRepository.save(entity);
            flushAndClear();

            // then
            BrandJpaEntity found = find(BrandJpaEntity.class, saved.getId());
            assertThat(found.isDisplayed()).isFalse();
        }
    }
}
