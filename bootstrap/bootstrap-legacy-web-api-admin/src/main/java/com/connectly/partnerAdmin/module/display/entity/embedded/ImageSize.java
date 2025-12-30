package com.connectly.partnerAdmin.module.display.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class ImageSize {

    @Column(name = "WIDTH", nullable = true)
    private double width;

    @Column(name = "HEIGHT", nullable = true)
    private double height;



}
