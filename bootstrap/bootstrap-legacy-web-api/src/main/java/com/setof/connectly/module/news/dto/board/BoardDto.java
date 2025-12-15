package com.setof.connectly.module.news.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {

    private String title;
    private String contents;

    @QueryProjection
    public BoardDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
