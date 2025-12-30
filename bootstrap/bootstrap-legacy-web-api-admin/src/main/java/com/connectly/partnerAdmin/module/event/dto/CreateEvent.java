package com.connectly.partnerAdmin.module.event.dto;

import com.connectly.partnerAdmin.module.event.entity.embedded.EventDetail;
import com.connectly.partnerAdmin.module.event.enums.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateEvent {

    @NotNull(message = "eventType은 필수입니다.")
    private EventType eventType;
    @Valid
    private EventDetail eventDetail;

    @Size(min = 1, message = "하위 이벤트 사이즈는 최소 1보다 커야합니다.")
    private List<SubEvent> subEvents;

}
