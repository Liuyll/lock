package com.example.lock;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock{
    class LockNode {
        public boolean isLocked;
    }

    private final ThreadLocal<LockNode> myLock;
    private final ThreadLocal<LockNode> preLock;
    private final AtomicReference<LockNode> lockQueueTail;

    public CLHLock() {
        myLock = ThreadLocal.withInitial(LockNode::new);
        preLock = new ThreadLocal<LockNode>();

        lockQueueTail = null;
    }

    public boolean lock() {
        LockNode cur = myLock.get();
        cur.isLocked = true;
        LockNode last = lockQueueTail.getAndSet(cur);
        preLock.set(last);

        while(last.isLocked) {}
        return true;
    }

    public void unlock() {
        LockNode cur = myLock.get();
        cur.isLocked = false;
        myLock.set(preLock.get());
    }
}
