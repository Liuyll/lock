package com.example.lock;

public interface Lock {
    boolean lock();
    void unlock() throws Exception;
}
