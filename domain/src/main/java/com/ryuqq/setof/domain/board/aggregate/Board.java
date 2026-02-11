package com.ryuqq.setof.domain.board.aggregate;

import com.ryuqq.setof.domain.board.id.BoardId;
import com.ryuqq.setof.domain.board.vo.BoardContents;
import com.ryuqq.setof.domain.board.vo.BoardTitle;
import java.time.Instant;

/**
 * Board - 공지사항 Aggregate.
 *
 * <p>공지사항 정보를 표현합니다.
 *
 * <p>현재 조회 전용 도메인으로, reconstitute 팩토리 메서드만 제공합니다. 향후 CUD 추가 시 forNew(), 비즈니스 메서드를 확장하세요.
 *
 * <p>DOM-AGG-001: 생성자 직접 노출 대신 static 팩토리 메서드 사용.
 *
 * <p>DOM-AGG-002: ID는 전용 ID VO(BoardId) 사용.
 *
 * <p>DOM-AGG-003: 시간 필드 Instant 타입.
 *
 * <p>DOM-AGG-004: Setter 금지.
 *
 * <p>DOM-AGG-008: 필수 도메인 값은 전용 VO 사용.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Board {

    private final BoardId id;
    private final BoardTitle title;
    private final BoardContents contents;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Board(
            BoardId id,
            BoardTitle title,
            BoardContents contents,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 영속성 계층에서 엔티티 복원.
     *
     * @param id 식별자
     * @param title 공지사항 제목
     * @param contents 공지사항 내용
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @return 복원된 Board 인스턴스
     */
    public static Board reconstitute(
            BoardId id,
            BoardTitle title,
            BoardContents contents,
            Instant createdAt,
            Instant updatedAt) {
        return new Board(id, title, contents, createdAt, updatedAt);
    }

    // ========== Query Methods ==========

    /** 신규 생성 여부 확인. */
    public boolean isNew() {
        return id.isNew();
    }

    // ========== Accessor Methods ==========

    public BoardId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public BoardTitle title() {
        return title;
    }

    public String titleValue() {
        return title.value();
    }

    public BoardContents contents() {
        return contents;
    }

    public String contentsValue() {
        return contents.value();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
