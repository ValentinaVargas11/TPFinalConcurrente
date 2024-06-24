package datos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class QuickSortComparison extends RecursiveTask<Void> {

    int start, end;
    int[] arr;
    
    //QuickSortHilos/Concurrente
    // Constructor de la clase QuickSortComparison para inicializar los valores
    public QuickSortComparison(int start, int end, int[] arr) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }
    // Método para particionar el array en dos subarrays
    private int partition(int start, int end, int[] arr) {
        int pivot = arr[end];// Elegimos el último elemento como pivote
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
        return i + 1; // Retornamos el índice del pivote
    }

    @Override
    protected Void compute() {
    	// si el subarray tiene menos de 2 elementos, no hacemos nada
        if (start >= end) return null; 
        // Particionamos el array y obtenemos el índice del pivote
        int p = partition(start, end, arr);
        // Creamos nuevas tareas para los subarrays resultantes
        QuickSortComparison left = new QuickSortComparison(start, p - 1, arr);
        QuickSortComparison right = new QuickSortComparison(p + 1, end, arr);
        
        left.fork(); // Ejecutamos la tarea de la izquierda en paralelo
        right.compute(); // Ejecutamos la tarea de la derecha en el hilo actual
        left.join(); // Esperamos a que la tarea de la izquierda termine
        return null;
    }

    //QuickSortSecuencial
    public static void quickSortSequential(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionSequential(arr, low, high); // Particionamos el array y obtenemos el índice del pivote
            quickSortSequential(arr, low, pi - 1); // Ordenamos el subarray izquierdo
            quickSortSequential(arr, pi + 1, high);// Ordenamos el subarray derecho
        }
    }
    // Método para particionar el array en dos subarrays (versión secuencial)
    private static int partitionSequential(int[] arr, int low, int high) {
        int pivot = arr[high];// Elegimos el último elemento como pivote
        int i = low - 1; // Índice del elemento más pequeño
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                // Intercambiamos arr[i] y arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // Intercambiamos arr[i+1] y arr[high] (o pivote)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1; // Retornamos el índice del pivote
    }

    
    public static void main(String[] args) {
		double tiempoInicial, tiempoFinal;

    	
        int n = 5000000; 
        int[] arr1 = generateArray(n);
        int[] arr2 = arr1.clone();
        
      
        // Medimos el tiempo de ejecución de QuickSort secuencial
        tiempoInicial = System.nanoTime();
        quickSortSequential(arr1, 0, n - 1);
        tiempoFinal = System.nanoTime() - tiempoInicial; 
        System.out.println("Tiempo de ejecución QuickSort secuencial: " + tiempoFinal/1000 + " nanosegundos");

        
 
        // Medimos el tiempo de ejecución de QuickSort con hilos
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

    
    // Método para generar un array de tamaño 'size' con valores aleatorios
    private static int[] generateArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * 100000);
        }
        return array;
    }
    
    // Método para mostrar los elementos del array
    public static void mostrarArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num +" - ");
        }
        
        System.out.println("\n-----------------------:\n");

    }
}
