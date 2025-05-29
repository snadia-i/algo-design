import java.io.*;
import java.util.*;

public class BinarySearchStep {
    public static void main(String[] args) throws Exception {
        // Check if the required arguments are provided
        if (args.length < 2) {
            System.out.println("Usage: BinarySearchStep <file> <target>");
            return;
        }

        String filename = args[0]; // Input CSV file name
        int target = Integer.parseInt(args[1]); // Target value to search for

        // Read the CSV file and store numbers and names in separate lists
        BufferedReader br = new BufferedReader(new FileReader(filename));
        List<Integer> numbers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String line;

        // Parse each line: first column is number, second column is name
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            numbers.add(Integer.parseInt(parts[0]));
            names.add(parts[1]);
        }
        br.close();

        // Prepare output file to record each step of the binary search
        String outputFile = "binary_search_step_" + target + ".txt";
        PrintWriter out = new PrintWriter(outputFile);

        int low = 0, high = numbers.size() - 1;
        // Perform binary search, logging each step to the output file
        while (low <= high) {
            int mid = (low + high) / 2; // Calculate the middle index
            out.println(mid + ": " + numbers.get(mid) + "/" + names.get(mid)); // Log current step

            if (numbers.get(mid) == target) {
                // Target found, close output and exit
                out.close();
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
    }
}
