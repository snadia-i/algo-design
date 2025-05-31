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
        try {
            Scanner scanner = new Scanner(System.in);
            // Prompt user for input file name
            System.out.print("Enter the CSV file name: ");
            String filename = scanner.nextLine();
            if (filename.isEmpty()) {
                System.out.println("No file name provided. Exiting.");
                scanner.close();
                return;
            }

            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filename));
            List<Integer> numbers = new ArrayList<>();

            // Skip the header line if present
            if (!lines.isEmpty() && lines.get(0).startsWith("id")) {
                lines.remove(0); // Remove header line
            }

            // Parse each line and extract the integer value (assumed to be the first column)
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    try {
                        numbers.add(Integer.parseInt(parts[0].trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid line: " + line);
                    }
                }
            }

            if (numbers.isEmpty()) {
                System.out.println("No valid numbers found in the file. Exiting.");
                scanner.close();
                return;
            }

            // Sort the numbers list to prepare for binary search
            Collections.sort(numbers);

            System.out.println("\n====== Binary Search Timing Results ======\n");
            System.out.println("Dataset: " + filename);

            int n = numbers.size(); // Total number of elements in the dataset

            // Select test cases for best, average, and worst scenarios
            int best = numbers.get(n / 2);         // Best case: target is in the middle
            int average = numbers.get(n / 3);      // Average case: target is somewhere in the list
            int worst = 2000000000;                // Worst case: target is not in the list

            // Prepare output file to write timing results
            File outputsFolder = new File("outputs");
            if (!outputsFolder.exists()) {
                outputsFolder.mkdir(); // Create outputs folder if it doesn't exist
            }
            String outputFile = "outputs/binary_search_" + n + ".txt";
            PrintWriter out = new PrintWriter(outputFile);

            // Measure and record time for best case
            long start = System.nanoTime();
            binarySearch(numbers, best);
            long end = System.nanoTime();
            System.out.println("Best case: " + (end - start) / 1000 + " µs");
            out.println("Best case: " + (end - start) / 1000 + " µs");

            // Measure and record time for average case
            start = System.nanoTime();
            binarySearch(numbers, average);
            end = System.nanoTime();
            System.out.println("Average case: " + (end - start) / 1000 + " µs");
            out.println("Average case: " + (end - start) / 1000 + " µs");

            // Measure and record time for worst case
            start = System.nanoTime();
            binarySearch(numbers, worst);
            end = System.nanoTime();
            System.out.println("Worst case: " + (end - start) / 1000 + " µs");
            out.println("Worst case: " + (end - start) / 1000 + " µs");

            System.out.println("\nTiming results written to binary_search_" + n + ".txt");

            out.close(); // Close the output file
            scanner.close(); // Close the scanner
        }
        catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}