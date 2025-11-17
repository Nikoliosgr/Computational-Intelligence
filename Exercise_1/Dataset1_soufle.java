import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Dataset1_soufle {

    public static void main(String[] args) {
        int totalPoints = 8000;
        double[][] points = new double[totalPoints][2];
        String[] categories = new String[totalPoints];
        Random rand = new Random(); // Seed for reproducibility

        // Generate random points
        for (int i = 0; i < totalPoints; i++) {
            points[i][0] = rand.nextDouble() * 2 - 1; // x1 between -1 and 1
            points[i][1] = rand.nextDouble() * 2 - 1; // x2 between -1 and 1
            categories[i] = classify(points[i][0], points[i][1]);
        }

        // Split into training and testing sets
        int trainSize = 4000;
        int testSize = 4000;
        double[][] trainPoints = new double[trainSize][2];
        String[] trainCategories = new String[trainSize];
        double[][] testPoints = new double[testSize][2];
        String[] testCategories = new String[testSize];

        // Shuffle the points and categories together using Fisher-Yates algorithm
        for (int i = totalPoints - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Swap points
            double tempX = points[i][0];
            points[i][0] = points[j][0];
            points[j][0] = tempX;
            double tempY = points[i][1];
            points[i][1] = points[j][1];
            points[j][1] = tempY;
            // Swap categories
            String tempC = categories[i];
            categories[i] = categories[j];
            categories[j] = tempC;
        }

        // Assign to training and testing sets
        for (int i = 0; i < trainSize; i++) {
            trainPoints[i][0] = points[i][0];
            trainPoints[i][1] = points[i][1];
            trainCategories[i] = categories[i];
        }
        for (int i = 0; i < testSize; i++) {
            testPoints[i][0] = points[trainSize + i][0];
            testPoints[i][1] = points[trainSize + i][1];
            testCategories[i] = categories[trainSize + i];
        }

        saveDataset("train_data.csv", trainPoints, trainCategories);
        saveDataset("test_data.csv", testPoints, testCategories);

        System.out.println("Dataset generation complete.");
    }

    private static String classify(double x1, double x2) {
        double condition1 = Math.pow(x1 - 0.5, 2) + Math.pow(x2 - 0.5, 2);
        double condition2 = Math.pow(x1 - 0.5, 2) + Math.pow(x2 + 0.5, 2);
        double condition3 = Math.pow(x1 + 0.5, 2) + Math.pow(x2 + 0.5, 2);
        double condition4 = Math.pow(x1 + 0.5, 2) + Math.pow(x2 - 0.5, 2);

        if (condition1 < 0.2 && x2 > 0.5) return "C1";
        if (condition1 < 0.2 && x2 < 0.5) return "C2";
        if (condition3 < 0.2 && x2 > -0.5) return "C1";
        if (condition3 < 0.2 && x2 < -0.5) return "C2";
        if (condition2 < 0.2 && x2 > -0.5) return "C1";
        if (condition2 < 0.2 && x2 < -0.5) return "C2";
        if (condition4 < 0.2 && x2 > 0.5) return "C1";
        if (condition4 < 0.2 && x2 < 0.5) return "C2";
        if (x1 * x2 > 0) return "C3";
        return "C4";
    }

    private static void saveDataset(String filename, double[][] points, String[] categories) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < points.length; i++) {
                writer.write(points[i][0] + "," + points[i][1] + "," + categories[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
