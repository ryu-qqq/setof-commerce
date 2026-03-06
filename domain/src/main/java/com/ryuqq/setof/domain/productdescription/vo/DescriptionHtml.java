package com.ryuqq.setof.domain.productdescription.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 상세설명 HTML 컨텐츠 Value Object.
 *
 * <p>상품 상세설명의 HTML 내용을 표현합니다.
 */
public record DescriptionHtml(String value) {

    private static final int MAX_LENGTH = 500_000;

    private static final Pattern IMG_SRC_PATTERN =
            Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);

    public DescriptionHtml {
        if (value != null) {
            value = value.trim();
            if (value.length() > MAX_LENGTH) {
                throw new IllegalArgumentException("상세설명은 " + MAX_LENGTH + "자를 초과할 수 없습니다");
            }
            if (value.isEmpty()) {
                value = null;
            }
        }
    }

    public static DescriptionHtml of(String value) {
        return new DescriptionHtml(value);
    }

    public static DescriptionHtml empty() {
        return new DescriptionHtml(null);
    }

    public boolean isEmpty() {
        return value == null || value.isBlank();
    }

    /** HTML 콘텐츠에서 &lt;img&gt; 태그의 src URL을 등장 순서대로 추출합니다. */
    public List<String> extractImageUrls() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        Matcher matcher = IMG_SRC_PATTERN.matcher(value);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group(1));
        }
        return Collections.unmodifiableList(urls);
    }
}
