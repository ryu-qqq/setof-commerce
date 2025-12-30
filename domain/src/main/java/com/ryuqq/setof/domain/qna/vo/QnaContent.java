package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.qna.exception.InvalidQnaContentException;
import java.util.Objects;

public final class QnaContent {

    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_CONTENT_LENGTH = 4000;

    private final String title;
    private final String content;

    private QnaContent(String title, String content) {
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
    }

    public static QnaContent of(String title, String content) {
        return new QnaContent(title, content);
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidQnaContentException("제목은 필수입니다.");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidQnaContentException("제목은 " + MAX_TITLE_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidQnaContentException("내용은 필수입니다.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidQnaContentException("내용은 " + MAX_CONTENT_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QnaContent that = (QnaContent) o;
        return Objects.equals(title, that.title) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }

    @Override
    public String toString() {
        return "QnaContent{title='" + title + "', content='" + content + "'}";
    }
}
