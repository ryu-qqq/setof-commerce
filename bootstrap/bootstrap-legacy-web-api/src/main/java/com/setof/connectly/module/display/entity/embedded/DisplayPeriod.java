package com.setof.connectly.module.display.entity.embedded;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.setof.connectly.module.exception.event.EventAlreadyEndException;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class DisplayPeriod {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime displayStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime displayEndDate;

    public DisplayPeriod(LocalDateTime displayStartDate, LocalDateTime displayEndDate) {
        this.displayStartDate = displayStartDate;
        this.displayEndDate = displayEndDate;
    }

    public void checkEventPeriod() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(displayStartDate) || now.isAfter(displayEndDate)) {
            throw new EventAlreadyEndException();
        }
    }
}
