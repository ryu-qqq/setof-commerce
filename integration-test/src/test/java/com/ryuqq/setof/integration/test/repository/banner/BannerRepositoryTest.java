package com.ryuqq.setof.integration.test.repository.banner;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerGroupJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Banner JPA Repository 통합 테스트.
 *
 * <p>BannerGroupJpaRepository와 BannerSlideJpaRepository의 기본 CRUD 동작을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.BANNER)
@DisplayName("배너 JPA Repository 테스트")
class BannerRepositoryTest extends RepositoryTestBase {

    @Autowired private BannerGroupJpaRepository groupRepository;
    @Autowired private BannerSlideJpaRepository slideRepository;

    @Nested
    @DisplayName("BannerGroup save 테스트")
    class BannerGroupSaveTest {

        @Test
        @DisplayName("새 배너 그룹 저장 성공")
        void shouldSaveNewBannerGroup() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity(null);

            // when
            BannerGroupJpaEntity saved = groupRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(BannerGroupJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("배너 그룹 정보가 올바르게 저장됩니다")
        void shouldSaveBannerGroupWithCorrectInfo() {
            // given
            BannerGroupJpaEntity entity = BannerJpaEntityFixtures.activeGroupEntity(null);

            // when
            BannerGroupJpaEntity saved = groupRepository.save(entity);
            flushAndClear();

            // then
            BannerGroupJpaEntity found = find(BannerGroupJpaEntity.class, saved.getId());
            assertThat(found.getTitle()).isEqualTo(BannerJpaEntityFixtures.DEFAULT_TITLE);
            assertThat(found.getBannerType())
                    .isEqualTo(BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("비활성 배너 그룹 저장 성공")
        void shouldSaveInactiveBannerGroup() {
            // given
            Instant now = Instant.now();
            BannerGroupJpaEntity entity =
                    BannerGroupJpaEntity.create(
                            null,
                            BannerJpaEntityFixtures.DEFAULT_TITLE,
                            BannerJpaEntityFixtures.DEFAULT_BANNER_TYPE,
                            BannerJpaEntityFixtures.DEFAULT_DISPLAY_START_AT,
                            BannerJpaEntityFixtures.DEFAULT_DISPLAY_END_AT,
                            false,
                            now,
                            now,
                            null);

            // when
            BannerGroupJpaEntity saved = groupRepository.save(entity);
            flushAndClear();

            // then
            BannerGroupJpaEntity found = find(BannerGroupJpaEntity.class, saved.getId());
            assertThat(found.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("BannerSlide save 테스트")
    class BannerSlideSaveTest {

        @Test
        @DisplayName("새 배너 슬라이드 저장 성공")
        void shouldSaveNewBannerSlide() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity entity = BannerJpaEntityFixtures.newSlideEntity(group.getId());

            // when
            BannerSlideJpaEntity saved = slideRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(BannerSlideJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("배너 슬라이드 정보가 올바르게 저장됩니다")
        void shouldSaveBannerSlideWithCorrectInfo() {
            // given
            BannerGroupJpaEntity group =
                    groupRepository.save(BannerJpaEntityFixtures.activeGroupEntity(null));
            BannerSlideJpaEntity entity = BannerJpaEntityFixtures.newSlideEntity(group.getId());

            // when
            BannerSlideJpaEntity saved = slideRepository.save(entity);
            flushAndClear();

            // then
            BannerSlideJpaEntity found = find(BannerSlideJpaEntity.class, saved.getId());
            assertThat(found.getTitle()).isEqualTo(BannerJpaEntityFixtures.DEFAULT_SLIDE_TITLE);
            assertThat(found.getImageUrl()).isEqualTo(BannerJpaEntityFixtures.DEFAULT_IMAGE_URL);
            assertThat(found.getLinkUrl()).isEqualTo(BannerJpaEntityFixtures.DEFAULT_LINK_URL);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getDeletedAt()).isNull();
            assertThat(found.getBannerGroupId()).isEqualTo(group.getId());
        }
    }
}
