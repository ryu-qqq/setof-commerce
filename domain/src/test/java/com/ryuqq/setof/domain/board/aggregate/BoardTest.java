package com.ryuqq.setof.domain.board.aggregate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ryuqq.setof.domain.board.exception.InvalidBoardStatusException;
import com.ryuqq.setof.domain.board.vo.BoardContent;
import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.board.vo.BoardType;
import com.ryuqq.setof.domain.board.vo.DisplayPeriod;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Board Aggregate 테스트
 *
 * <p>게시판 도메인의 핵심 비즈니스 로직을 테스트합니다.
 */
@DisplayName("Board Aggregate")
class BoardTest {

    private static final Instant FIXED_TIME = Instant.parse("2025-01-01T00:00:00Z");
    private static final Long ADMIN_USER_ID = 1L;

    @Nested
    @DisplayName("forNew() - 신규 게시글 생성")
    class ForNew {

        @Test
        @DisplayName("신규 게시글을 생성할 수 있다")
        void shouldCreateNewBoard() {
            // given
            BoardType type = BoardType.NOTICE;
            BoardContent content = new BoardContent("테스트 제목", "테스트 내용", null, null);
            DisplayPeriod displayPeriod = DisplayPeriod.unlimited();

            // when
            Board board = Board.forNew(type, content, displayPeriod, ADMIN_USER_ID, FIXED_TIME);

            // then
            assertNotNull(board);
            assertNotNull(board.getId());
            assertTrue(board.getId().isNew());
            assertEquals(BoardType.NOTICE, board.getBoardType());
            assertEquals(BoardStatus.DRAFT, board.getStatus());
            assertEquals("테스트 제목", board.getTitle());
            assertEquals("테스트 내용", board.getContentValue());
            assertEquals(0, board.getViewCount());
            assertFalse(board.isPinned());
            assertFalse(board.isDeleted());
        }

        @Test
        @DisplayName("신규 게시글은 DRAFT 상태로 시작한다")
        void shouldStartWithDraftStatus() {
            // given
            BoardContent content = new BoardContent("제목", "내용", null, null);

            // when
            Board board =
                    Board.forNew(
                            BoardType.EVENT,
                            content,
                            DisplayPeriod.unlimited(),
                            ADMIN_USER_ID,
                            FIXED_TIME);

            // then
            assertEquals(BoardStatus.DRAFT, board.getStatus());
            assertFalse(board.isDisplayable());
        }
    }

    @Nested
    @DisplayName("publish() - 게시글 게시")
    class Publish {

        @Test
        @DisplayName("DRAFT 상태의 게시글을 게시할 수 있다")
        void shouldPublishDraftBoard() {
            // given
            Board board = createDraftBoard();

            // when
            Board published = board.publish(ADMIN_USER_ID, FIXED_TIME);

            // then
            assertEquals(BoardStatus.PUBLISHED, published.getStatus());
            assertTrue(published.isDisplayable());
        }

        @Test
        @DisplayName("HIDDEN 상태의 게시글을 다시 게시할 수 있다")
        void shouldPublishHiddenBoard() {
            // given
            Board board = createPublishedBoard();
            Board hidden = board.hide(ADMIN_USER_ID, FIXED_TIME);

            // when
            Board published = hidden.publish(ADMIN_USER_ID, FIXED_TIME);

            // then
            assertEquals(BoardStatus.PUBLISHED, published.getStatus());
        }

        @Test
        @DisplayName("이미 게시된 게시글을 다시 게시하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyPublished() {
            // given
            Board board = createPublishedBoard();

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class,
                    () -> board.publish(ADMIN_USER_ID, FIXED_TIME));
        }

