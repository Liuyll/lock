package com.example.lock;

import java.util.concurrent.atomic.AtomicInteger;

public class SelfSpin implements Lock{
    AtomicInteger ticket;
    AtomicInteger cur_ticket;
    int my_ticket;
    boolean get_flag = false;

    public SelfSpin(AtomicInteger ticket,AtomicInteger cur_ticker) {
        this.ticket = ticket;
        this.cur_ticket = cur_ticker;
    }

    public void getTicket() {
        if(!get_flag) {
            my_ticket = getAndAdd(ticket);
            get_flag = true;
        }

    }

    public int getAndAdd(AtomicInteger atom) {
        for(;;) {
            int temp = atom.get();
            int next = temp + 1;
            if(atom.compareAndSet(temp,next)) return temp;
        }
    }

    public boolean lock() {
        getTicket();
        return cur_ticket.get() == my_ticket;
    }

    public void lockBlock() {
        for(;;) {
            getTicket();
            if(cur_ticket.get() == my_ticket) break;
        }
    }
    public void unlock() throws Exception{
        if(!get_flag) {
            throw new Exception("unget lock");
        }

        try {
            get_flag = false;
            this.cur_ticket.addAndGet(1);
        } catch(Exception e) {
            throw e;
        }
    }
}
