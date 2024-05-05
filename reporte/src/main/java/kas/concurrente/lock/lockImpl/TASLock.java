package kas.concurrente.lock.lockImpl;

import java.util.concurrent.atomic.AtomicBoolean;

import kas.concurrente.lock.Lock;

/**
 * Clase que modela TASLock
 * @author Kassandra Mirael
 * @version 1.0
 */
public class TASLock implements Lock {
    private AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        while(state.getAndSet(true));
    }

    @Override
    public void unlock() {
        state.set(false);
    }
    
}
