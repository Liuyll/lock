package com.example.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private int num = 1;
    public static void main(String[] args) {
//        new Main().run();
        for(int i = 0;i < 10000;i++) {
            System.out.println(i);
        }
    }

    public void run() {
        final AtomicInteger ticket = new AtomicInteger(1);
        final AtomicInteger cur_ticket = new AtomicInteger(1);
        ExecutorService threadPool = Executors.newCachedThreadPool();

        for(int i = 0;i < 5;i++) {
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    SelfSpin lock = new SelfSpin(ticket,cur_ticket);
                    try {
                        _run(lock);
                    } catch (Exception e){ }
                }
            });
        }

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch(Exception e) {}

        System.out.println("finish");
    }
    public void _run(SelfSpin lock) throws Exception{
        while (!Thread.currentThread().isInterrupted() && num < 10000) {
            if(!lock.lock()) continue;
            System.out.println(num);
            num++;
            lock.unlock();
        }
    }


}
