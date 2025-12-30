package com.ryuqq.setof.domain.qna.vo;

import com.ryuqq.setof.domain.qna.exception.InvalidReplyPathException;
import java.util.Objects;

public final class ReplyPath {

    private static final String PATH_SEPARATOR = "/";
    private static final int MAX_PATH_LENGTH = 1000;

    private final String path;

    private ReplyPath(String path) {
        validatePath(path);
        this.path = path;
    }

    public static ReplyPath createRoot(long replyId) {
        return new ReplyPath(String.valueOf(replyId));
    }

    public static ReplyPath createChild(ReplyPath parentPath, long replyId) {
        String newPath = parentPath.getPath() + PATH_SEPARATOR + replyId;
        return new ReplyPath(newPath);
    }

    public static ReplyPath of(String path) {
        return new ReplyPath(path);
    }

    private void validatePath(String path) {
        if (path == null || path.isBlank()) {
            throw new InvalidReplyPathException("경로는 필수입니다.");
        }
        if (path.length() > MAX_PATH_LENGTH) {
            throw new InvalidReplyPathException("경로가 너무 깁니다.");
        }
    }

    public String getPath() {
        return path;
    }

    public int getDepth() {
        return (int) path.chars().filter(ch -> ch == '/').count();
    }

    public boolean isDescendantOf(ReplyPath ancestor) {
        return path.startsWith(ancestor.getPath() + PATH_SEPARATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplyPath replyPath = (ReplyPath) o;
        return Objects.equals(path, replyPath.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "ReplyPath{path='" + path + "'}";
    }
}
