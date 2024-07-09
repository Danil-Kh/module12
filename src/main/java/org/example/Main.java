package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class Main {
    public static int n;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    public final Queue<String> queue = new LinkedList<>();
    private int currentNumber = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть N: ");
        n = scanner.nextInt();
        Main main = new Main();
        main.start();
        TimeCounter timeCounter = new TimeCounter();
      //15  timeCounter.timeAfterLaunch();
    }

    public void start() {
        Thread threadA = createThread("A", this::fizz);
        Thread threadB = createThread("B", this::buzz);
        Thread threadC = createThread("C", this::fizzbuzz);
        Thread threadD = createThread("D", this::number);

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
            threadD.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Thread createThread(String name, Runnable task) {
        return new Thread(task, name);
    }

    private void fizz() {
        processNumber(i -> i % 3 == 0 && i % 5 != 0, "Fizz");
    }

    private void buzz() {
        processNumber(i -> i % 5 == 0 && i % 3 != 0, "Buzz");
    }

    private void fizzbuzz() {
        processNumber(i -> i % 3 == 0 && i % 5 == 0, "FizzBuzz");
    }

    private void number() {
        while (currentNumber <= n) {
            lock.lock();
            try {
                while (currentNumber <= n &&
                        ((currentNumber % 3 == 0 || currentNumber % 5 == 0) && queue.isEmpty())) {
                    condition.await();
                }
                if (currentNumber <= n) {
                    String output = queue.poll();
                    if (output == null) {
                        output = String.valueOf(currentNumber);
                    }
                    System.out.print(output);
                    if (currentNumber < n) System.out.print(", ");
                    currentNumber++;
                    condition.signalAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
    private void processNumber(Predicate<Integer> predicate, String output) {
        while (currentNumber <= n) {
            lock.lock();
            try {
                if (currentNumber <= n && predicate.test(currentNumber)) {
                    queue.offer(output);
                    condition.signalAll();
                    condition.await();
                } else {
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
