package datos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class QuickSortComparison extends RecursiveTask<Void> {

    int start, end;
    int[] arr;
    
    //QuickSortHilos/Concurrente
    public QuickSortComparison(int start, int end, int[] arr) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    private int partition(int start, int end, int[] arr) {
        int pivot = arr[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = temp;
        return i + 1;
    }

    @Override
    protected Void compute() {
    	
        if (start >= end) return null;
        int p = partition(start, end, arr);
        QuickSortComparison left = new QuickSortComparison(start, p - 1, arr);
        QuickSortComparison right = new QuickSortComparison(p + 1, end, arr);
        
        left.fork();
        right.compute();
        left.join();
        return null;
    }

    //QuickSortSecuencial

    public static void quickSortSequential(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionSequential(arr, low, high);
            quickSortSequential(arr, low, pi - 1);
            quickSortSequential(arr, pi + 1, high);
        }
    }

    private static int partitionSequential(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    
    public static void main(String[] args) {
		double tiempoInicial, tiempoFinal;

    	
        int n = 5000000; 
        int[] arr1 = generateArray(n);
        int[] arr2 = arr1.clone();
        
      
        // QuickSort secuencial
        tiempoInicial = System.nanoTime();
        quickSortSequential(arr1, 0, n - 1);
        tiempoFinal = System.nanoTime() - tiempoInicial; 
        System.out.println("Tiempo de ejecución QuickSort secuencial: " + tiempoFinal/1000 + " nanosegundos");

        
 
        // QuickSort con hilos
        ForkJoinPool pool = ForkJoinPool.commonPool();
        tiempoInicial = System.nanoTime();
        pool.invoke(new QuickSortComparison(0, n - 1, arr2));
        tiempoFinal = System.nanoTime() - tiempoInicial; 
        System.out.println("Tiempo de ejecución QuickSort con hilos: " + tiempoFinal/1000 + " nanosegundos");
        
        
        //System.out.println("\n Array1 Secuencial ");

        //mostrarArray(arr1);
        
       // System.out.println("\n Array2 Concurrente ");

        //mostrarArray(arr2);
    }

    private static int[] generateArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 100000);
        }
        return array;
    }
    
    public static void mostrarArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num +" - ");
        }
        
        System.out.println("\n-----------------------:\n");

    }
}
