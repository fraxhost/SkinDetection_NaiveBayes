import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrossValidation {

    private final int fold;

    List<Integer> constantDataset;
    List<Integer> dataset;
    List<Integer> trainset;
    List<Integer> testset;

    public CrossValidation(int fold) {
        this.fold = fold;
        this.dataset = new ArrayList<>();
        constantDataset = new ArrayList<>();
        trainset = new ArrayList<>();
        testset = new ArrayList<>();
    }

    public void run() throws IOException {
        generateNumber();
        validation();
        new ConfusionMatrix().finalPerformance();
    }

    private void validation() throws IOException {
        for (int i=0; i<Main.fold; i++) {
            //create train and test set
            divideDataset();

            //train model
            TrainingMachine tm = new TrainingMachine();
            tm.train(trainset, constantDataset.size());
            tm.store();

            //testModel
            SkinDetection sd = new SkinDetection();
            sd.reading();
            sd.predictingMultipleImagesAtOnce(testset, constantDataset.size());

            //analyzeModel

            trainset.clear();
            testset.clear();
            rotateDataset(i);

//            for (int data:dataset) {
//                System.out.println(data);
//            }
        }
    }

    private void divideDataset() {
        int totalData = dataset.size();
        int limit = totalData*(fold-1) / fold;

        for (int i=0; i<totalData; i++) {
            if (i < limit) trainset.add(dataset.get(i));
            else testset.add(dataset.get(i));
        }
    }

    private void rotateDataset(int turn) {
        List<Integer> temp = new ArrayList<Integer>();
        int oneSet = dataset.size() / fold;

        dataset.clear();
        dataset.addAll(constantDataset);

        for (int i=0; i<dataset.size(); i++) {
            if(i>=turn*oneSet && i<(turn+1)*oneSet) {
                temp.add(dataset.get(i));
                dataset.set(i, -1);
            }
        }

        while (dataset.contains(-1)) {
            dataset.remove(Integer.valueOf(-1));
        }

        dataset.addAll(temp);
    }

    private void generateNumber() {
        Scanner input = new Scanner(System.in);

        System.out.print("Total Number of Photos: ");
        int photo = input.nextInt();

        for (int i=0; i<photo; i++) dataset.add(i);

        constantDataset.addAll(dataset);
    }
}
