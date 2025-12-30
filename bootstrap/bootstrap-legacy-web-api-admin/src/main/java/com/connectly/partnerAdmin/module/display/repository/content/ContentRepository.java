package com.connectly.partnerAdmin.module.display.repository.content;

import com.connectly.partnerAdmin.module.display.entity.content.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
