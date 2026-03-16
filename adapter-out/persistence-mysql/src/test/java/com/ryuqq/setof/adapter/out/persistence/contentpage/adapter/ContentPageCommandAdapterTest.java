package com.ryuqq.setof.adapter.out.persistence.contentpage.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.mapper.ContentPageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageJpaRepository;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ContentPageCommandAdapterTest - 콘텐츠 페이지 Command Adapter 단위 테스트.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ContentPageCommandAdapter 단위 테스트")
class ContentPageCommandAdapterTest {

    @Mock private ContentPageJpaRepository contentPageJpaRepository;

    @Mock private ContentPageJpaEntityMapper mapper;

    @InjectMocks private ContentPageCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persist 테스트
    // ========================================================================

    @Nested
    @DisplayName("persist 메서드 테스트")
    class PersistTest {

        @Test
        @DisplayName("Domain을 Entity로 변환 후 저장하고 ID를 반환합니다")
        void persist_WithValidDomain_SavesAndReturnsId() {
            // given
            ContentPage domain = ContentPageFixtures.newContentPage();
            ContentPageJpaEntity entityToSave = ContentPageJpaEntityFixtures.newEntity();
            ContentPageJpaEntity savedEntity = ContentPageJpaEntityFixtures.activeEntity(100L);

            given(mapper.toEntity(domain)).willReturn(entityToSave);
            given(contentPageJpaRepository.save(entityToSave)).willReturn(savedEntity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(100L);
            then(mapper).should().toEntity(domain);
            then(contentPageJpaRepository).should().save(entityToSave);
        }

        @Test
        @DisplayName("활성 상태 콘텐츠 페이지를 저장합니다")
        void persist_WithActiveContentPage_Saves() {
            // given
            ContentPage domain = ContentPageFixtures.activeContentPage();
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(contentPageJpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("비활성 상태 콘텐츠 페이지를 저장합니다")
        void persist_WithInactiveContentPage_Saves() {
            // given
            ContentPage domain = ContentPageFixtures.inactiveContentPage();
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.inactiveEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(contentPageJpaRepository.save(entity)).willReturn(entity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isNotNull();
        }

        @Test
        @DisplayName("Mapper가 정확히 한 번 호출됩니다")
        void persist_CallsMapperOnce() {
            // given
            ContentPage domain = ContentPageFixtures.newContentPage();
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.newEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(contentPageJpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(mapper).should(times(1)).toEntity(domain);
        }
    }
}
