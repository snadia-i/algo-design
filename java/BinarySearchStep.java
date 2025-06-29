import java.io.*;
import java.util.*;

public class BinarySearchStep {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        try {
            // Prompt user for input file name
            System.out.print("Enter the CSV file name: ");
            String filename = scanner.nextLine();
            // Check if the filename is empty
            if (filename.isEmpty()) {
                System.out.println("No file name provided. Exiting.");
                return;
            }

            // Prompt user for target value
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

            List<Integer> numbers = new ArrayList<>();
            List<String> names = new ArrayList<>();
            // Read the CSV file and parse the numbers and names
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line by comma and parse the first part as an integer
                    // and the second part as a string (name)
                    String[] parts = line.split(",");
                    numbers.add(Integer.parseInt(parts[0].trim()));
                    names.add(parts[1].trim());
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename);
                return;
            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return;
            }

            // Prepare output file to record each step of the binary search
            String outputFile = "binary_search_step_" + target + ".txt";
            PrintWriter out = new PrintWriter(outputFile);

            // Perform binary search and record each step
            int low = 0, high = numbers.size() - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                String step = mid + ": " + numbers.get(mid) + "/" + names.get(mid);
                // Output the current step to the file
                out.println(step);

                // Target found
                if (numbers.get(mid) == target) {
                    out.println("Target found at index: " + mid + " (" + names.get(mid) + ")");
                    out.close();
                    System.out.println("Target found at index: " + mid + " (" + names.get(mid) + ")");
                    System.out.println("Binary search steps recorded in: " + outputFile);
                    return;
                } else if (numbers.get(mid) < target) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            // Target not found, output -1
            out.println("-1");
            out.close();
            System.out.println("Target not found. Binary search steps recorded in: " + outputFile);
        } finally {
            scanner.close();
        }
    }
}
