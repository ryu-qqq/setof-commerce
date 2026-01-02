package com.connectly.partnerAdmin.module.display.dto.content.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.SubComponent;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateContent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long contentId;

    @Valid
    private DisplayPeriod displayPeriod;
    @Length(max = 50, message = "컨텐트 타이틀은 최대 50자 이내 입니다.")
    private String title;
    @Length(max = 50, message = "컨텐트 메모는 최대 200자 이내 입니다.")
    private String memo;
    @Setter
    private String imageUrl;

    @NotNull(message = "displayYn는 필수입니다")
    private Yn displayYn;

    @Size(min = 1, message = "하위 컴포넌트 사이즈는 최소 1보다 커야합니다.")
    private List<SubComponent> components;


    public Map<ComponentType, List<SubComponent>> getComponentsByType() {
        if (components == null || components.isEmpty()) {
            return Collections.emptyMap();
        }
        return components.stream()
                .collect(Collectors.groupingBy(SubComponent::getComponentType));
    }

}
