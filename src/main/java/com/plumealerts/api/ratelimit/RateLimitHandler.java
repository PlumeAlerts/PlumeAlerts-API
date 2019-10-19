package com.plumealerts.api.ratelimit;

import com.plumealerts.api.ratelimit.future.FutureRequest;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Handles making requests that are rate limited.
 */
public class RateLimitHandler {
    protected static final Queue<FutureRequest> REQUEST_QUEUE = new ConcurrentLinkedQueue<>();

    /**
     * There is a limit of 800 requests a minute in the current clientId and secret.
     * This can be increased if requested but this is the current limit set by Twitch.
     */
    private static final int REQUEST_LIMIT = 700;

    public RateLimitHandler() {
        TimedSemaphore sem = new TimedSemaphore(1, TimeUnit.MINUTES, REQUEST_LIMIT);
        RequestThread thread = new RequestThread(sem);
        thread.start();
    }

    /**
     * Add a request to the queue
     * @param futureRequest The request you are adding to the queue
     */
    public void add(FutureRequest futureRequest) {
        REQUEST_QUEUE.add(futureRequest);
    }
}
