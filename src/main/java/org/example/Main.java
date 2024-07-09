package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static int n;
    private final Object obj = new Object();
    public final Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        Main main = new Main();
        main.Start();
        //timeAfterLaunch();

    }
    public void Start(){
        Thread threadA = new Thread(() -> {
            for (int i = 1; i <= n; i++) {
            synchronized (obj) {
                if (fizz(i)) {
                    queue.add("Fizz");
                }
               // System.out.println("queue = " + queue);
                obj.notifyAll();
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 1; i <= n; i++) {
                synchronized (obj) {
                    if (buzz(i)) {
                        queue.add("Buzz");
                    }
                  //  System.out.println("queue = " + queue);
                    obj.notifyAll();
                    try {
                        obj.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Thread threadC = new Thread(() ->{
            for (int i = 1; i <= n; i++) {
                synchronized (obj) {
                    if (fizzBuzz(i)) {
                        queue.add("fizzbuzz");
                    }
                  //  System.out.println("queue = " + queue);
                    obj.notifyAll();
                    try {
                        obj.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Thread threadD = new Thread(() ->{
            for (int i = 1; i <= n; i++) {
                number(i);
            }
        });
        threadA.start();
       threadD.start();
        threadB.start();
        threadC.start();
    }
    public boolean fizz(int i){
        return i % 3 == 0 && i % 5 != 0;
    }
    public boolean buzz(int i){
        return i % 5 == 0 && i % 3 != 0;
    }
    public boolean fizzBuzz(int i){
        return i % 5 == 0 && i % 3 == 0;
    }
    public void number(int i){
        synchronized (obj) {
            if (i % 3 != 0 && i % 5 != 0) {
                System.out.println(i);
            } else {
                while (queue.isEmpty()) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
                System.out.println(queue.poll());
                obj.notifyAll();
            }

        }
    }
    public static void timeAfterLaunch(){

        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(() ->
                        System.out.println("Часу пройшло с момента запуску: " +  System.nanoTime() /  1_000_000_000),
                0,
                1,
                TimeUnit.SECONDS
        );
        service.scheduleAtFixedRate(() ->
                        System.out.println("Пройшло 5 секунд"),
                0,
                5,
                TimeUnit.SECONDS
        );
    }
}
