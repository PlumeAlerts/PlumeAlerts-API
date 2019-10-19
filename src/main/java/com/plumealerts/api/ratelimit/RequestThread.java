package com.plumealerts.api.ratelimit;

import com.plumealerts.api.ratelimit.future.FutureRequest;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class RequestThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(RequestThread.class.getName());

    private final TimedSemaphore semaphore;

    public RequestThread(TimedSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (RateLimitHandler.REQUEST_QUEUE.isEmpty())
                    continue;

                this.semaphore.acquire();
                FutureRequest request = RateLimitHandler.REQUEST_QUEUE.poll();
                if (request != null) {
                    request.execute();
                }
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Request Thread Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}