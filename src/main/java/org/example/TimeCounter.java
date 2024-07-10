package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeCounter {
    public void timeAfterLaunch() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        final long startTime = System.currentTimeMillis();

        service.scheduleAtFixedRate(() -> {
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Часу пройшло с момента запуску: " + elapsedTime + " секунд");
        }, 0, 1, TimeUnit.SECONDS);

        service.scheduleAtFixedRate(() ->
                        System.out.println("Пройшло 5 секунд"),
                5, 5, TimeUnit.SECONDS);
    }
}
