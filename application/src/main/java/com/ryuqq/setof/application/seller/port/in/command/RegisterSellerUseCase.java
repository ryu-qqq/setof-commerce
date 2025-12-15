package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;

/**
 * Register Seller UseCase (Command)
 *
 * <p>셀러 등록을 담당하는 Inbound Port
 *
 * <p>비즈니스 로직:
 *
 * <ol>
 *   <li>사업자 등록번호 유효성 검증
 *   <li>CS 정보 최소 1개 존재 검증
 *   <li>Seller 도메인 생성
 *   <li>Seller 저장 (트랜잭션)
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterSellerUseCase {

    /**
     * 셀러 등록 실행
     *
     * @param command 셀러 등록 명령
     * @return 등록된 셀러 ID
     */
    Long execute(RegisterSellerCommand command);
}
