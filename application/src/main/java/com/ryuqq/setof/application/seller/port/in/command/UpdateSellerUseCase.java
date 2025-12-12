package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;

/**
 * Update Seller UseCase (Command)
 *
 * <p>셀러 정보 수정을 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateSellerUseCase {

    /**
     * 셀러 정보 수정 실행
     *
     * @param command 셀러 수정 명령
     */
    void execute(UpdateSellerCommand command);
}
