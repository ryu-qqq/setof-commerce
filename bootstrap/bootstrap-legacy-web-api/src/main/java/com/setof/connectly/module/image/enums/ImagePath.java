package com.setof.connectly.module.image.enums;

import com.setof.connectly.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImagePath implements EnumType {
    PRODUCT("product/"),
    DESCRIPTION("description/"),
    QNA("qna/"),
    CONTENT("content/"),
    IMAGE_COMPONENT("content/image/"),
    BANNER("content/banner/"),
    REVIEW("review/");

    private final String path;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return name();
    }
}
