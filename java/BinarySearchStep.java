import java.io.*;
import java.util.*;

public class BinarySearchStep {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
        // Prompt user for input file name and target value
            System.out.print("Enter the CSV file name: ");
            String filename = scanner.nextLine();
            if (filename.isEmpty()) {
                System.out.println("No file name provided. Exiting.");
                return;
            }

            System.out.print("Enter the target value to search for: ");
            int target;
            try {
                target = Integer.parseInt(scanner.nextLine());
                if (target < 0) {
                    System.out.println("Invalid target value. Exiting.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for target value. Please enter a valid integer.");
                return;
            }

            // Read the CSV file and store numbers and names in separate lists
            List<Integer> numbers = new ArrayList<>();
            List<String> names = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;

                // Skip the header line if present
                br.readLine();

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            numbers.add(Integer.parseInt(parts[0].trim()));
                            names.add(parts[1].trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid line: " + line);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
                scanner.close();
                return;
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                scanner.close();
                return;
            }

            // Check if the lists are sorted
            // boolean isSorted = true;
            // for (int i = 1; i < numbers.size(); i++) {
            //     if (numbers.get(i) < numbers.get(i - 1)) {
            //         isSorted = false;
            //         break;
            //     }
            // }
            // if (!isSorted) {
            //     System.out.println("The numbers in the file are not sorted. Please provide a sorted file.");
            //     scanner.close();
            //     return;
            // }

            // Prepare output file to record each step of the binary search
            File outputsFolder = new File("outputs");
            if (!outputsFolder.exists()) {
                outputsFolder.mkdir(); // Create outputs folder if it doesn't exist
            }
            String outputFile = "outputs/binary_search_step_" + target + ".txt";
            PrintWriter out = new PrintWriter(outputFile);

            int low = 0, high = numbers.size() - 1;
            // Perform binary search, logging each step to the output file
            while (low <= high) {
                int mid = (low + high) / 2; // Calculate the middle index
                String step = mid + ": " + numbers.get(mid) + "/" + names.get(mid); // Log current step
                out.println(step); // Write step to output file
                System.out.println(step); // Print step to console for immediate feedback

                if (numbers.get(mid) == target) {
                    // Target found, log the index and name
                    out.println(mid + ": " + names.get(mid)); // Log found index and name
                    out.println("Target found: " + target);
                    out.println("Target name: " + names.get(mid));
                    out.println("Total steps: " + (high - low + 1)); // Log total steps taken
                    out.flush(); // Ensure all data is written to the file
                    out.close();
                    System.out.println("Target found at index: " + mid + " (" + names.get(mid) + ")");
                    System.out.println("Binary search steps recorded in: " + outputFile);
                    scanner.close();
                    return;
                } else if (numbers.get(mid) < target) {
                    // Search right half
                    low = mid + 1;
                } else {
                    // Search left half
                    high = mid - 1;
                }
            }

            // Target not found, log -1
            out.println("-1");
            out.close();
            System.out.println("Target not found. Binary search steps recorded in: " + outputFile);
        } finally {
            scanner.close(); // Ensure scanner is closed to avoid resource leaks
        }
    }
}
