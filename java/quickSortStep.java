import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class quickSortStep {
    // Data class defined within the same file
    static class Data implements Comparable<Data> {
        int number;
        String letters;

        public Data(int number, String letters) {
            this.number = number;
            this.letters = letters;
        }

        @Override
        public int compareTo(Data other) {
            return Integer.compare(this.number, other.number);
        }

        @Override
        public String toString() {
            return number + "/" + letters;
        }
        }

        // List to store sorting steps
        static ArrayList<String> sortingSteps = new ArrayList<>();

        public static void quickSort(Data[] arr, int low, int high, int startRow, int endRow) {
        if (low < high) {
            int pi = partition(arr, low, high, startRow, endRow);
            // Record step after partition
            sortingSteps.add("pi=" + pi + " " + arraySliceToString(arr, startRow, endRow));
            quickSort(arr, low, pi - 1, startRow, endRow);
            quickSort(arr, pi + 1, high, startRow, endRow);
        }
        }

        private static int partition(Data[] arr, int low, int high, int startRow, int endRow) {
        Data pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
            i++;
            Data temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            }
        }
        Data temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
        }

        private static String arraySliceToString(Data[] arr, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append(arr[i].toString());
            if (i < end) sb.append(", ");
        }
        return "[" + sb.toString() + "]";
        }

        public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter the CSV file path: ");
        String filePath = scanner.nextLine();
        ArrayList<Data> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 2);
            if (parts.length == 2) {
            int number = Integer.parseInt(parts[0].trim());
            String letters = parts[1].trim();
            dataList.add(new Data(number, letters));
            }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
        Data[] arr = dataList.toArray(new Data[dataList.size()]);

        System.out.print("Enter start row: ");
        int startRowInput = scanner.nextInt();
        System.out.print("Enter end row: ");
        int endRowInput = scanner.nextInt();

        // Convert to 0-based index
        int startRow = startRowInput - 1;
        int endRow = endRowInput - 1;

        if (startRow < 0 || endRow >= arr.length || startRow > endRow) {
            System.out.println("Invalid start or end row.");
            return;
        }

        // Add the initial array slice before sorting steps
        sortingSteps.add(arraySliceToString(arr, startRow, endRow));

        long startTime = System.nanoTime();
        quickSort(arr, startRow, endRow, startRow, endRow);
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        String outputFileName = String.format("quick_sort_step_%d_%d.txt", startRow + 1, endRow + 1);
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(outputFileName))) {
            bw.write("Sorting steps for elements from row " + (startRow + 1) + " to " + (endRow + 1) + ":\n");
            for (String step : sortingSteps) {
            bw.write(step);
            bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        }
    }
