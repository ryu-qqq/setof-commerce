package com.ryuqq.setof.application.seller;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.AddressCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.ContactInfoCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.CsContactCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.SellerAddressCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.SellerBusinessInfoCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand.SellerInfoCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerAddressCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerBusinessInfoCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerFullCommand;

/**
 * Seller Command 테스트 Fixtures.
 *
 * <p>Seller 관련 Command 객체들을 생성하는 테스트 유틸리티입니다.
 */
public final class SellerCommandFixtures {

    private SellerCommandFixtures() {}

    // ===== RegisterSellerCommand =====

    public static RegisterSellerCommand registerCommand() {
        return new RegisterSellerCommand(
                sellerInfoCommand(), sellerBusinessInfoCommand(), sellerAddressCommand());
    }

    public static RegisterSellerCommand registerCommand(String sellerName) {
        return new RegisterSellerCommand(
                sellerInfoCommand(sellerName), sellerBusinessInfoCommand(), sellerAddressCommand());
    }

    public static SellerInfoCommand sellerInfoCommand() {
        return sellerInfoCommand("테스트 셀러");
    }

    public static SellerInfoCommand sellerInfoCommand(String sellerName) {
        return new SellerInfoCommand(
                sellerName, "테스트 스토어", "https://example.com/logo.png", "테스트 셀러 설명입니다.");
    }

    public static SellerInfoCommand sellerInfoCommandWithoutOptionals() {
        return new SellerInfoCommand("테스트 셀러", "테스트 스토어", null, null);
    }

    public static SellerBusinessInfoCommand sellerBusinessInfoCommand() {
        return new SellerBusinessInfoCommand(
                "123-45-67890",
                "테스트 주식회사",
                "홍길동",
                "2024-서울강남-0001",
                addressCommand(),
                csContactCommand());
    }

    public static SellerBusinessInfoCommand sellerBusinessInfoCommandWithoutOptionals() {
        return new SellerBusinessInfoCommand(
                "123-45-67890", "테스트 주식회사", "홍길동", null, addressCommand(), csContactCommand());
    }

    public static SellerAddressCommand sellerAddressCommand() {
        return new SellerAddressCommand(
                "SHIPPING", "본사 창고", addressCommand(), contactInfoCommand(), true);
    }

    public static AddressCommand addressCommand() {
        return new AddressCommand("06141", "서울시 강남구 테헤란로 123", "테스트빌딩 5층");
    }

    public static CsContactCommand csContactCommand() {
        return new CsContactCommand("02-1234-5678", "cs@test.com", "010-1234-5678");
    }

    public static ContactInfoCommand contactInfoCommand() {
        return new ContactInfoCommand("김담당", "010-9876-5432");
    }

    // ===== UpdateSellerCommand =====

    public static UpdateSellerCommand updateSellerCommand(Long sellerId) {
        return new UpdateSellerCommand(
                sellerId, "수정된 셀러명", "수정된 스토어", "https://example.com/new-logo.png", "수정된 설명입니다.");
    }

    public static UpdateSellerCommand updateSellerCommandWithoutOptionals(Long sellerId) {
        return new UpdateSellerCommand(sellerId, "수정된 셀러명", "수정된 스토어", null, null);
    }

    // ===== UpdateSellerBusinessInfoCommand =====

    public static UpdateSellerBusinessInfoCommand updateBusinessInfoCommand(Long sellerId) {
        return new UpdateSellerBusinessInfoCommand(
                sellerId,
                "987-65-43210",
                "수정된 주식회사",
                "김철수",
                "2024-서울강남-0002",
                updateBusinessInfoAddressCommand(),
                updateBusinessInfoCsContactCommand());
    }

    public static UpdateSellerBusinessInfoCommand updateBusinessInfoCommandWithoutOptionals(
            Long sellerId) {
        return new UpdateSellerBusinessInfoCommand(
                sellerId,
                "987-65-43210",
                "수정된 주식회사",
                "김철수",
                null,
                updateBusinessInfoAddressCommand(),
                updateBusinessInfoCsContactCommand());
    }

    public static UpdateSellerBusinessInfoCommand.AddressCommand
            updateBusinessInfoAddressCommand() {
        return new UpdateSellerBusinessInfoCommand.AddressCommand(
                "06142", "서울시 강남구 역삼로 456", "수정빌딩 10층");
    }

    public static UpdateSellerBusinessInfoCommand.CsContactCommand
            updateBusinessInfoCsContactCommand() {
        return new UpdateSellerBusinessInfoCommand.CsContactCommand(
                "02-5678-1234", "newcs@test.com", "010-5678-1234");
    }

    // ===== UpdateSellerAddressCommand =====

    public static UpdateSellerAddressCommand updateAddressCommand(Long addressId) {
        return new UpdateSellerAddressCommand(
                addressId,
                "수정된 창고",
                updateSellerAddressAddressCommand(),
                updateSellerAddressContactInfoCommand());
    }

