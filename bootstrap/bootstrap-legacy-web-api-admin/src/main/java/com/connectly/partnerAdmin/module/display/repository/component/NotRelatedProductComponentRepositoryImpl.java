package com.connectly.partnerAdmin.module.display.repository.component;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.NotProductRelatedComponents;

import com.connectly.partnerAdmin.module.display.dto.component.QNotProductRelatedComponents;
import com.connectly.partnerAdmin.module.display.dto.query.blank.QBlankComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.image.QImageComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.text.QTextComponentQueryDto;
import com.connectly.partnerAdmin.module.display.dto.query.title.QTitleComponentQueryDto;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.connectly.partnerAdmin.module.display.entity.component.QComponent.component;
import static com.connectly.partnerAdmin.module.display.entity.component.item.QImageComponentItem.imageComponentItem;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.QBlankComponent.blankComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.QImageComponent.imageComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.QTextComponent.textComponent;
import static com.connectly.partnerAdmin.module.display.entity.component.sub.QTitleComponent.titleComponent;


@Repository
@RequiredArgsConstructor
public class NotRelatedProductComponentRepositoryImpl implements NotRelatedProductComponentRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public NotProductRelatedComponents fetchNonRelatedProductComponent(long contentId, List<Long> componentIds) {
        return queryFactory
                .from(component)
                .leftJoin(imageComponent)
                    .on(imageComponent.component.id.eq(component.id))
                    .on(imageComponent.deleteYn.eq(Yn.N))
                .leftJoin(imageComponentItem)
                    .on(imageComponentItem.imageComponentId.eq(imageComponent.id))
                    .on(imageComponentItem.deleteYn.eq(Yn.N))
                .leftJoin(blankComponent).on(blankComponent.component.id.eq(component.id))
                    .on(blankComponent.deleteYn.eq(Yn.N))
                .leftJoin(textComponent).on(textComponent.component.id.eq(component.id))
                    .on(textComponent.deleteYn.eq(Yn.N))
                .leftJoin(titleComponent).on(titleComponent.component.id.eq(component.id))
                    .on(titleComponent.deleteYn.eq(Yn.N))
                .where(componentIdIn(componentIds), deleteYn())
                .transform(
                        GroupBy.groupBy(component.contentId).as(
                                new QNotProductRelatedComponents(
                                        component.contentId,
                                        GroupBy.set(
                                                new QTitleComponentQueryDto(
                                                        component.contentId,
                                                        titleComponent.component.id,
                                                        titleComponent.id.as("titleComponentId"),
                                                        titleComponent.titleDetails.title1,
                                                        titleComponent.titleDetails.title2,
                                                        titleComponent.titleDetails.subTitle1,
                                                        titleComponent.titleDetails.subTitle2
                                                ).skipNulls()
                                        ),
                                        GroupBy.set(
                                                new QTextComponentQueryDto(
                                                        component.contentId,
                                                        textComponent.component.id,
                                                        textComponent.id.as("textComponentId"),
                                                        textComponent.content.coalesce("")
                                                ).skipNulls()
                                        ),
                                        GroupBy.set(
                                                new QImageComponentQueryDto(
                                                        component.contentId,
                                                        imageComponent.component.id,
                                                        imageComponent.id.as("imageComponentId"),
                                                        imageComponent.imageType,
                                                        imageComponentItem.id,
                                                        imageComponentItem.imageUrl,
                                                        imageComponentItem.linkUrl,
                                                        imageComponentItem.displayOrder.as("imageDisplayOrder"),
                                                        imageComponentItem.imageSize
                                                        ).skipNulls()
                                        ),
                                        GroupBy.set(
                                                new QBlankComponentQueryDto(
                                                        component.contentId,
                                                        blankComponent.component.id,
                                                        blankComponent.id.as("blankComponentId"),
                                                        blankComponent.height,
                                                        blankComponent.lineYn
                                                ).skipNulls()
                                        )
                                )
                        )
                ).get(contentId);
    }

    private BooleanExpression componentIdIn(List<Long> componentIds){
        return component.id.in(componentIds);
    }

    private BooleanExpression deleteYn(){
        return component.deleteYn.eq(Yn.N);
    }

}
