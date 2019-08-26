package com.plumealerts.api.ratelimit;

import com.plumealerts.api.ratelimit.future.FutureRequest;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

//TODO On error, reconnect
public class RequestThread extends Thread {
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
            e.printStackTrace();
        }
    }
}