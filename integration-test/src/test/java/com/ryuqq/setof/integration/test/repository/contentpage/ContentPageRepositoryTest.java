package com.ryuqq.setof.integration.test.repository.contentpage;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageJpaRepository;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ContentPage JPA Repository 통합 테스트.
 *
 * <p>ContentPageJpaRepository의 기본 CRUD 동작을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag(TestTags.CONTENT_PAGE)
@DisplayName("콘텐츠 페이지 JPA Repository 테스트")
class ContentPageRepositoryTest extends RepositoryTestBase {

    @Autowired private ContentPageJpaRepository contentPageRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("새 콘텐츠 페이지 저장 성공")
        void shouldSaveNewContentPage() {
            // given
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.newEntity();

            // when
            ContentPageJpaEntity saved = contentPageRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();
            assertThat(find(ContentPageJpaEntity.class, saved.getId())).isNotNull();
        }

        @Test
        @DisplayName("콘텐츠 페이지 정보가 올바르게 저장됩니다")
        void shouldSaveContentPageWithCorrectInfo() {
            // given
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.newEntity();

            // when
            ContentPageJpaEntity saved = contentPageRepository.save(entity);
            flushAndClear();

            // then
            ContentPageJpaEntity found = find(ContentPageJpaEntity.class, saved.getId());
            assertThat(found.getTitle()).isEqualTo(ContentPageJpaEntityFixtures.DEFAULT_TITLE);
            assertThat(found.getMemo()).isEqualTo(ContentPageJpaEntityFixtures.DEFAULT_MEMO);
            assertThat(found.getImageUrl())
                    .isEqualTo(ContentPageJpaEntityFixtures.DEFAULT_IMAGE_URL);
            assertThat(found.isActive()).isTrue();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("memo가 null인 콘텐츠 페이지 저장 성공")
        void shouldSaveContentPageWithNullMemo() {
            // given
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.entityWithoutMemo();
            ContentPageJpaEntity newEntity =
                    ContentPageJpaEntity.create(
                            null,
                            entity.getTitle(),
                            null,
                            entity.getImageUrl(),
                            entity.getDisplayStartAt(),
                            entity.getDisplayEndAt(),
                            entity.isActive(),
                            entity.getCreatedAt(),
                            entity.getUpdatedAt(),
                            null);

            // when
            ContentPageJpaEntity saved = contentPageRepository.save(newEntity);
            flushAndClear();

            // then
            ContentPageJpaEntity found = find(ContentPageJpaEntity.class, saved.getId());
            assertThat(found.getMemo()).isNull();
        }

        @Test
        @DisplayName("비활성 콘텐츠 페이지 저장 성공")
        void shouldSaveInactiveContentPage() {
            // given
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.inactiveEntity();
            ContentPageJpaEntity newEntity =
                    ContentPageJpaEntity.create(
                            null,
                            entity.getTitle(),
                            entity.getMemo(),
                            entity.getImageUrl(),
                            entity.getDisplayStartAt(),
                            entity.getDisplayEndAt(),
                            false,
                            entity.getCreatedAt(),
                            entity.getUpdatedAt(),
                            null);

            // when
            ContentPageJpaEntity saved = contentPageRepository.save(newEntity);
            flushAndClear();

            // then
            ContentPageJpaEntity found = find(ContentPageJpaEntity.class, saved.getId());
            assertThat(found.isActive()).isFalse();
        }
    }
}
