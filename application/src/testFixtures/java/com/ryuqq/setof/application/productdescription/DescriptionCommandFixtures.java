package com.ryuqq.setof.application.productdescription;

import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;

/**
 * ProductDescription Application Command 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class DescriptionCommandFixtures {

    private DescriptionCommandFixtures() {}

    // ===== 상수 =====
    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;
    public static final String DEFAULT_CONTENT = "<p>상품 상세설명입니다</p>";
    public static final String DEFAULT_CDN_PATH = "https://cdn.example.com/descriptions/100";

    public static final String UPDATED_CONTENT =
            "<p>수정된 상세설명입니다</p><img src=\"https://cdn.example.com/img.jpg\"/>";
    public static final String UPDATED_CDN_PATH = "https://cdn.example.com/descriptions/100/v2";

    // ===== RegisterProductGroupDescriptionCommand Fixtures =====

    public static RegisterProductGroupDescriptionCommand registerCommand() {
        return new RegisterProductGroupDescriptionCommand(
                DEFAULT_PRODUCT_GROUP_ID, DEFAULT_CONTENT, DEFAULT_CDN_PATH);
    }

    public static RegisterProductGroupDescriptionCommand registerCommand(long productGroupId) {
        return new RegisterProductGroupDescriptionCommand(
                productGroupId, DEFAULT_CONTENT, DEFAULT_CDN_PATH);
    }

    public static RegisterProductGroupDescriptionCommand registerCommandWithoutCdnPath() {
        return new RegisterProductGroupDescriptionCommand(
                DEFAULT_PRODUCT_GROUP_ID, DEFAULT_CONTENT, null);
    }

    // ===== UpdateProductGroupDescriptionCommand Fixtures =====

    public static UpdateProductGroupDescriptionCommand updateCommand() {
        return new UpdateProductGroupDescriptionCommand(
                DEFAULT_PRODUCT_GROUP_ID, UPDATED_CONTENT, UPDATED_CDN_PATH);
    }

    public static UpdateProductGroupDescriptionCommand updateCommand(long productGroupId) {
        return new UpdateProductGroupDescriptionCommand(
                productGroupId, UPDATED_CONTENT, UPDATED_CDN_PATH);
    }

    public static UpdateProductGroupDescriptionCommand updateCommandWithSameContent() {
        return new UpdateProductGroupDescriptionCommand(
                DEFAULT_PRODUCT_GROUP_ID, DEFAULT_CONTENT, UPDATED_CDN_PATH);
    }
}
