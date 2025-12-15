package com.setof.connectly.module.news.service.faq;

import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import java.util.List;

public interface FaqFindService {

    List<FaqDto> fetchFaqDto(FaqFilter filter);
}
