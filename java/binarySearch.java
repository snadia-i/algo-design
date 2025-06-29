import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BinarySearch {
    // Binary search function
    public static int binarySearch(List<Integer> data, int target) {
        int low = 0, high = data.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (data.get(mid) == target) return mid;
            else if (data.get(mid) < target) low = mid + 1;
            else high = mid - 1;
        }
        return -1;
    }

    // Function to check if the list is sorted
    public static boolean isSorted(List<Integer> data) {
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i) < data.get(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the CSV file name: ");
            // Read the filename from user input
            String filename = scanner.nextLine();
            
            if (filename.isEmpty()) {
                System.out.println("No file name provided. Exiting.");
                scanner.close();
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(filename));
            List<Integer> numbers = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");
                numbers.add(Integer.parseInt(parts[0].trim()));
            }

            if (numbers.isEmpty()) {
                System.out.println("No numbers found in the file. Exiting.");
                scanner.close();
                return;
            }

            if (!isSorted(numbers)) {
                System.out.println("Dataset is not sorted. Binary search cannot be performed. Exiting.");
                scanner.close();
                return;
            }

            //System.out.println("\n====== Binary Search Timing Results ======\n");
            //System.out.println("Dataset: " + filename);

            int n = numbers.size();

            int best = numbers.get((n - 1) / 2);
            int average = numbers.get((n - 1) / 3);
            int worst = Integer.MAX_VALUE;

            String outputFile = "binary_search_" + n + ".txt";
            PrintWriter out = new PrintWriter(outputFile);

            // out.println("\n====== Binary Search Timing Results ======\n");
            // out.println("Dataset: " + filename);

            // Best case
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                binarySearch(numbers, best);
            }
            long end = System.nanoTime();
            double bestTime = (end - start) / 1000000.0; // Convert to milliseconds
            //System.out.println("Best case: " + (end - start) / 1000000.0 + " ms");
            out.println("Best case: " + String.format("%.3f", bestTime)+ " ms");

            // Average case
            start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                binarySearch(numbers, average);
            }
            end = System.nanoTime();
            double averageTime = (end - start) / 1000000.0; // Convert to milliseconds
            //System.out.println("Average case: " + (end - start) / 1000000.0 + " ms");
            out.println("Average case: " + String.format("%.3f", averageTime) + " ms");

            // Worst case
            start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                binarySearch(numbers, worst);
            }
            end = System.nanoTime();
            double worstTime = (end - start) / 1000000.0; // Convert to milliseconds
            //System.out.println("Worst case: " + (end - start) / 1000000.0 + " ms");
            out.println("Worst case: " + String.format("%.3f", worstTime) + " ms");

            System.out.println("Timing results written to: " + outputFile);

            out.close();
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}