        @Test
        @DisplayName("삭제된 게시글을 게시하면 예외가 발생한다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            Board board = createDraftBoard();
            Board deleted = board.softDelete(ADMIN_USER_ID, FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class,
                    () -> deleted.publish(ADMIN_USER_ID, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("hide() - 게시글 숨김")
    class Hide {

        @Test
        @DisplayName("게시된 게시글을 숨길 수 있다")
        void shouldHidePublishedBoard() {
            // given
            Board board = createPublishedBoard();

            // when
            Board hidden = board.hide(ADMIN_USER_ID, FIXED_TIME);

            // then
            assertEquals(BoardStatus.HIDDEN, hidden.getStatus());
            assertFalse(hidden.isDisplayable());
        }

        @Test
        @DisplayName("DRAFT 상태의 게시글은 숨길 수 없다")
        void shouldThrowExceptionWhenDraft() {
            // given
            Board board = createDraftBoard();

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class, () -> board.hide(ADMIN_USER_ID, FIXED_TIME));
        }

        @Test
        @DisplayName("이미 숨김 처리된 게시글을 다시 숨기면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyHidden() {
            // given
            Board board = createPublishedBoard();
            Board hidden = board.hide(ADMIN_USER_ID, FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class,
                    () -> hidden.hide(ADMIN_USER_ID, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("pin() - 상단 고정")
    class Pin {

        @Test
        @DisplayName("게시글을 상단 고정할 수 있다")
        void shouldPinBoard() {
            // given
            Board board = createPublishedBoard();

            // when
            Board pinned = board.pin(1, ADMIN_USER_ID, FIXED_TIME);

            // then
            assertTrue(pinned.isPinned());
            assertEquals(1, pinned.getPinOrder());
        }

        @Test
        @DisplayName("상단 고정을 해제할 수 있다")
        void shouldUnpinBoard() {
            // given
            Board board = createPublishedBoard();
            Board pinned = board.pin(1, ADMIN_USER_ID, FIXED_TIME);

            // when
            Board unpinned = pinned.unpin(ADMIN_USER_ID, FIXED_TIME);

            // then
            assertFalse(unpinned.isPinned());
            assertEquals(0, unpinned.getPinOrder());
        }
    }

    @Nested
    @DisplayName("updateContent() - 내용 수정")
    class UpdateContent {

        @Test
        @DisplayName("게시글 내용을 수정할 수 있다")
        void shouldUpdateContent() {
            // given
            Board board = createDraftBoard();
            BoardContent newContent = new BoardContent("수정된 제목", "수정된 내용", "요약", null);

            // when
            Board updated = board.updateContent(newContent, ADMIN_USER_ID, FIXED_TIME);

            // then
            assertEquals("수정된 제목", updated.getTitle());
            assertEquals("수정된 내용", updated.getContentValue());
            assertEquals("요약", updated.getSummary());
        }

        @Test
        @DisplayName("삭제된 게시글은 수정할 수 없다")
        void shouldThrowExceptionWhenDeleted() {
            // given
            Board board = createDraftBoard();
            Board deleted = board.softDelete(ADMIN_USER_ID, FIXED_TIME);
            BoardContent newContent = new BoardContent("수정된 제목", "수정된 내용", null, null);

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class,
                    () -> deleted.updateContent(newContent, ADMIN_USER_ID, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("incrementViewCount() - 조회수 증가")
    class IncrementViewCount {

        @Test
        @DisplayName("조회수를 증가시킬 수 있다")
        void shouldIncrementViewCount() {
            // given
            Board board = createPublishedBoard();
            assertEquals(0, board.getViewCount());

            // when
            Board viewed = board.incrementViewCount();

            // then
            assertEquals(1, viewed.getViewCount());
        }
    }

    @Nested
    @DisplayName("softDelete() - 소프트 삭제")
    class SoftDelete {

        @Test
        @DisplayName("게시글을 소프트 삭제할 수 있다")
        void shouldSoftDeleteBoard() {
            // given
            Board board = createDraftBoard();

            // when
            Board deleted = board.softDelete(ADMIN_USER_ID, FIXED_TIME);

            // then
            assertTrue(deleted.isDeleted());
            assertNotNull(deleted.getDeletedAt());
        }

        @Test
        @DisplayName("이미 삭제된 게시글을 다시 삭제하면 예외가 발생한다")
        void shouldThrowExceptionWhenAlreadyDeleted() {
            // given
            Board board = createDraftBoard();
            Board deleted = board.softDelete(ADMIN_USER_ID, FIXED_TIME);

            // when & then
            assertThrows(
                    InvalidBoardStatusException.class,
                    () -> deleted.softDelete(ADMIN_USER_ID, FIXED_TIME));
        }
    }

    @Nested
    @DisplayName("isDisplayable() - 표시 가능 여부")
    class IsDisplayable {

        @Test
        @DisplayName("게시 상태이고 노출 기간 내이면 표시 가능하다")
        void shouldBeDisplayableWhenPublishedAndWithinPeriod() {
            // given
            DisplayPeriod period =
                    new DisplayPeriod(
                            Instant.parse("2024-01-01T00:00:00Z"),
                            Instant.parse("2026-12-31T23:59:59Z"));
            BoardContent content = new BoardContent("제목", "내용", null, null);
            Board board =
                    Board.forNew(BoardType.NOTICE, content, period, ADMIN_USER_ID, FIXED_TIME);
            Board published = board.publish(ADMIN_USER_ID, FIXED_TIME);

            // when
            boolean displayable = published.isDisplayable();

            // then
            assertTrue(displayable);
        }

        @Test
        @DisplayName("DRAFT 상태이면 표시 불가능하다")
        void shouldNotBeDisplayableWhenDraft() {
            // given
            Board board = createDraftBoard();

            // when
            boolean displayable = board.isDisplayable();

            // then
            assertFalse(displayable);
        }

        @Test
        @DisplayName("삭제된 게시글은 표시 불가능하다")
        void shouldNotBeDisplayableWhenDeleted() {
            // given
            Board board = createPublishedBoard();
            Board deleted = board.softDelete(ADMIN_USER_ID, FIXED_TIME);

            // when
            boolean displayable = deleted.isDisplayable();

            // then
            assertFalse(displayable);
        }
    }

    // ===== Helper Methods =====

    private Board createDraftBoard() {
        BoardContent content = new BoardContent("테스트 제목", "테스트 내용", null, null);
        return Board.forNew(
                BoardType.NOTICE, content, DisplayPeriod.unlimited(), ADMIN_USER_ID, FIXED_TIME);
    }

    private Board createPublishedBoard() {
        Board board = createDraftBoard();
        return board.publish(ADMIN_USER_ID, FIXED_TIME);
    }
}
