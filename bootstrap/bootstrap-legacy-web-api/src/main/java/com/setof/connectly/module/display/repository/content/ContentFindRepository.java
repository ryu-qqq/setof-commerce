package com.setof.connectly.module.display.repository.content;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.dto.content.ContentGroupResponse;
import com.setof.connectly.module.display.dto.query.ContentQueryDto;
import java.util.List;
import java.util.Optional;

public interface ContentFindRepository {
    Optional<ContentQueryDto> fetchContentQueryInfo(long contentId, Yn bypass);

    Optional<ContentGroupResponse> fetchContent(long contentId);

    List<Long> fetchOnDisplayContents();
}
