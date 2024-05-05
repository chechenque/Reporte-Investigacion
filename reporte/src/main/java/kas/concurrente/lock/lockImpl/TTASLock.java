package kas.concurrente.lock.lockImpl;

import java.util.concurrent.atomic.AtomicBoolean;

import kas.concurrente.lock.Lock;

/**
 * Clase que Modela TTASLock
 * @author Kassandra Mirael
 * @version 1.0
 */
public class TTASLock implements Lock{
    private AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        while(true){
            while(state.get());
            if(!state.getAndSet(true)){
                return;
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
    
}
