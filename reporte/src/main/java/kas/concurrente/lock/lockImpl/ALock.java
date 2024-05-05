package kas.concurrente.lock.lockImpl;

import java.util.concurrent.atomic.AtomicInteger;

import kas.concurrente.lock.Lock;

public class ALock implements Lock {
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>(){
        protected Integer initialValue() {
            return 0;
        }
    };
    AtomicInteger tail;
    volatile boolean[] flag;
    int size;
    
    public ALock(int capacity){
        size = capacity;
        tail = new AtomicInteger(0);
        flag = new boolean[capacity];
        flag[0] = true;
    }

    @Override
    public void lock() {
        int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        while (!flag[slot]) Thread.yield();
    }

    @Override
    public void unlock() {
        int slot = mySlotIndex.get();
        flag[slot] = false;
        flag[(slot + 1)% size] = true;
    }
}
