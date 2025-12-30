package com.ryuqq.setof.domain.board.vo;

/**
 * 게시물 컨텐츠 Value Object
 *
 * <p>제목, 본문, 요약, 썸네일을 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record BoardContent(String title, String content, String summary, String thumbnailUrl) {

    private static final int MAX_TITLE_LENGTH = 500;
    private static final int MAX_SUMMARY_LENGTH = 500;
    private static final int MAX_THUMBNAIL_URL_LENGTH = 500;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException 제목이 null이거나 비어있을 때
     * @throws IllegalArgumentException 본문이 null일 때
     * @throws IllegalArgumentException 길이 제한 초과 시
     */
    public BoardContent {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("제목은 %d자 이하로 입력해주세요: %d", MAX_TITLE_LENGTH, title.length()));
        }
        if (content == null) {
            throw new IllegalArgumentException("본문은 필수입니다");
        }
        if (summary != null && summary.length() > MAX_SUMMARY_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("요약은 %d자 이하로 입력해주세요: %d", MAX_SUMMARY_LENGTH, summary.length()));
        }
        if (thumbnailUrl != null && thumbnailUrl.length() > MAX_THUMBNAIL_URL_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(
                            "썸네일 URL은 %d자 이하로 입력해주세요: %d",
                            MAX_THUMBNAIL_URL_LENGTH, thumbnailUrl.length()));
        }
    }

    /**
     * 컨텐츠 존재 여부 확인
     *
     * @return 본문이 있으면 true
     */
    public boolean hasContent() {
        return content != null && !content.isBlank();
    }

    /**
     * 썸네일 존재 여부 확인
     *
     * @return 썸네일이 있으면 true
     */
    public boolean hasThumbnail() {
        return thumbnailUrl != null && !thumbnailUrl.isBlank();
    }

    /**
     * 제목 업데이트
     *
     * @param newTitle 새로운 제목
     * @return 제목이 업데이트된 BoardContent
     */
    public BoardContent updateTitle(String newTitle) {
        return new BoardContent(newTitle, this.content, this.summary, this.thumbnailUrl);
    }

    /**
     * 본문 업데이트
     *
     * @param newContent 새로운 본문
     * @return 본문이 업데이트된 BoardContent
     */
    public BoardContent updateContent(String newContent) {
        return new BoardContent(this.title, newContent, this.summary, this.thumbnailUrl);
    }

    /**
     * 요약 업데이트
     *
     * @param newSummary 새로운 요약
     * @return 요약이 업데이트된 BoardContent
     */
    public BoardContent updateSummary(String newSummary) {
        return new BoardContent(this.title, this.content, newSummary, this.thumbnailUrl);
    }

    /**
     * 썸네일 업데이트
     *
     * @param newThumbnailUrl 새로운 썸네일 URL
     * @return 썸네일이 업데이트된 BoardContent
     */
    public BoardContent updateThumbnailUrl(String newThumbnailUrl) {
        return new BoardContent(this.title, this.content, this.summary, newThumbnailUrl);
    }
}
