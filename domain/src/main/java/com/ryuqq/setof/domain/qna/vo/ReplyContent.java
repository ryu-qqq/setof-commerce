package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.qna.exception.InvalidQnaContentException;
import java.util.Objects;

public final class ReplyContent {

    private static final int MAX_CONTENT_LENGTH = 4000;

    private final String content;

    private ReplyContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public static ReplyContent of(String content) {
        return new ReplyContent(content);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidQnaContentException("답변 내용은 필수입니다.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidQnaContentException("답변 내용은 " + MAX_CONTENT_LENGTH + "자를 초과할 수 없습니다.");
        }
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
        ReplyContent that = (ReplyContent) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return "ReplyContent{content='" + content + "'}";
    }
}
