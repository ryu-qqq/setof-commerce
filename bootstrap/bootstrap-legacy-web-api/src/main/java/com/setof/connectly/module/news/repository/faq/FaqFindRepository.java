package com.setof.connectly.module.news.repository.faq;

import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import java.util.List;

public interface FaqFindRepository {

    List<FaqDto> fetchFaq(FaqFilter filter);
}
