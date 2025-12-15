package com.ryuqq.setof.application.product.dto.command;

import com.ryuqq.setof.application.productdescription.dto.command.DescriptionImageDto;
import java.util.List;

/**
 * 상품설명 Command DTO
 *
 * <p>상품그룹 상세설명 등록/수정 데이터
 *
 * @param id 설명 ID (수정 시 사용, null이면 신규)
 * @param htmlContent HTML 컨텐츠
 * @param images 이미지 목록
 * @author development-team
 * @since 1.0.0
 */
public record ProductDescriptionCommandDto(
        Long id, String htmlContent, List<DescriptionImageDto> images) {}
