package com.ryuqq.setof.application.seller;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;

/**
 * Seller Application Command 테스트 Fixtures.
 *
 * <p>Seller 등록/수정 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerCommandFixtures {

    private SellerCommandFixtures() {}

    // ===== RegisterSellerCommand =====

    public static RegisterSellerCommand registerCommand() {
        return new RegisterSellerCommand(sellerInfoCommand(), sellerBusinessInfoCommand());
    }

    public static RegisterSellerCommand.SellerInfoCommand sellerInfoCommand() {
        return new RegisterSellerCommand.SellerInfoCommand(
                "테스트셀러", "테스트스토어", "http://example.com/logo.png", "테스트 셀러 설명");
    }

    public static RegisterSellerCommand.SellerBusinessInfoCommand sellerBusinessInfoCommand() {
        return new RegisterSellerCommand.SellerBusinessInfoCommand(
                "123-45-67890",
                "테스트 주식회사",
                "홍길동",
                "2024-서울-0001",
                addressCommand(),
                csContactCommand());
    }

    public static RegisterSellerCommand.AddressCommand addressCommand() {
        return new RegisterSellerCommand.AddressCommand("12345", "서울시 강남구 테헤란로 1", "101호");
    }

    public static RegisterSellerCommand.CsContactCommand csContactCommand() {
        return new RegisterSellerCommand.CsContactCommand(
                "02-1234-5678", "cs@test.com", "010-1234-5678");
    }

    // ===== UpdateSellerCommand =====

    public static UpdateSellerCommand updateCommand(Long sellerId) {
        return new UpdateSellerCommand(
                sellerId,
                "수정된셀러",
                "수정된스토어",
                "http://example.com/new-logo.png",
                "수정된 설명",
                csInfoCommand(),
                businessInfoCommand());
    }

    public static UpdateSellerCommand updateCommandWithoutOptional(Long sellerId) {
        return new UpdateSellerCommand(
                sellerId,
                "수정된셀러",
                "수정된스토어",
                "http://example.com/new-logo.png",
                "수정된 설명",
                null,
                null);
    }

    public static UpdateSellerCommand.CsInfoCommand csInfoCommand() {
        return new UpdateSellerCommand.CsInfoCommand(
                "02-9876-5432", "new-cs@test.com", "010-9876-5432");
    }

    public static UpdateSellerCommand.BusinessInfoCommand businessInfoCommand() {
        return new UpdateSellerCommand.BusinessInfoCommand(
                "123-45-67890", "수정된 주식회사", "김철수", "2024-서울-0002", updateAddressCommand());
    }

    public static UpdateSellerCommand.AddressCommand updateAddressCommand() {
        return new UpdateSellerCommand.AddressCommand("54321", "서울시 서초구 반포대로 1", "202호");
    }
}