    public static UpdateSellerAddressCommand.AddressCommand updateSellerAddressAddressCommand() {
        return new UpdateSellerAddressCommand.AddressCommand(
                "54321", "부산시 해운대구 센텀로 100", "수정 물류센터");
    }

    public static UpdateSellerAddressCommand.ContactInfoCommand
            updateSellerAddressContactInfoCommand() {
        return new UpdateSellerAddressCommand.ContactInfoCommand("최담당", "010-3333-4444");
    }

    // ===== UpdateSellerFullCommand =====

    public static UpdateSellerFullCommand updateFullCommand(Long sellerId) {
        return new UpdateSellerFullCommand(
                sellerId,
                updateFullSellerInfoCommand(),
                updateFullBusinessInfoCommand(),
                updateFullAddressCommand(1L),
                updateFullCsInfoCommand(),
                updateFullContractInfoCommand(),
                updateFullSettlementInfoCommand());
    }

    public static UpdateSellerFullCommand.SellerInfoCommand updateFullSellerInfoCommand() {
        return new UpdateSellerFullCommand.SellerInfoCommand(
                "수정된 셀러명", "수정된 스토어", "https://example.com/new-logo.png", "수정된 설명입니다.");
    }

    public static UpdateSellerFullCommand.SellerInfoCommand
            updateFullSellerInfoCommandWithoutOptionals() {
        return new UpdateSellerFullCommand.SellerInfoCommand("수정된 셀러명", "수정된 스토어", null, null);
    }

    public static UpdateSellerFullCommand.SellerBusinessInfoCommand
            updateFullBusinessInfoCommand() {
        return new UpdateSellerFullCommand.SellerBusinessInfoCommand(
                "987-65-43210",
                "수정된 주식회사",
                "김철수",
                "2024-서울강남-0002",
                updateFullAddressCommand(),
                updateFullCsContactCommand());
    }

    public static UpdateSellerFullCommand.SellerBusinessInfoCommand
            updateFullBusinessInfoCommandWithoutOptionals() {
        return new UpdateSellerFullCommand.SellerBusinessInfoCommand(
                "987-65-43210",
                "수정된 주식회사",
                "김철수",
                null,
                updateFullAddressCommand(),
                updateFullCsContactCommand());
    }

    public static UpdateSellerFullCommand.SellerAddressCommand updateFullAddressCommand(
            Long addressId) {
        return new UpdateSellerFullCommand.SellerAddressCommand(
                addressId,
                "수정된 창고",
                updateFullAddressCommandInner(),
                updateFullContactInfoCommand());
    }

    public static UpdateSellerFullCommand.AddressCommand updateFullAddressCommand() {
        return new UpdateSellerFullCommand.AddressCommand("06142", "서울시 강남구 역삼로 456", "수정빌딩 10층");
    }

    public static UpdateSellerFullCommand.AddressCommand updateFullAddressCommandInner() {
        return new UpdateSellerFullCommand.AddressCommand("54321", "부산시 해운대구 센텀로 100", "수정 물류센터");
    }

    public static UpdateSellerFullCommand.CsContactCommand updateFullCsContactCommand() {
        return new UpdateSellerFullCommand.CsContactCommand(
                "02-5678-1234", "newcs@test.com", "010-5678-1234");
    }

    public static UpdateSellerFullCommand.ContactInfoCommand updateFullContactInfoCommand() {
        return new UpdateSellerFullCommand.ContactInfoCommand("최담당", "010-3333-4444");
    }

    public static UpdateSellerFullCommand.CsInfoCommand updateFullCsInfoCommand() {
        return new UpdateSellerFullCommand.CsInfoCommand(
                updateFullCsContactCommand(),
                updateFullOperatingHoursCommand(),
                "월,화,수,목,금",
                "https://pf.kakao.com/test");
    }

    public static UpdateSellerFullCommand.OperatingHoursCommand updateFullOperatingHoursCommand() {
        return new UpdateSellerFullCommand.OperatingHoursCommand("09:00", "18:00");
    }

    public static UpdateSellerFullCommand.ContractInfoCommand updateFullContractInfoCommand() {
        return new UpdateSellerFullCommand.ContractInfoCommand(
                10.5, "2025-01-01", "2025-12-31", "특약사항 없음");
    }

    public static UpdateSellerFullCommand.SettlementInfoCommand updateFullSettlementInfoCommand() {
        return new UpdateSellerFullCommand.SettlementInfoCommand(
                updateFullBankAccountCommand(), "MONTHLY", 15);
    }

    public static UpdateSellerFullCommand.BankAccountCommand updateFullBankAccountCommand() {
        return new UpdateSellerFullCommand.BankAccountCommand(
                "004", "국민은행", "12345678901234", "홍길동");
    }
}
