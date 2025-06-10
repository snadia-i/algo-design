import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class dataset_generator {

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

        try (FileWriter writer = new FileWriter(filename)) {
            while (generatedIntegers.size() < size) {
                int number = 1 + rand.nextInt(1_000_000_000); // 32-bit positive integer
                if (!generatedIntegers.contains(number)) {
                    generatedIntegers.add(number);
                    String randomWord = getRandomWord(rand);
                    writer.write(number + "," + randomWord + "\n");
                }
            }
            System.out.println("Dataset generated: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    public static String getRandomWord(Random rand) {
        int length = 5 + rand.nextInt(2); // 5 or 6 letters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }
}
