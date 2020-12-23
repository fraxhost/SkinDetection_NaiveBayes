import java.util.List;

public class ConfusionMatrix {
    private static double precision;
    private static double recall;
    private static double fscore;
    private static double accuracy;
    private static int numberOfTimesTheClassRan=0;

    public int[][] matrix = new int[2][2];

    public void build(double[][] actual, double[][] prediction, int width, int height) {

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {

                        if (actual[i][j] == 1 && prediction[i][j] == 1) matrix[0][0]++;
                        if (actual[i][j] == 1 && prediction[i][j] == -1) matrix[0][1]++;
                        if (actual[i][j] == -1 && prediction[i][j] == 1) matrix[1][0]++;
                        if (actual[i][j] == -1 && prediction[i][j] == -1) matrix[1][1]++;

            }
        }

//        for (int i=0; i<2; i++) {
//            for (int j=0; j<2; j++) {
//                System.out.print(matrix[i][j] + " ");
//            }
//            System.out.println();
//        }

        double total = matrix[0][0] + matrix[1][0] + matrix[0][1] + matrix[1][1];

        double accuracyOfThisImage = (matrix[0][0] + matrix[1][1]) / total / 0.01;

        double TPskin = matrix[0][0], FPskin = matrix[1][0], FNskin = matrix[0][1], TNskin = matrix[1][1];
        double Pskin = TPskin * 1.0 / (TPskin + FPskin);
        double Rskin = TPskin * 1.0 / (TPskin + FNskin);
        double Fskin = 2.0 * Pskin * Rskin / (Pskin + Rskin);

        double TPnot = matrix[1][1], FPnot = matrix[0][1], FNnot = matrix[1][0], TNnot = matrix[0][0];
        double Pnot = TPnot * 1.0 / (TPnot + FPnot);
        double Rnot = TPskin * 1.0 / (TPnot + FNnot);
        double Fnot = 2.0 * Pnot * Rnot / (Pnot + Rnot);

        double precisionAverageOfThisImage = (Pskin + Pnot) / 2.0 / 0.01;
        double recallAverageOfThisImage = (Rskin + Rnot) / 2.0 / 0.01;
        double fscoreAverageOfThisImage = (Fskin + Fnot) / 2.0 / 0.01;

        System.out.println("Precision = " + precisionAverageOfThisImage);
        System.out.println("Recall = " + recallAverageOfThisImage);
        System.out.println("F-score = " + fscoreAverageOfThisImage);
        System.out.println("Accuracy = " + accuracyOfThisImage);


        precision += precisionAverageOfThisImage;
        recall += recallAverageOfThisImage;
        fscore += fscoreAverageOfThisImage;
        accuracy += accuracyOfThisImage;

        numberOfTimesTheClassRan++;
    }

    public void finalPerformance() {
        System.out.println("Number of times the class ran: " + numberOfTimesTheClassRan);
        System.out.println("Average Precision = " + precision/numberOfTimesTheClassRan);
        System.out.println("Average Recall = " + recall/numberOfTimesTheClassRan);
        System.out.println("Average F-score = " + fscore/numberOfTimesTheClassRan);
        System.out.println("Average Accuracy = " + accuracy/numberOfTimesTheClassRan);
    }
}
