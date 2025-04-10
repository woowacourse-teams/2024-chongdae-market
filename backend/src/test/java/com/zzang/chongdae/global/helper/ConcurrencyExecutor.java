package com.zzang.chongdae.global.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.stereotype.Component;

@Component
public class ConcurrencyExecutor {

    public <T> void executeWithoutResult(Callable<T>... tasks) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(tasks.length);
        executorService.invokeAll(Arrays.asList(tasks));
        executorService.shutdown();
    }

    public <T> void executeWithoutResult(Callable<T> task, int repeatCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
        executorService.invokeAll(Collections.nCopies(repeatCount, task));
        executorService.shutdown();
    }

    public final <T> List<T> execute(Callable<T>... tasks) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(tasks.length);
        List<Future<T>> results = executorService.invokeAll(Arrays.asList(tasks));
        executorService.shutdown();
        return results.stream()
                .map(this::getWithoutException)
                .toList();
    }

    public <T> List<T> execute(Callable<T> task, int repeatCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
        List<Future<T>> results = executorService.invokeAll(Collections.nCopies(repeatCount, task));
        executorService.shutdown();
        return results.stream()
                .map(this::getWithoutException)
                .toList();
    }

    private <T> T getWithoutException(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("동시 작업 내부 예외 발생: ", e);
        }
    }
}
