package com.connectly.partnerAdmin.module.display.entity.embedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class DisplayPeriod {


    @NotNull(message = "displayStartDate은 필수입니다.")
    @Column(name = "DISPLAY_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime displayStartDate;

    @NotNull(message = "displayEndDate은 필수입니다.")
    @Column(name = "DISPLAY_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime displayEndDate;

    public DisplayPeriod(LocalDateTime displayStartDate, LocalDateTime displayEndDate) {
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
    }
}
