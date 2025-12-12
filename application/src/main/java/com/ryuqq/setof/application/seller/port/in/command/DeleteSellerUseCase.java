package com.ryuqq.setof.application.seller.port.in.command;

import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;

/**
 * Delete Seller UseCase (Command)
 *
 * <p>셀러 삭제(소프트 삭제)를 담당하는 Inbound Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface DeleteSellerUseCase {

    /**
     * 셀러 삭제 실행 (소프트 삭제)
     *
     * @param command 삭제 명령
     */
    void execute(DeleteSellerCommand command);
}
