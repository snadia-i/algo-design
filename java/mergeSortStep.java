import java.io.*;
import java.util.*;

public class mergeSortStep {
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
            // Compare by number first, then by text (full string) if numbers are equal
            if (L[i].number < R[j].number) {
                array[k++] = L[i++];
            } else if (L[i].number > R[j].number) {
                array[k++] = R[j++];
            } else {
                // Compare the full text if numbers are equal
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

    // Helper method to format the array as required
    private static String formatArray(Tuple[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i].number).append("/").append(array[i].text);
            if (i != array.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // Modified merge sort to log steps
    public static void doMergeSortWithSteps(Tuple[] array, int left, int right, BufferedWriter bw) throws IOException {
        if (left < right) {
            int mid = (left + right) / 2;
            doMergeSortWithSteps(array, left, mid, bw);
            doMergeSortWithSteps(array, mid + 1, right, bw);
            mergeWithSteps(array, left, mid, right, bw);
            // Log the array after each merge step
            bw.write(formatArray(array));
            bw.newLine();
        }
    }

    // Modified merge to log steps
    private static void mergeWithSteps(Tuple[] array, int left, int mid, int right, BufferedWriter bw) throws IOException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Tuple[] L = new Tuple[n1];
        Tuple[] R = new Tuple[n2];

        for (int i = 0; i < n1; i++) L[i] = array[left + i];
        for (int j = 0; j < n2; j++) R[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter CSV file path: ");
        String filePath = scanner.nextLine();
        System.out.print("Enter start row begin at 1: ");
        int startRow = scanner.nextInt();
        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();
        scanner.close();

        if (startRow < 1 || endRow < startRow) {
            System.out.println("Invalid row range.");
            return;
        }

        List<Tuple> tupleList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentRow = 0;
            while ((line = br.readLine()) != null) {
                currentRow++;
                if (currentRow < startRow) continue;
                if (currentRow > endRow) break;
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

        Tuple[] array = tupleList.toArray(new Tuple[0]);
        int n = array.length;
        String outputFileName = "merge_sort_step_" + startRow + "_" + endRow + ".txt";

        long startTime = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            // Write initial unsorted array
            bw.write(formatArray(array));
            bw.newLine();
            doMergeSortWithSteps(array, 0, n - 1, bw);
        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
            return;
        }
        long endTime = System.nanoTime();

        double runningTimeMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Sorted steps saved to %s\n", outputFileName);
        System.out.printf("Running time: %.3f ms\n", runningTimeMs);
    }
}
