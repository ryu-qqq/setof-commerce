package com.setof.connectly.module.display.repository.component;

import static com.setof.connectly.module.display.entity.component.QComponent.component;
import static com.setof.connectly.module.display.entity.component.item.QImageComponentItem.imageComponentItem;
import static com.setof.connectly.module.display.entity.component.sub.QBlankComponent.blankComponent;
import static com.setof.connectly.module.display.entity.component.sub.QImageComponent.imageComponent;
import static com.setof.connectly.module.display.entity.component.sub.QTextComponent.textComponent;
import static com.setof.connectly.module.display.entity.component.sub.QTitleComponent.titleComponent;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.component.NotProductRelatedComponents;
import com.setof.connectly.module.display.dto.component.QNotProductRelatedComponents;
import com.setof.connectly.module.display.dto.query.blank.QBlankComponentQueryDto;
import com.setof.connectly.module.display.dto.query.image.QImageComponentQueryDto;
import com.setof.connectly.module.display.dto.query.text.QTextComponentQueryDto;
import com.setof.connectly.module.display.dto.query.title.QTitleComponentQueryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotRelatedProductComponentRepositoryImpl
        implements NotRelatedProductComponentRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public NotProductRelatedComponents fetchNonRelatedProductComponent(
            long contentId, List<Long> componentIds) {
        return queryFactory
                .from(component)
                .leftJoin(imageComponent)
                .on(imageComponent.componentId.eq(component.id))
                .on(imageComponent.deleteYn.eq(Yn.N))
                .leftJoin(imageComponentItem)
                .on(imageComponentItem.imageComponentId.eq(imageComponent.id))
                .on(imageComponentItem.deleteYn.eq(Yn.N))
                .leftJoin(blankComponent)
                .on(blankComponent.componentId.eq(component.id))
                .on(blankComponent.deleteYn.eq(Yn.N))
                .leftJoin(textComponent)
                .on(textComponent.componentId.eq(component.id))
                .on(textComponent.deleteYn.eq(Yn.N))
                .leftJoin(titleComponent)
                .on(titleComponent.componentId.eq(component.id))
                .on(titleComponent.deleteYn.eq(Yn.N))
                .where(component.id.in(componentIds))
                .transform(
                        GroupBy.groupBy(component.contentId)
                                .as(
                                        new QNotProductRelatedComponents(
                                                component.contentId,
                                                GroupBy.set(
                                                        new QTitleComponentQueryDto(
                                                                component.contentId,
                                                                titleComponent.componentId,
                                                                titleComponent.id.as(
                                                                        "titleComponentId"),
                                                                titleComponent.titleDetails.title1,
                                                                titleComponent.titleDetails.title2,
                                                                titleComponent
                                                                        .titleDetails
                                                                        .subTitle1,
                                                                titleComponent
                                                                        .titleDetails
                                                                        .subTitle2)),
                                                GroupBy.set(
                                                        new QTextComponentQueryDto(
                                                                component.contentId,
                                                                textComponent.componentId,
                                                                textComponent.id.as(
                                                                        "textComponentId"),
                                                                textComponent.content.coalesce(
                                                                        ""))),
                                                GroupBy.set(
                                                        new QImageComponentQueryDto(
                                                                component.contentId,
                                                                imageComponent.componentId,
                                                                imageComponent.id.as(
                                                                        "imageComponentId"),
                                                                imageComponent.imageType,
                                                                imageComponentItem.id,
                                                                imageComponentItem.imageUrl,
                                                                imageComponentItem.linkUrl,
                                                                imageComponentItem.displayOrder.as(
                                                                        "imageDisplayOrder"))),
                                                GroupBy.set(
                                                        new QBlankComponentQueryDto(
                                                                component.contentId,
                                                                blankComponent.componentId,
                                                                blankComponent.id.as(
                                                                        "blankComponentId"),
                                                                blankComponent.height,
                                                                blankComponent.lineYn)))))
                .get(contentId);
    }
}
