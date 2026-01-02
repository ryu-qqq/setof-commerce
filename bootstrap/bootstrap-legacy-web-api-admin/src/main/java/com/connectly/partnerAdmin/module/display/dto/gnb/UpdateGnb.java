package com.connectly.partnerAdmin.module.display.dto.gnb;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateGnb {

    List<CreateGnb> toUpdateGnbs = new ArrayList<>();
    List<Long> deleteGnbIds = new ArrayList<>();

}
