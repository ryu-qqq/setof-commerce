package com.ryuqq.setof.adapter.in.rest.v2.review.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.review.dto.command.CreateReviewV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.command.UpdateReviewV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.review.dto.query.ReviewV2SearchApiRequest;
import com.ryuqq.setof.application.review.dto.command.CreateReviewCommand;
import com.ryuqq.setof.application.review.dto.command.DeleteReviewCommand;
import com.ryuqq.setof.application.review.dto.command.UpdateReviewCommand;
import com.ryuqq.setof.application.review.dto.query.ReviewSearchQuery;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Review V2 API Mapper
 *
 * <p>REST API Request/Response 변환을 담당하는 Mapper입니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>API Request → Application Command DTO 변환
 *   <li>API Request → Application Query DTO 변환
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ReviewV2ApiMapper {

    /**
     * CreateReviewV2ApiRequest → CreateReviewCommand 변환
     *
     * @param request API 요청
     * @param memberId 인증된 회원 ID (UUID)
     * @return Application Layer 명령
     */
    public CreateReviewCommand toCreateCommand(CreateReviewV2ApiRequest request, UUID memberId) {
        return new CreateReviewCommand(
                memberId,
                request.orderId(),
                request.productGroupId(),
                request.rating(),
                request.content(),
                request.imageUrls());
    }

    /**
     * UpdateReviewV2ApiRequest → UpdateReviewCommand 변환
     *
     * @param reviewId 수정할 리뷰 ID
     * @param request API 요청
     * @param memberId 인증된 회원 ID (UUID)
     * @return Application Layer 명령
     */
    public UpdateReviewCommand toUpdateCommand(
            Long reviewId, UpdateReviewV2ApiRequest request, UUID memberId) {
        return new UpdateReviewCommand(
                reviewId, memberId, request.rating(), request.content(), request.imageUrls());
    }

    /**
     * DeleteReviewCommand 생성
     *
     * @param reviewId 삭제할 리뷰 ID
     * @param memberId 인증된 회원 ID (UUID)
     * @return Application Layer 명령
     */
    public DeleteReviewCommand toDeleteCommand(Long reviewId, UUID memberId) {
        return new DeleteReviewCommand(reviewId, memberId);
    }

    /**
     * 상품 그룹 ID + ReviewV2SearchApiRequest → ReviewSearchQuery 변환
     *
     * @param productGroupId 상품 그룹 ID
     * @param request 검색 조건
     * @return Application Layer 검색 조건
     */
    public ReviewSearchQuery toProductSearchQuery(
            Long productGroupId, ReviewV2SearchApiRequest request) {
        return ReviewSearchQuery.forProduct(
                productGroupId, request.pageNumber(), request.pageSize());
    }

    /**
     * 회원 ID + ReviewV2SearchApiRequest → ReviewSearchQuery 변환
     *
     * @param memberId 회원 ID (UUID)
     * @param request 검색 조건
     * @return Application Layer 검색 조건
     */
    public ReviewSearchQuery toMemberSearchQuery(UUID memberId, ReviewV2SearchApiRequest request) {
        return ReviewSearchQuery.forUser(memberId, request.pageNumber(), request.pageSize());
    }
}
