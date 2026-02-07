package com.ryuqq.setof.storage.legacy.composite.web.board.dto;

/**
 * LegacyWebBoardQueryDto - 레거시 게시판 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (Q클래스 생성 불필요).
 *
 * @param title 제목
 * @param contents 내용
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBoardQueryDto(String title, String contents) {}
