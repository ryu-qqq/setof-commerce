package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.claim.vo.ClaimId;
import com.ryuqq.setof.domain.claim.vo.ClaimNumber;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ClaimNotFoundException - 클레임을 찾을 수 없음 예외
 *
 * @author development-team
 * @since 2.0.0
 */
public class ClaimNotFoundException extends DomainException {

    /**
     * ClaimId로 조회 실패
     *
     * @param claimId 클레임 ID
     * @return ClaimNotFoundException
     */
    public static ClaimNotFoundException byId(ClaimId claimId) {
        return new ClaimNotFoundException("claimId", claimId.value());
    }

    /**
     * ClaimId 문자열로 조회 실패
     *
     * @param claimId 클레임 ID 문자열
     * @return ClaimNotFoundException
     */
    public static ClaimNotFoundException byId(String claimId) {
        return new ClaimNotFoundException("claimId", claimId);
    }

    /**
     * ClaimNumber로 조회 실패
     *
     * @param claimNumber 클레임 번호
     * @return ClaimNotFoundException
     */
    public static ClaimNotFoundException byNumber(ClaimNumber claimNumber) {
        return new ClaimNotFoundException("claimNumber", claimNumber.value());
    }

    /**
     * ClaimNumber 문자열로 조회 실패
     *
     * @param claimNumber 클레임 번호 문자열
     * @return ClaimNotFoundException
     */
    public static ClaimNotFoundException byNumber(String claimNumber) {
        return new ClaimNotFoundException("claimNumber", claimNumber);
    }

    private ClaimNotFoundException(String fieldName, String fieldValue) {
        super(
                ClaimErrorCode.CLAIM_NOT_FOUND,
                String.format("클레임을 찾을 수 없습니다 - %s: %s", fieldName, fieldValue),
                Map.of(fieldName, fieldValue));
    }
}
