package kas.concurrente.productorconsumidor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Almacen implements Runnable {
    public static final int TAM = 20;
    public static final int CONSUMIDORES = 1;
    public static final int PRODUCTORES = 1;
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
                System.out.println("ENTRA CONSUMIDOR");
                retira();
                System.out.println("SALE CONSUMIDOR");
            }else{
                System.out.println("ENTRA PRODUCTOR");
                annade();
                System.out.println("SALE PRODUCTOR");
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
        espera.release();
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

    public static void main(String[] args) throws InterruptedException {
        List<Thread> productores = new ArrayList<>();
        List<Thread> consumidores = new ArrayList<>();
        Almacen almacen = new Almacen();

        for(int i = 0; i < CONSUMIDORES; ++i){
            Thread t = new Thread(almacen,"consumidor");
            consumidores.add(t);
            t.start();
        }

        for(int i = 0; i < PRODUCTORES; ++i){
            Thread t = new Thread(almacen,"productor");
            productores.add(t);
            t.start();
        }

        for(Thread t : consumidores){
            t.join();
        }

        for(Thread t : productores){
            t.join();
        }
    }
}
