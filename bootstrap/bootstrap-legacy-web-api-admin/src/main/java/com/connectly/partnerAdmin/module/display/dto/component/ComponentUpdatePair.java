package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ComponentUpdatePair <T extends SubComponent>{
    private ComponentType componentType;
    private T newComponent;
    private T existingComponent;

}
