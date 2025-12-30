package com.connectly.partnerAdmin.module.display.mapper.title;

import com.connectly.partnerAdmin.module.display.dto.component.title.TitleComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.component.sub.TitleComponent;
import com.connectly.partnerAdmin.module.display.entity.embedded.TitleDetails;
import org.springframework.stereotype.Component;

@Component
public class TitleComponentMapperImpl implements TitleComponentMapper {

    public TitleComponent toEntity(com.connectly.partnerAdmin.module.display.entity.component.Component component, TitleComponentDetail titleComponentDetail){
        return TitleComponent.builder()
                .component(component)
                .titleDetails(toTitleDetails(titleComponentDetail))
                .build();
    }


    private TitleDetails toTitleDetails(TitleComponentDetail titleComponentDetail){
        return TitleDetails.builder()
                .title1(titleComponentDetail.getTitle1())
                .title2(titleComponentDetail.getTitle2())
                .subTitle1(titleComponentDetail.getSubTitle1())
                .subTitle2(titleComponentDetail.getSubTitle2())
                .build();
    }
}
