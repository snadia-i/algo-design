import java.io.*;
import java.util.*;

public class mergeSort {
    // Define a tuple class to hold an integer and a string
    public static class Tuple {
        int number;
        String text;

        public Tuple(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    // Merge sort function for Tuple array
    public static void doMergeSort(Tuple[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            doMergeSort(array, left, mid);
            doMergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    // Merge function for Tuple array
    private static void merge(Tuple[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Tuple[] L = new Tuple[n1];
        Tuple[] R = new Tuple[n2];

        for (int i = 0; i < n1; i++) L[i] = array[left + i];
        for (int j = 0; j < n2; j++) R[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            // Compare by number first, then by text if numbers are equal
            if (L[i].number < R[j].number) {
                array[k++] = L[i++];
            } else if (L[i].number > R[j].number) {
                array[k++] = R[j++];
            } else {
                if (L[i].text.compareTo(R[j].text) <= 0) {
                    array[k++] = L[i++];
                } else {
                    array[k++] = R[j++];
                }
            }
        }

        while (i < n1) array[k++] = L[i++];
        while (j < n2) array[k++] = R[j++];
    }

    public static void main(String[] args) {
        String filePath;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter CSV file path: ");
            filePath = scanner.nextLine();
        }

        List<Tuple> tupleList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String text = parts[1].trim();
                    tupleList.add(new Tuple(number, text));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in file.");
            return;
        }

        Tuple[] array = tupleList.toArray(Tuple[]::new);
        int n = array.length;
        String outputFileName = "merge_sort_" + n + ".csv";

        long startTime = System.nanoTime();
        doMergeSort(array, 0, n - 1);
        long endTime = System.nanoTime();

        // Write sorted data to output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            for (Tuple t : array) {
                bw.write(t.number + "," + t.text);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
            return;
        }

        double runningTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Sorted data saved to %s\n", outputFileName);
        System.out.printf("Running time: %.3f ms\n", runningTimeMs);
    }
}
