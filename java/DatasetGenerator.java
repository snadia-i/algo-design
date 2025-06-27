import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class datasetGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset size: "); // get size of dataset
        int size = scanner.nextInt();
        scanner.close();

        String filename = "dataset_" + size + ".csv";
        generateDataset(size, filename);
    }

    public static void generateDataset(int size, String filename) {
        Random rand = new Random();
        HashSet<Integer> generatedIntegers = new HashSet<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            while (generatedIntegers.size() < size) {
                // generate a unique positive integer > 0 and up to 1.2 billion (just incase)
                int number = 1 + rand.nextInt(1_200_000_000);

                if (generatedIntegers.add(number)) {
                    String randomWord = getRandomWord(rand);
                    writer.write(number + "," + randomWord);
                    writer.newLine();
                }
            }

            System.out.println("Dataset generated successfully: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // generate a random lowercase word of length 4 to 6
    public static String getRandomWord(Random rand) {
        int length = 4 + rand.nextInt(3); // 4,5 to 6 letters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }
}