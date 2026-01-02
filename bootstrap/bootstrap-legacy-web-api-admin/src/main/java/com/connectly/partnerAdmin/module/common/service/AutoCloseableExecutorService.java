package com.connectly.partnerAdmin.module.common.service;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class AutoCloseableExecutorService implements AutoCloseable {

    private static final String EXECUTOR_ERROR_MSG = "ExecutorService did not terminate";
    private final ExecutorService executorService;

    public AutoCloseableExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void submit(Runnable task) {
        executorService.submit(task);
    }

    @Override
    public void close() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS))
                    log.error(EXECUTOR_ERROR_MSG);
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}