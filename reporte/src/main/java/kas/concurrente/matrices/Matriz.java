package kas.concurrente.matrices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Matriz implements Runnable {
    private int [][] matriz;
    private int [][] matrizA;
    private int [][] matrizB;

    public Matriz(int [][] matrizA, int [][] matrizB) {
        this.matrizA = matrizA;
        this.matrizB = matrizB;
        matriz = new int[matrizA.length][matrizA.length];
    }

    /**
     * Metodo que lee una matriz desde un fichero.
     * @param args el archivo
     * @return La matriz que fue leida desde el fichero.
     */
    public static int[][] leer(String args) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(args));
            String texto = br.readLine();

            int filas = 0;
            int j=0;
            for (int i = 0; i < texto.length(); i++) {
                if(texto.charAt(i) == ' '){
                    j++;
                }
            }
            int [][] m = new int [j+1][j+1];
            while (texto != null) {
                String [] valores = texto.split(" ");
                for (int i = 0; i < valores.length; i++) {
                    m[i][filas] = Integer.parseInt(""+valores[i]);
                }
                filas++;
                texto = br.readLine();
            }
            
            br.close();
            return m;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    /**
     * Metodo que realiza LA multiplicacion de matrices de manera secuencial.
     * @param A La matriz A.
     * @param B La matriz B.
     * @return una matriz con el resultado de la multiplicacion de la matriz A y la matriz B de manera secuencial.
     */
    public int[][] resuelveSecuencial(int[][] A, int[][] B){
        int[][] resultado = new int[A.length][B.length];
        for(int i = 0; i<A.length; ++i){ 
            for(int j = 0; j<B.length; ++j){ 
                int valor = 0;
                for(int k = 0; k<A[0].length; ++k){
                    valor += A[i][k]*B[k][j]; 
                }
                resultado[i][j] = valor;
            }
        }
        return resultado;
    }

    /**
     * Metodo que resuelve la matriz de manera concurrente
     * @param matrizA La matriz A
     * @param matrizB La matriz B
     * @param fila La fila donde trabajara
     */
    public void resuelveConcurrente(int[][] matrizA,int[][] matrizB, int fila){
        for(int j=0; j< matrizA.length; j++){
            int valor = 0;
            for(int k = 0; k< matrizB.length; k++){
                valor += matrizA[fila][k]*matrizB[k][j];
            }
            matriz[fila][j] = valor;
        }
    }

    /**
     * Metodo que imprime con estilo una matriz.
     * @param x la matriz que se desea imprimir.
     * @return
     */
    public static String imprime(int [][] x){
        String cad = "";
        for (int i=0;i<x.length ;i++ ) {
            for (int j= 0;j<x[0].length ;j++ ) {
                cad += "|"+x[j][i]+"";
            }
            cad += "|\n";
        }
                
        return cad;
    }

    @Override
    public void run() {
        try{
            int hilo = Integer.parseInt(Thread.currentThread().getName());
            resuelveConcurrente(this.matrizA,this.matrizB,hilo);
        } catch (Exception e){
            System.out.println("Error en los Hilos");
        }
        
    }

    public static void main(String[] args) throws InterruptedException{
        int hilos = Integer.valueOf(args[0]);
        String archivo = args[1];
        int[][] matrizA = leer(archivo);
        int[][] matrizB = leer(archivo);
        Matriz m = new Matriz(matrizA, matrizB);

        if(hilos == 1){
            Date start = new Date();
            int[][] resultado = m.resuelveSecuencial(matrizA, matrizB);
            Date end = new Date();
            System.out.println("Tiempo de ejecucion en milisegundos "+ (end.getTime() - start.getTime()));
        }else{
            Date start = new Date();
            
            List<Thread> hilosL = new ArrayList<Thread>();
            for(int i=0; i<matrizA.length; i++){
                Thread t = new Thread(m,""+i);
                hilosL.add(t);
                t.start();

                if(hilos == hilosL.size()){
                    for(Thread threads : hilosL){
                        threads.join();
                    }
                    hilosL.clear();
                }
            }
            for(Thread threads : hilosL){
                threads.join();
            }
            
            Date end = new Date();
            //System.out.println(imprime(matriz));
            System.out.println("Tiempo de ejecucion en milisegundos " + (end.getTime() - start.getTime()));
        }
    }
    
}
