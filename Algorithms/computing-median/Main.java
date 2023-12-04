import java.util.Arrays;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Random rand  = new Random();
        int[] sizes = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000};
        long startTime, endTime;
        int median;

        for (int i = 0; i < 10; i++) {

            for (int size: sizes){
                int[] A = generateRandomDistinctArray(size, rand);
                System.out.println("\nSize: " + size);
                startTime = System.nanoTime();
                median = RandomizedSelectApproach.computeMedian(A);
                endTime = System.nanoTime();
                System.out.println("• Randomized Select: " + (endTime - startTime) + " ns" + " | Median: " + median);
                write("RandSelect", endTime - startTime, size);
                
                A = generateRandomDistinctArray(size, rand);
                startTime = System.nanoTime();
                median = MedianOfMediansApproach.computeMedian(A);
                endTime = System.nanoTime();
                System.out.println("• Median of Medians: " + (endTime - startTime) + " ns" + " | Median: " + median);
                write("MedianOfMedians", endTime - startTime, size);
                
                A = generateRandomDistinctArray(size, rand);
                startTime = System.nanoTime();
                median = NaiveApproach.computeMedian(A);
                endTime = System.nanoTime();
                System.out.println("• Naive Approach: " + (endTime - startTime) + " ns" + " | Median: " + median);
                write("Naive", endTime - startTime, size);
            }

        }
    }

    private static int write(String method, long time, int size) {
        try (FileWriter fw = new FileWriter("performance_data.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(method + "," + time + "," + size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int[] generateRandomDistinctArray(int size, Random rand) {
        int[] A = new int[size];
        for (int i=0; i<size; i++) {
            A[i] = i;
        }
        for (int i=0; i<size; i++) {
            int j = rand.nextInt(size);
            int temp = A[i];
            A[i] = A[j];
            A[j] = temp;
        }
        return A;
    }
}

class RandomizedSelectApproach {
    public static int computeMedian(int[] A) {
        int n = A.length;
        if (n % 2 == 1) {
            return RandomSelect(A, 0, n - 1, n / 2 + 1);
        } else {
            return RandomSelect(A, 0, n - 1, n / 2);
        }
    }


    public static int RandomSelect(int[] A, int p, int r, int i) {
        if (p == r) {
            return A[p];
        }
        int q = RandomPartition(A, p, r);
        int k = q - p + 1;
        if (i == k) {
            return A[q];
        } else if (i < k) {
            return RandomSelect(A, p, q-1, i);
        } else {
            return RandomSelect(A, q+1, r, i-k);
        }
    }

    public static int RandomPartition(int[] A, int p, int r) {
        int i = (int) (Math.random() * (r-p+1)) + p;
        swap(A, i, r);
        return Partition(A, p, r);
    }

    public static int Partition(int[] A, int p, int r) {
        int x = A[r];
        int i = p - 1;
        for (int j=p; j<r; j++) {
            if (A[j] <= x) {
                i++;
                swap(A, i, j);
            }
        }
        swap(A, i+1, r);
        return i+1;
    }

    private static void swap(int[] A, int i, int j){
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    
    }
}

class MedianOfMediansApproach {

    public static int computeMedian(int[] A) {
        int n = A.length;
        if (n % 2 == 1) {
            return select(A, 0, n - 1, n / 2 + 1);
        } else {
            return select(A, 0, n - 1, n / 2);
        }
    }

    public static int select(int[] A, int p, int r, int i) {
        while (true) {
            if (p == r) {
                return A[p];
            }
            int median = medianOfMedians(A, p, r);
            int q = partition(A, p, r, median);
            int k = q - p + 1;

            if (i == k){
                return A[q];
            } else if (i < k) {
                r = q - 1;
            } else {
                p = q + 1;
                i = i - k;
            }
        }
    }

    public static int partition(int[] A, int p, int r, int pivot) {
        int pivotIndex = -1;
        for (int j = p; j <= r; j++){
            if (A[j] == pivot) {
                pivotIndex = j;
                break;
            }
        }
        swap(A, pivotIndex, r);
        int i = p - 1;
        for (int j = p; j < r; j++){
            if (A[j] <= pivot) {
                i++;
                swap(A, i, j);
            }
        }
        swap(A, i+1, r);
        return i + 1;
    }

    private static int medianOfMedians(int[] A, int p, int r) {
        int n = r - p + 1;
        if (n < 5) {
            Arrays.sort(A, p, r+1);
            return A[p+n/2];
        }
        int[] medians = new int[(n+4)/5];
        for (int i=0; i<medians.length; i++){
            int left = p + i * 5;
            int right = Math.min(left + 4, r);
            Arrays.sort(A, left, right+1);
            medians[i] = A[left + (right-left)/2];
        }
        return select(medians, 0, medians.length-1, medians.length/2);
    }

    private static void swap(int[] A, int i, int j){
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
}

class NaiveApproach {
    public static int computeMedian(int[] A) {
        Arrays.sort(A);
        int n = A.length;
        if (n % 2 == 1) {
            return A[n / 2];
        } else {
            return A[n / 2 - 1];
        }
    }
}