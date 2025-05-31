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

            System.out.println("\n====== Binary Search Timing Results ======\n");
            System.out.println("Dataset: " + filename);

            int n = numbers.size();

            int best = numbers.get(n / 2);
            int average = numbers.get(n / 3);
            int worst = 2000000000;

            File outputsFolder = new File("outputs");
            if (!outputsFolder.exists()) {
                outputsFolder.mkdir();
            }
            String outputFile = "outputs/binary_search_" + n + ".txt";
            PrintWriter out = new PrintWriter(outputFile);

            // Best case
            long start = System.nanoTime();
            binarySearch(numbers, best);
            long end = System.nanoTime();
            System.out.println("Best case: " + (end - start) / 1000 + " µs");
            out.println("Best case: " + (end - start) / 1000 + " µs");

            // Average case
            start = System.nanoTime();
            binarySearch(numbers, average);
            end = System.nanoTime();
            System.out.println("Average case: " + (end - start) / 1000 + " µs");
            out.println("Average case: " + (end - start) / 1000 + " µs");

            // Worst case
            start = System.nanoTime();
            binarySearch(numbers, worst);
            end = System.nanoTime();
            System.out.println("Worst case: " + (end - start) / 1000 + " µs");
            out.println("Worst case: " + (end - start) / 1000 + " µs");

            System.out.println("\nTiming results written to : " + outputFile);

            out.close();
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
