package com.ryuqq.setof.adapter.in.rest.admin.navigation;

import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateGnbV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.DisplayPeriodV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.GnbDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateGnbV1ApiRequest;
import java.time.LocalDateTime;
import java.util.List;

public final class GnbCommandV1ApiFixtures {
    private GnbCommandV1ApiFixtures() {}

    public static final LocalDateTime DEFAULT_START = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    public static final LocalDateTime DEFAULT_END = LocalDateTime.of(2025, 12, 31, 23, 59, 59);

    public static DisplayPeriodV1ApiRequest defaultDisplayPeriod() {
        return new DisplayPeriodV1ApiRequest(DEFAULT_START, DEFAULT_END);
    }

    public static GnbDetailsV1ApiRequest defaultGnbDetails() {
        return new GnbDetailsV1ApiRequest("홈", "/", 1, defaultDisplayPeriod(), "Y");
    }

    public static GnbDetailsV1ApiRequest gnbDetails(
            String title, String linkUrl, int displayOrder, String displayYn) {
        return new GnbDetailsV1ApiRequest(
                title, linkUrl, displayOrder, defaultDisplayPeriod(), displayYn);
    }

    public static CreateGnbV1ApiRequest createRequest() {
        return new CreateGnbV1ApiRequest(null, defaultGnbDetails());
    }

    public static CreateGnbV1ApiRequest createRequest(String title, String linkUrl) {
        return new CreateGnbV1ApiRequest(null, gnbDetails(title, linkUrl, 1, "Y"));
    }

    public static CreateGnbV1ApiRequest updateRequest(Long gnbId) {
        return new CreateGnbV1ApiRequest(gnbId, gnbDetails("남성", "/men", 2, "N"));
    }

    public static UpdateGnbV1ApiRequest bulkRequest() {
        return new UpdateGnbV1ApiRequest(
                List.of(createRequest(), updateRequest(5L)), List.of(3L, 4L));
    }

    public static UpdateGnbV1ApiRequest createOnlyRequest() {
        return new UpdateGnbV1ApiRequest(List.of(createRequest()), null);
    }

    public static UpdateGnbV1ApiRequest deleteOnlyRequest() {
        return new UpdateGnbV1ApiRequest(null, List.of(3L, 4L));
    }
}
