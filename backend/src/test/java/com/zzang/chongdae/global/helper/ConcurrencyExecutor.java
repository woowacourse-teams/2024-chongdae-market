package com.zzang.chongdae.global.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ConcurrencyExecutor {

    private static ConcurrencyExecutor INSTANCE;

    public static ConcurrencyExecutor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConcurrencyExecutor();
        }
        return INSTANCE;
    }

    public void executeWithoutResult(Runnable... tasks) throws InterruptedException {
        int executeCount = tasks.length;
        ExecutorService executorService = Executors.newFixedThreadPool(executeCount);
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        for (Runnable task : tasks) {
            executorService.submit(() -> {
                try {
                    task.run();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
    }

    public void executeWithoutResult(int repeatCount, Runnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
        CountDownLatch countDownLatch = new CountDownLatch(repeatCount);

        for (int i = 0; i < repeatCount; i++) {
            executorService.submit(() -> {
                try {
                    task.run();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
    }

    public final <T> List<T> execute(Supplier<T>... tasks) throws InterruptedException {
        int executeCount = tasks.length;
        ExecutorService executorService = Executors.newFixedThreadPool(executeCount);
        CountDownLatch countDownLatch = new CountDownLatch(executeCount);

        List<Future<T>> result = new ArrayList<>();
        for (Supplier<T> task : tasks) {
            Future<T> future = executorService.submit(() -> {
                try {
                    return task.get();
                } finally {
                    countDownLatch.countDown();
                }
            });
            result.add(future);
        }

        countDownLatch.await();
        executorService.shutdown();

        return result.stream()
                .map(this::getWithoutException)
                .toList();
    }

    public <T> List<T> execute(int repeatCount, Supplier<T> task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
        CountDownLatch countDownLatch = new CountDownLatch(repeatCount);

        List<Future<T>> result = new ArrayList<>();
        for (int i = 0; i < repeatCount; i++) {
            Future<T> future = executorService.submit(() -> {
                try {
                    return task.get();
                } finally {
                    countDownLatch.countDown();
                }
            });
            result.add(future);
        }

        countDownLatch.await();
        executorService.shutdown();

        return result.stream()
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
