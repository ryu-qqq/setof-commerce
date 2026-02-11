package com.ryuqq.setof.storage.legacy.board.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.board.aggregate.Board;
import com.ryuqq.setof.storage.legacy.board.LegacyBoardEntityFixtures;
import com.ryuqq.setof.storage.legacy.board.entity.LegacyBoardEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * LegacyBoardEntityMapper 단위 테스트.
 *
 * <p>Entity → Domain 변환 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("LegacyBoardEntityMapper 테스트")
class LegacyBoardEntityMapperTest {

    private final LegacyBoardEntityMapper mapper = new LegacyBoardEntityMapper();

    @Nested
    @DisplayName("toDomain 메서드 테스트")
    class ToDomainTest {

        @Test
        @DisplayName("Entity를 Domain으로 정상 변환합니다")
        void shouldConvertEntityToDomain() {
            // given
            LegacyBoardEntity entity = LegacyBoardEntityFixtures.builder().build();

            // when
            Board domain = mapper.toDomain(entity);

            // then
            assertThat(domain).isNotNull();
            assertThat(domain.id().value()).isEqualTo(LegacyBoardEntityFixtures.DEFAULT_ID);
            assertThat(domain.title().value()).isEqualTo(LegacyBoardEntityFixtures.DEFAULT_TITLE);
            assertThat(domain.contents().value())
                    .isEqualTo(LegacyBoardEntityFixtures.DEFAULT_CONTENTS);
        }

        @Test
        @DisplayName("insertDate가 null인 경우 현재 시간으로 대체됩니다")
        void shouldUseCurrentInstantWhenInsertDateIsNull() {
            // given
            LegacyBoardEntity entity = LegacyBoardEntityFixtures.builder().insertDate(null).build();

            // when
            Board domain = mapper.toDomain(entity);

            // then
            assertThat(domain.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("updateDate가 null인 경우 현재 시간으로 대체됩니다")
        void shouldUseCurrentInstantWhenUpdateDateIsNull() {
            // given
            LegacyBoardEntity entity = LegacyBoardEntityFixtures.builder().updateDate(null).build();

            // when
            Board domain = mapper.toDomain(entity);

            // then
            assertThat(domain.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("모든 필드를 올바르게 매핑합니다")
        void shouldMapAllFieldsCorrectly() {
            // given
            LegacyBoardEntity entity =
                    LegacyBoardEntityFixtures.builder()
                            .id(10L)
                            .title("중요 공지사항")
                            .contents("긴급 공지 내용입니다.")
                            .build();

            // when
            Board domain = mapper.toDomain(entity);

            // then
            assertThat(domain.id().value()).isEqualTo(10L);
            assertThat(domain.title().value()).isEqualTo("중요 공지사항");
            assertThat(domain.contents().value()).isEqualTo("긴급 공지 내용입니다.");
            assertThat(domain.createdAt()).isNotNull();
            assertThat(domain.updatedAt()).isNotNull();
        }
    }
}
