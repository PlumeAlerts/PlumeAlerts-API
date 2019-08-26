package com.plumealerts.api.ratelimit;

import com.plumealerts.api.ratelimit.future.FutureRequest;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class RateLimitHandler {
    public static final Queue<FutureRequest> REQUEST_QUEUE = new ConcurrentLinkedQueue<>();

    public RateLimitHandler() {
        TimedSemaphore sem = new TimedSemaphore(1, TimeUnit.MINUTES, 700/*Buffer*/);
        RequestThread thread = new RequestThread(sem);
        thread.start();
    }

    public void add(FutureRequest futureRequest) {
        REQUEST_QUEUE.add(futureRequest);
    }
}
