import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class quickSort {
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
    }

    public static void quickSort(Data[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Data[] arr, int low, int high) {
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

        long startTime = System.nanoTime();
        quickSort(arr, 0, arr.length - 1);
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Sorting runtime: %.3f ms%n", durationMs);

        String outputFileName = "quick_sort_" + arr.length + ".csv";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(outputFileName))) {
            for (Data d : arr) {
                bw.write(d.number + "," + d.letters);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
