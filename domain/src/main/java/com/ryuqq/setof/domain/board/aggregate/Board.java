package com.ryuqq.setof.domain.board.aggregate;

import com.ryuqq.setof.domain.board.exception.InvalidBoardStatusException;
import com.ryuqq.setof.domain.board.vo.BoardContent;
import com.ryuqq.setof.domain.board.vo.BoardId;
import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.board.vo.BoardType;
import com.ryuqq.setof.domain.board.vo.DisplayPeriod;
import com.ryuqq.setof.domain.board.vo.PinSetting;
import java.time.Instant;

/**
 * Board Aggregate Root
 *
 * <p>게시물을 나타내는 도메인 엔티티입니다. 공지사항, 이벤트, 뉴스 등의 게시물을 관리합니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - final 필드
 *   <li>Private 생성자 + Static Factory - 외부 직접 생성 금지
 *   <li>Law of Demeter - Helper 메서드로 내부 객체 접근 제공
 * </ul>
 */
@SuppressWarnings("PMD.DomainLayerDemeterStrict")
public class Board {

    private final BoardId id;
    private final BoardType boardType;
    private final BoardContent content;
    private final PinSetting pinSetting;
    private final DisplayPeriod displayPeriod;
    private final BoardStatus status;
    private final long viewCount;
    private final Long createdBy;
    private final Long updatedBy;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    /** Private 생성자 - 외부 직접 생성 금지 */
    private Board(
            BoardId id,
            BoardType boardType,
            BoardContent content,
            PinSetting pinSetting,
            DisplayPeriod displayPeriod,
            BoardStatus status,
            long viewCount,
            Long createdBy,
            Long updatedBy,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        this.id = id;
        this.boardType = boardType;
        this.content = content;
        this.pinSetting = pinSetting;
        this.displayPeriod = displayPeriod;
        this.status = status;
        this.viewCount = viewCount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * 신규 게시물 생성용 Static Factory Method
     *
     * <p>ID 없이 DRAFT 상태로 생성
     *
     * @param boardType 게시판 타입
     * @param content 게시물 컨텐츠
     * @param displayPeriod 노출 기간
     * @param createdBy 생성자 ID
     * @param createdAt 생성일시
     * @return Board 인스턴스
     */
    public static Board forNew(
            BoardType boardType,
            BoardContent content,
            DisplayPeriod displayPeriod,
            Long createdBy,
            Instant createdAt) {
        validateCreate(boardType, content);
        return new Board(
                BoardId.forNew(),
                boardType,
                content,
                PinSetting.unpinned(),
                displayPeriod != null ? displayPeriod : DisplayPeriod.unlimited(),
                BoardStatus.DRAFT,
                0L,
                createdBy,
                createdBy,
                createdAt,
                createdAt,
                null);
    }

    /**
     * Persistence에서 복원용 Static Factory Method
     *
     * <p>검증 없이 모든 필드를 그대로 복원
     *
     * @param id 게시물 ID
     * @param boardType 게시판 타입
     * @param content 게시물 컨텐츠
     * @param pinSetting 고정 설정
     * @param displayPeriod 노출 기간
     * @param status 상태
     * @param viewCount 조회수
     * @param createdBy 생성자 ID
     * @param updatedBy 수정자 ID
     * @param createdAt 생성일시
     * @param updatedAt 수정일시
     * @param deletedAt 삭제일시
     * @return Board 인스턴스
     */
    public static Board reconstitute(
            BoardId id,
            BoardType boardType,
            BoardContent content,
            PinSetting pinSetting,
            DisplayPeriod displayPeriod,
            BoardStatus status,
            long viewCount,
            Long createdBy,
            Long updatedBy,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new Board(
                id,
                boardType,
                content,
                pinSetting,
                displayPeriod,
                status,
                viewCount,
                createdBy,
                updatedBy,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ========== 비즈니스 메서드 ==========

    /**
     * 게시물 게시 (DRAFT/HIDDEN → PUBLISHED)
     *
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 게시된 Board 인스턴스
     * @throws InvalidBoardStatusException 게시할 수 없는 상태일 때
     */
    public Board publish(Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        if (!status.canPublish()) {
            throw InvalidBoardStatusException.cannotPublish(status);
        }
        return new Board(
                this.id,
                this.boardType,
                this.content,
                this.pinSetting,
                this.displayPeriod,
                BoardStatus.PUBLISHED,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 게시물 숨김 (PUBLISHED → HIDDEN)
     *
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 숨김 처리된 Board 인스턴스
     * @throws InvalidBoardStatusException 숨김 처리할 수 없는 상태일 때
     */
    public Board hide(Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        if (!status.canHide()) {
            throw InvalidBoardStatusException.cannotHide(status);
        }
        return new Board(
                this.id,
                this.boardType,
                this.content,
                this.pinSetting,
                this.displayPeriod,
                BoardStatus.HIDDEN,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 상단 고정
     *
     * @param order 고정 순서
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 상단 고정된 Board 인스턴스
     */
    public Board pin(int order, Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        PinSetting newPinSetting = PinSetting.pinned(order);
        return new Board(
                this.id,
                this.boardType,
                this.content,
                newPinSetting,
                this.displayPeriod,
                this.status,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 상단 고정 해제
     *
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 고정 해제된 Board 인스턴스
     */
    public Board unpin(Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        return new Board(
                this.id,
                this.boardType,
                this.content,
                PinSetting.unpinned(),
                this.displayPeriod,
                this.status,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 컨텐츠 업데이트
     *
     * @param newContent 새로운 컨텐츠
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 컨텐츠가 업데이트된 Board 인스턴스
     */
    public Board updateContent(BoardContent newContent, Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        return new Board(
                this.id,
                this.boardType,
                newContent,
                this.pinSetting,
                this.displayPeriod,
                this.status,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 노출 기간 업데이트
     *
     * @param newDisplayPeriod 새로운 노출 기간
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 노출 기간이 업데이트된 Board 인스턴스
     */
    public Board updateDisplayPeriod(
            DisplayPeriod newDisplayPeriod, Long updatedBy, Instant updatedAt) {
        validateNotDeleted();
        return new Board(
                this.id,
                this.boardType,
                this.content,
                this.pinSetting,
                newDisplayPeriod,
                this.status,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 전체 업데이트
     *
     * @param newContent 새로운 컨텐츠
     * @param newDisplayPeriod 새로운 노출 기간
     * @param updatedBy 수정자 ID
     * @param updatedAt 수정일시
     * @return 업데이트된 Board 인스턴스
     */
    public Board update(
            BoardContent newContent,
            DisplayPeriod newDisplayPeriod,
            Long updatedBy,
            Instant updatedAt) {
        validateNotDeleted();
        return new Board(
                this.id,
                this.boardType,
                newContent,
                this.pinSetting,
                newDisplayPeriod,
                this.status,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                updatedAt,
                null);
    }

    /**
     * 조회수 증가
     *
     * @return 조회수가 증가된 Board 인스턴스
     */
    public Board incrementViewCount() {
        return new Board(
                this.id,
                this.boardType,
                this.content,
                this.pinSetting,
                this.displayPeriod,
                this.status,
                this.viewCount + 1,
                this.createdBy,
                this.updatedBy,
                this.createdAt,
                this.updatedAt,
                null);
    }

    /**
     * Soft Delete
     *
     * @param updatedBy 삭제자 ID
     * @param deletedAt 삭제일시
     * @return 삭제된 Board 인스턴스
     * @throws InvalidBoardStatusException 이미 삭제된 경우
     */
    public Board softDelete(Long updatedBy, Instant deletedAt) {
        validateNotDeleted();
        return new Board(
                this.id,
                this.boardType,
                this.content,
                PinSetting.unpinned(),
                this.displayPeriod,
                BoardStatus.HIDDEN,
                this.viewCount,
                this.createdBy,
                updatedBy,
                this.createdAt,
                deletedAt,
                deletedAt);
    }

    // ========== 상태 확인 메서드 ==========

    /**
     * 현재 노출 가능 여부 확인
     *
     * <p>게시 상태이고, 노출 기간 내이고, 삭제되지 않았으면 true
     *
     * @return 노출 가능하면 true
     */
    public boolean isDisplayable() {
        return !isDeleted() && status.isDisplayable() && displayPeriod.isCurrentlyDisplayable();
    }

    /**
     * 삭제 여부 확인
     *
     * @return 삭제되었으면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 상단 고정 여부 확인
     *
     * @return 고정되었으면 true
     */
    public boolean isPinned() {
        return pinSetting.isPinned();
    }

    /**
     * ID 존재 여부 확인 (영속화 여부)
     *
     * @return ID가 있으면 true
     */
    public boolean hasId() {
        return id != null && !id.isNew();
    }

    // ========== Law of Demeter Helper Methods ==========

    /**
     * 게시물 ID 값 반환 (Law of Demeter 준수)
     *
     * @return 게시물 ID Long 값, ID가 없으면 null
     */
    public Long getIdValue() {
        return id != null ? id.value() : null;
    }

    /**
     * 제목 반환 (Law of Demeter 준수)
     *
     * @return 제목 문자열
     */
    public String getTitle() {
        return content != null ? content.title() : null;
    }

    /**
     * 본문 반환 (Law of Demeter 준수)
     *
     * @return 본문 문자열
     */
    public String getContentValue() {
        return content != null ? content.content() : null;
    }

    /**
     * 요약 반환 (Law of Demeter 준수)
     *
     * @return 요약 문자열
     */
    public String getSummary() {
        return content != null ? content.summary() : null;
    }

    /**
     * 썸네일 URL 반환 (Law of Demeter 준수)
     *
     * @return 썸네일 URL 문자열
     */
    public String getThumbnailUrl() {
        return content != null ? content.thumbnailUrl() : null;
    }

    /**
     * 고정 순서 반환 (Law of Demeter 준수)
     *
     * @return 고정 순서
     */
    public int getPinOrder() {
        return pinSetting != null ? pinSetting.pinOrder() : 0;
    }

    /**
     * 노출 시작일시 반환 (Law of Demeter 준수)
     *
     * @return 노출 시작일시
     */
    public Instant getDisplayStartAt() {
        return displayPeriod != null ? displayPeriod.startAt() : null;
    }

    /**
     * 노출 종료일시 반환 (Law of Demeter 준수)
     *
     * @return 노출 종료일시
     */
    public Instant getDisplayEndAt() {
        return displayPeriod != null ? displayPeriod.endAt() : null;
    }

    // ========== 검증 메서드 ==========

    private static void validateCreate(BoardType boardType, BoardContent content) {
        if (boardType == null) {
            throw new IllegalArgumentException("BoardType is required");
        }
        if (content == null) {
            throw new IllegalArgumentException("BoardContent is required");
        }
    }

    private void validateNotDeleted() {
        if (isDeleted()) {
            throw InvalidBoardStatusException.alreadyDeleted();
        }
    }

    // ========== Getter 메서드 (Lombok 금지) ==========

    public BoardId getId() {
        return id;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public BoardContent getContent() {
        return content;
    }

    public PinSetting getPinSetting() {
        return pinSetting;
    }

    public DisplayPeriod getDisplayPeriod() {
        return displayPeriod;
    }

    public BoardStatus getStatus() {
        return status;
    }

    public long getViewCount() {
        return viewCount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
