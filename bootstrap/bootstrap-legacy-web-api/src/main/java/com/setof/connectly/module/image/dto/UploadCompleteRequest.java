package com.setof.connectly.module.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadCompleteRequest {

    @NotBlank
    private String sessionId;

    @NotBlank
    private String etag;
}
