import java.util.Scanner;

public class Main {
    public static String ibtd =
            "F:\\Programming\\JAVA\\Machine Learning\\Naive Bayes (Skin Detection)\\ibtd\\";
    public static String mask =
            "F:\\Programming\\JAVA\\Machine Learning\\Naive Bayes (Skin Detection)\\ibtd\\Mask\\";
    public static String codeFile =
            "F:\\Programming\\JAVA\\Machine Learning\\Naive Bayes (Skin Detection)\\";
    public static String trainingDataStorageFile =
            "F:\\Programming\\JAVA\\Machine Learning\\Naive Bayes (Skin Detection)\\Training Data Storage\\TrainingData.txt";

    public static double thresholdOfSkin = 1.0;
    public static int fold = 10;

    static public void main(String args[]) throws Exception {

        System.out.print(
                "Skin Detection: \n1. Single\n2. Directory\n3. Cross Validation\n----------------\nEnter Choice: ");

        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        if (choice==1) {
            TrainingMachine tm = new TrainingMachine();
            SkinDetection sd = new SkinDetection();

            tm.train();
            tm.store();
            sd.reading();
            sd.predictingSingleImageAtOnce();
        }
        else if (choice==2) {
            TrainingMachine tm = new TrainingMachine();
            SkinDetection sd = new SkinDetection();

            tm.train();
            tm.store();
            sd.reading();
            sd.predictingAllImagesFromDirectoryAtOnce();
        }
        else new CrossValidation(Main.fold).run();
    }
}
