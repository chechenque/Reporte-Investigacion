package kas.concurrente.productorconsumidor;

import java.util.concurrent.Semaphore;

public class Almacen implements Runnable {
    public static final int TAM = 20;
    private Semaphore hilo;
    private Semaphore conteo;
    private Semaphore espera;
    private int productos;

    public Almacen() {
        this.hilo = new Semaphore(1);
        this.espera = new Semaphore(0);
        this.conteo = new Semaphore(TAM);
        this.productos = 0;
    }

    @Override
    public void run(){
        String nombre = Thread.currentThread().getName();
        try{
            if(nombre.equals("consumidor")){
                retira();
            }else{
                annade();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * Metodo que annade un elemeto
     * @throws InterruptedException
     */
    public void annade() throws InterruptedException {
        conteo.acquire();
        hilo.acquire();
        this.productos++;
        System.out.println("Se ha agregado un producto, quedan: " + this.productos);
        hilo.release();
        espera.acquire();
    }

    /**
     * Metodo que retira un elemento
     * @throws InterruptedException
     */
    public void retira() throws InterruptedException {
        espera.acquire();
        hilo.acquire();
        this.productos--;
        System.out.println("Se ha tomado un producto, quedan: " + this.productos);
        hilo.release();
        conteo.release();
    }
}
