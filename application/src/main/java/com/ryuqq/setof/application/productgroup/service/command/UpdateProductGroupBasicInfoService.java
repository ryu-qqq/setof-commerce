package com.ryuqq.setof.application.productgroup.service.command;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.application.productgroup.factory.ProductGroupCommandFactory;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupCommandCoordinator;
import com.ryuqq.setof.application.productgroup.manager.ProductGroupReadManager;
import com.ryuqq.setof.application.productgroup.port.in.command.UpdateProductGroupBasicInfoUseCase;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import org.springframework.stereotype.Service;

/**
 * UpdateProductGroupBasicInfoService - 상품그룹 기본정보 수정 서비스.
 *
 * <p>ReadManager로 기존 상품그룹을 조회하고, CommandFactory로 수정 데이터를 생성한 뒤, ProductGroupCommandCoordinator에 수정을
 * 위임합니다.
 */
@Service
public class UpdateProductGroupBasicInfoService implements UpdateProductGroupBasicInfoUseCase {

    private final ProductGroupReadManager readManager;
    private final ProductGroupCommandFactory commandFactory;
    private final ProductGroupCommandCoordinator commandCoordinator;
    private final TimeProvider timeProvider;

    public UpdateProductGroupBasicInfoService(
            ProductGroupReadManager readManager,
            ProductGroupCommandFactory commandFactory,
            ProductGroupCommandCoordinator commandCoordinator,
            TimeProvider timeProvider) {
        this.readManager = readManager;
        this.commandFactory = commandFactory;
        this.commandCoordinator = commandCoordinator;
        this.timeProvider = timeProvider;
    }

    @Override
    public void execute(UpdateProductGroupBasicInfoCommand command) {
        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        ProductGroup productGroup = readManager.getById(productGroupId);

        ProductGroupUpdateData updateData =
                commandFactory.createUpdateData(
                        command, productGroup.optionType(), timeProvider.now());

        commandCoordinator.updateBasicInfo(productGroup, updateData);
    }
}
