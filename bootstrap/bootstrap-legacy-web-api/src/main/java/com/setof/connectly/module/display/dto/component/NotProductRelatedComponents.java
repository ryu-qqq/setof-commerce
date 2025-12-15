package com.setof.connectly.module.display.dto.component;

import com.querydsl.core.annotations.QueryProjection;
import com.setof.connectly.module.display.dto.query.ComponentQuery;
import com.setof.connectly.module.display.dto.query.blank.BlankComponentQueryDto;
import com.setof.connectly.module.display.dto.query.image.ImageComponentQueryDto;
import com.setof.connectly.module.display.dto.query.text.TextComponentQueryDto;
import com.setof.connectly.module.display.dto.query.title.TitleComponentQueryDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotProductRelatedComponents {
    private long contentId;
    @Builder.Default private Set<TitleComponentQueryDto> titleComponents = new HashSet<>();
    @Builder.Default private Set<TextComponentQueryDto> textComponents = new HashSet<>();
    @Builder.Default private Set<ImageComponentQueryDto> imageComponents = new HashSet<>();
    @Builder.Default private Set<BlankComponentQueryDto> blankComponents = new HashSet<>();

    @QueryProjection
    public NotProductRelatedComponents(
            long contentId,
            Set<TitleComponentQueryDto> titleComponents,
            Set<TextComponentQueryDto> textComponents,
            Set<ImageComponentQueryDto> imageComponents,
            Set<BlankComponentQueryDto> blankComponents) {
        this.contentId = contentId;
        this.titleComponents = titleComponents;
        this.textComponents = textComponents;
        this.imageComponents = imageComponents;
        this.blankComponents = blankComponents;
    }

    public List<ComponentQuery> getAllComponents() {
        List<ComponentQuery> allComponents = new ArrayList<>();
        allComponents.addAll(titleComponents);
        allComponents.addAll(textComponents);
        allComponents.addAll(imageComponents);
        allComponents.addAll(blankComponents);
        return allComponents.stream()
                .filter(component -> component.getComponentId() != 0)
                .collect(Collectors.toList());
    }
}
