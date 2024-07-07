package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static int n;
    private final Object obj = new Object();
    public final Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть число");
        n = scanner.nextInt();
        Main main = new Main();
        main.setN();
        //timeAfterLaunch();

    }
    public void setN(){
        Thread threadA = new Thread(new Fizz());
        Thread threadB = new Thread(new Buzz());
        Thread threadC = new Thread(new FizzBuzz());
        Thread threadD = new Thread(new Number());
        threadD.start();
        threadC.start();
        threadA.start();
        threadB.start();
    }
    public class Fizz implements Runnable {
        public void run() {
            for (int i = 1; i <= n; i++) {
                synchronized (obj) {
                    if (i % 3 == 0 && i % 5 != 0) {
                        queue.add("Fizz");
                    }
                    obj.notifyAll();
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public class Buzz implements Runnable{
        public void run() {
            for (int i = 1; i <= n; i++) {
                synchronized (obj) {
                    if (i % 5 == 0 && i % 3 != 0) {
                        queue.add("Buzz");
                    }
                    obj.notifyAll();
                    try {
                        obj.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public class FizzBuzz implements Runnable{
        public void run() {
            for (int i = 1; i <= n; i++) {
                synchronized (obj) {
                    if (i % 5 == 0 && i % 3 == 0) {
                        queue.add("FizzBuzz");
                    }
                    obj.notifyAll();
                    try {
                        obj.wait();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);

                    }
                }
            }
        }
    }
    public class Number implements Runnable{
        public void run() {
            for (int i = 1; i <= n; i++) {
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
                    }
                    obj.notifyAll();
                }
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