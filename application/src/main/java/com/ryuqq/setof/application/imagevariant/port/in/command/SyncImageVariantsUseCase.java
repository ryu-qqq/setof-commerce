package com.ryuqq.setof.application.imagevariant.port.in.command;

import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;

/** 이미지 Variant 동기화 UseCase. */
public interface SyncImageVariantsUseCase {

    /**
     * 이미지 Variant를 동기화합니다.
     *
     * <p>기존 variant와 비교하여 추가/삭제/유지를 판단합니다.
     *
     * @param command 동기화 커맨드
     */
    void execute(SyncImageVariantsCommand command);
}
