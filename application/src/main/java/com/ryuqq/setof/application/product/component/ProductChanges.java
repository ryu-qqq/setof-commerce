package com.ryuqq.setof.application.product.component;

import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

/**
 * 상품 변경 결과
 *
 * <p>기존 데이터와 새 데이터 비교 후 변경 사항을 담는 불변 객체
 *
 * @param updatedProductGroup 수정된 ProductGroup (변경 없으면 null)
 * @param imagesToUpdate 수정할 이미지 목록
 * @param imageDtosToAdd 추가할 이미지 DTO 목록
 * @param imageIdsToDelete 삭제할 이미지 ID 목록
 * @param updatedDescription 수정된 상세설명 (변경 없으면 null)
 * @param updatedNotice 수정된 고시정보 (변경 없으면 null)
 * @author development-team
 * @since 1.0.0
 */
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record ProductChanges(
        ProductGroup updatedProductGroup,
        List<ProductImage> imagesToUpdate,
        List<ProductImageCommandDto> imageDtosToAdd,
        List<Long> imageIdsToDelete,
        ProductDescription updatedDescription,
        ProductNotice updatedNotice) {

    /**
     * ProductGroup 변경이 있는지 확인
     *
     * @return 변경이 있으면 true
     */
    public boolean hasProductGroupChanges() {
        return updatedProductGroup != null;
    }

    /**
     * 이미지 변경이 있는지 확인
     *
     * @return 변경이 있으면 true
     */
    public boolean hasImageChanges() {
        return !imagesToUpdate.isEmpty()
                || !imageDtosToAdd.isEmpty()
                || !imageIdsToDelete.isEmpty();
    }

    /**
     * 상세설명 변경이 있는지 확인
     *
     * @return 변경이 있으면 true
     */
    public boolean hasDescriptionChanges() {
        return updatedDescription != null;
    }

    /**
     * 고시정보 변경이 있는지 확인
     *
     * @return 변경이 있으면 true
     */
    public boolean hasNoticeChanges() {
        return updatedNotice != null;
    }

    /**
     * 전체 변경이 없는지 확인
     *
     * @return 변경이 없으면 true
     */
    public boolean hasNoChanges() {
        return !hasProductGroupChanges()
                && !hasImageChanges()
                && !hasDescriptionChanges()
                && !hasNoticeChanges();
    }

    /**
     * 빈 변경 결과 생성
     *
     * @return 변경이 없는 ProductChanges
     */
    public static ProductChanges empty() {
        return new ProductChanges(null, List.of(), List.of(), List.of(), null, null);
    }

    /**
     * Builder 생성
     *
     * @return Builder 인스턴스
     */
    public static Builder builder() {
        return new Builder();
    }

    /** ProductChanges Builder */
    public static class Builder {
        private ProductGroup updatedProductGroup;
        private List<ProductImage> imagesToUpdate = List.of();
        private List<ProductImageCommandDto> imageDtosToAdd = List.of();
        private List<Long> imageIdsToDelete = List.of();
        private ProductDescription updatedDescription;
        private ProductNotice updatedNotice;

        public Builder updatedProductGroup(ProductGroup productGroup) {
            this.updatedProductGroup = productGroup;
            return this;
        }

        public Builder imagesToUpdate(List<ProductImage> images) {
            this.imagesToUpdate = images != null ? images : List.of();
            return this;
        }

        public Builder imageDtosToAdd(List<ProductImageCommandDto> dtos) {
            this.imageDtosToAdd = dtos != null ? dtos : List.of();
            return this;
        }

        public Builder imageIdsToDelete(List<Long> ids) {
            this.imageIdsToDelete = ids != null ? ids : List.of();
            return this;
        }

        public Builder updatedDescription(ProductDescription description) {
            this.updatedDescription = description;
            return this;
        }

        public Builder updatedNotice(ProductNotice notice) {
            this.updatedNotice = notice;
            return this;
        }

        public ProductChanges build() {
            return new ProductChanges(
                    updatedProductGroup,
                    imagesToUpdate,
                    imageDtosToAdd,
                    imageIdsToDelete,
                    updatedDescription,
                    updatedNotice);
        }
    }
}
