import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class DatasetGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset size: ");
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
                // Generate a unique positive integer > 0 and up to 1.2 billion (just incase)
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

    // Generate a random lowercase word of length 5 to 10
    public static String getRandomWord(Random rand) {
        int length = 5 + rand.nextInt(6); // 5 to 10 characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }
}
