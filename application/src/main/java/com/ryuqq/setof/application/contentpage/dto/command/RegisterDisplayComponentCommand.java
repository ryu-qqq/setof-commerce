package com.ryuqq.setof.application.contentpage.dto.command;

import java.time.Instant;

/**
 * RegisterDisplayComponentCommand - 디스플레이 컴포넌트 등록/수정 Command.
 *
 * <p>APP-CMD-001: Command는 record 기반 순수 데이터 객체입니다. 컴포넌트 타입별 세부 데이터는 specData(JSON 문자열)로 전달됩니다.
 *
 * @param componentId 컴포넌트 ID (null이면 신규, 값이 있으면 기존 수정)
 * @param displayOrder 노출 순서
 * @param componentType 컴포넌트 타입 (PRODUCT, BRAND, CATEGORY, TAB, IMAGE, TEXT, TITLE, BLANK)
 * @param componentName 컴포넌트 이름
 * @param displayStartAt 전시 시작일
 * @param displayEndAt 전시 종료일
 * @param active 전시 여부
 * @param listType 리스트 타입
 * @param orderType 정렬 타입
 * @param badgeType 뱃지 타입
 * @param filterEnabled 필터 사용 여부
 * @param exposedProducts 노출 상품 수
 * @param viewExtensionCommand 뷰 확장 설정 (nullable)
 * @param specData 컴포넌트 타입별 세부 데이터 (JSON 문자열)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record RegisterDisplayComponentCommand(
        Long componentId,
        int displayOrder,
        String componentType,
        String componentName,
        Instant displayStartAt,
        Instant displayEndAt,
        boolean active,
        String listType,
        String orderType,
        String badgeType,
        boolean filterEnabled,
        int exposedProducts,
        ViewExtensionCommand viewExtensionCommand,
        String specData) {}
