package com.ryuqq.setof.domain.category.vo;

import com.ryuqq.setof.domain.category.exception.InvalidCategoryPathException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 경로 Value Object (Path Enumeration)
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java Record 사용
 *   <li>불변성 보장 - Java Record 특성
 *   <li>Path Enumeration 형식 검증 (예: "/1/5/23/")
 * </ul>
 *
 * <p>Path Enumeration 패턴:
 *
 * <ul>
 *   <li>슬래시(/)로 시작하고 끝남
 *   <li>ID들이 슬래시로 구분됨
 *   <li>예: 최상위 카테고리 "/1/", 2단계 "/1/5/", 3단계 "/1/5/23/"
 * </ul>
 *
 * @param value 카테고리 경로 값 (최대 500자)
 */
public record CategoryPath(String value) {

    private static final int MAX_LENGTH = 500;
    private static final String PATH_SEPARATOR = "/";

    /** Compact Constructor - 검증 로직 */
    public CategoryPath {
        validate(value);
    }

    /**
     * Static Factory Method
     *
     * @param value 카테고리 경로 문자열
     * @return CategoryPath 인스턴스
     * @throws InvalidCategoryPathException value가 유효하지 않은 경우
     */
    public static CategoryPath of(String value) {
        return new CategoryPath(value);
    }

    /**
     * 경로에서 ID 목록 추출
     *
     * @return 상위부터 하위까지의 카테고리 ID 목록
     */
    public List<Long> extractIds() {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(PATH_SEPARATOR))
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    /**
     * 경로 깊이 계산
     *
     * @return 경로 깊이 (ID 개수)
     */
    public int depth() {
        return extractIds().size();
    }

    /**
     * 특정 ID가 경로에 포함되어 있는지 확인
     *
     * @param categoryId 확인할 카테고리 ID
     * @return 포함되어 있으면 true
     */
    public boolean contains(Long categoryId) {
        return extractIds().contains(categoryId);
    }

    /**
     * 현재 경로가 주어진 경로의 하위인지 확인
     *
     * @param parentPath 상위 경로
     * @return 하위 경로이면 true
     */
    public boolean isChildOf(CategoryPath parentPath) {
        return value.startsWith(parentPath.value()) && !value.equals(parentPath.value());
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCategoryPathException(value);
        }
        if (value.length() > MAX_LENGTH) {
            throw new InvalidCategoryPathException(value);
        }
        // Path는 "/"로 시작하고 "/"로 끝나야 함
        if (!value.startsWith(PATH_SEPARATOR) || !value.endsWith(PATH_SEPARATOR)) {
            throw new InvalidCategoryPathException(value);
        }
        // Path 내부의 값들이 숫자인지 검증
        String[] parts = value.split(PATH_SEPARATOR);
        for (String part : parts) {
            if (!part.isBlank()) {
                try {
                    Long.parseLong(part);
                } catch (NumberFormatException e) {
                    throw new InvalidCategoryPathException(value);
                }
            }
        }
    }
}
