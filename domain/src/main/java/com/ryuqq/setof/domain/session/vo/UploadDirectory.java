package com.ryuqq.setof.domain.session.vo;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 허용된 업로드 디렉토리 화이트리스트.
 *
 * <p>레거시 ImagePath enum과 1:1 매핑됩니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public enum UploadDirectory {
    PRODUCT("product/"),
    DESCRIPTION("description/"),
    QNA("qna/"),
    CONTENT("content/"),
    IMAGE_COMPONENT("content/image/"),
    BANNER("content/banner/"),
    REVIEW("review/");

    private final String path;

    private static final Map<String, UploadDirectory> BY_NAME =
            Stream.of(values()).collect(Collectors.toMap(Enum::name, d -> d));

    UploadDirectory(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    /**
     * enum name으로 변환 (레거시 ImagePath name 호환).
     *
     * @param name enum 이름 (예: "PRODUCT", "DESCRIPTION")
     * @return UploadDirectory
     * @throws IllegalArgumentException 허용되지 않은 디렉토리
     */
    public static UploadDirectory fromName(String name) {
        UploadDirectory result = BY_NAME.get(name);
        if (result == null) {
            throw new IllegalArgumentException(
                    "허용되지 않은 업로드 디렉토리입니다: " + name + ". 허용 목록: " + BY_NAME.keySet());
        }
        return result;
    }
}
