package com.ryuqq.setof.application.productdescription.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand.DescriptionImageCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupDescriptionCommandFactory - 상세설명 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupDescriptionCommandFactory {

    private final TimeProvider timeProvider;

    public ProductGroupDescriptionCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 등록 커맨드로부터 새 ProductGroupDescription 도메인 객체를 생성합니다.
     *
     * @param command 등록 커맨드
     * @return 새 ProductGroupDescription 인스턴스
     */
    public ProductGroupDescription createNewDescription(
            RegisterProductGroupDescriptionCommand command) {
        Instant now = timeProvider.now();
        return ProductGroupDescription.forNew(
                ProductGroupId.of(command.productGroupId()),
                DescriptionHtml.of(command.content()),
                null,
                now);
    }

    /**
     * 등록 커맨드의 이미지 목록으로부터 DescriptionImage 도메인 객체 목록을 생성합니다.
     *
     * @param imageCommands 이미지 커맨드 목록
     * @return DescriptionImage 도메인 객체 목록
     */
    public List<DescriptionImage> createNewImages(List<DescriptionImageCommand> imageCommands) {
        if (imageCommands == null || imageCommands.isEmpty()) {
            return List.of();
        }
        return imageCommands.stream()
                .map(cmd -> DescriptionImage.forNew(cmd.imageUrl(), cmd.sortOrder()))
                .toList();
    }

    /**
     * 수정 커맨드로부터 DescriptionUpdateData를 생성합니다.
     *
     * @param command 수정 커맨드
     * @return DescriptionUpdateData 인스턴스
     */
    public DescriptionUpdateData createUpdateData(UpdateProductGroupDescriptionCommand command) {
        Instant now = timeProvider.now();
        List<DescriptionImage> newImages = createUpdateImages(command.descriptionImages());
        return DescriptionUpdateData.of(
                DescriptionHtml.of(command.content()), null, newImages, now);
    }

    private List<DescriptionImage> createUpdateImages(
            List<UpdateProductGroupDescriptionCommand.DescriptionImageCommand> imageCommands) {
        if (imageCommands == null || imageCommands.isEmpty()) {
            return List.of();
        }
        return imageCommands.stream()
                .map(cmd -> DescriptionImage.forNew(cmd.imageUrl(), cmd.sortOrder()))
                .toList();
    }
}
