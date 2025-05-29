import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BinarySearch {
    public static int binarySearch(List<Integer> data, int target) {
        int low = 0, high = data.size() - 1;
        // Continue searching while the search space is valid
        while (low <= high) {
            int mid = (low + high) / 2; // Find the middle index
            if (data.get(mid) == target) return mid; // Target found
            else if (data.get(mid) < target) low = mid + 1; // Search right half
            else high = mid - 1; // Search left half
        }
        return -1; // Target not found
    }

    public static void main(String[] args) throws Exception {
        // Name of the CSV file containing the dataset
        String filename = "dataset_sample_1000.csv";
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Integer> numbers = new ArrayList<>();

        // Parse each line and extract the integer value (assumed to be the first column)
        for (String line : lines) {
            String[] parts = line.split(",");
            numbers.add(Integer.parseInt(parts[0]));
        }

        int n = numbers.size(); // Total number of elements in the dataset

        // Select test cases for best, average, and worst scenarios
        int best = numbers.get(n / 2);         // Best case: target is in the middle
        int average = numbers.get(n / 3);      // Average case: target is somewhere in the list
        int worst = 2000000000;                // Worst case: target is not in the list

        // Prepare output file to write timing results
        PrintWriter out = new PrintWriter("binary_search_" + n + ".txt");

        // Measure and record time for best case
        long start = System.nanoTime();
        binarySearch(numbers, best);
        long end = System.nanoTime();
        out.println("Best case: " + (end - start) / 1000 + " µs");

        // Measure and record time for average case
        start = System.nanoTime();
        binarySearch(numbers, average);
        end = System.nanoTime();
        out.println("Average case: " + (end - start) / 1000 + " µs");

        // Measure and record time for worst case
        start = System.nanoTime();
        binarySearch(numbers, worst);
        end = System.nanoTime();
        out.println("Worst case: " + (end - start) / 1000 + " µs");

        out.close(); // Close the output file
    }
}
