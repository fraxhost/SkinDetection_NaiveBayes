import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import static javax.imageio.ImageIO.read;

public class SkinDetection {

    private double[][][] ProbabilityFromTrainedData = new double [256][256][256];

    // Deserialization
    public void reading()
    {
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(Main.trainingDataStorageFile);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            ProbabilityFromTrainedData = (double[][][]) in.readObject();

            in.close();
            file.close();

            System.out.println("Training Data has been analyzed!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void predictingMultipleImagesAtOnce(List<Integer> testset, int totalData) throws IOException {
        File folderImage = new File(Main.codeFile + "ibtd\\");
        File folderMask = new File(Main.codeFile + "ibtd\\Mask\\");

        for (int i=0; i<totalData; i++) {
            if (!testset.contains(i)) continue;

            double[][] prediction;
            double[][] actual;
            double[][][] isPixelPresentInCurrentImage = new double[256][256][256];

            String imageFile = folderImage.getAbsolutePath();
            String maskFile = folderMask.getAbsolutePath();

            System.out.println("Image: " + i);

            if (i < 10) {
                imageFile = imageFile + "\\000" + i + ".jpg";
                maskFile = maskFile + "\\000" + i + ".bmp";
            }
            else if (i < 100) {
                imageFile = imageFile + "\\00" + i + ".jpg";
                maskFile = maskFile +"\\00" + i + ".bmp";
            }
            else {
                imageFile = imageFile + "\\0" + i + ".jpg";
                maskFile = maskFile +"\\0" + i + ".bmp";
            }


            File imageToPredict = new File(imageFile);
            File imageMask = new File(maskFile);

            BufferedImage testImage = ImageIO.read(imageToPredict);
            BufferedImage maskImage = ImageIO.read(imageMask);
            int newWidth = testImage.getWidth();
            int newHeight = testImage.getHeight();

            actual = new double[newWidth][newHeight];
            prediction = new double[newWidth][newHeight];

            predictingImageData(maskImage, testImage, newWidth, newHeight, prediction, actual);

            ImageIO.write(testImage,"jpg", new File(Main.codeFile + "\\CV\\" + i + ".jpg"));
            ImageIO.write(maskImage,"jpg", new File(Main.codeFile + "\\CV\\" + i + ".bmp"));

            new ConfusionMatrix().build(actual, prediction, newWidth, newHeight);

            System.out.println("-------------------------------------");

        }
    }

    //Testing
    public void predictingSingleImageAtOnce() throws IOException {
        BufferedImage testImage;
        double newWidth=0;
        double newHeight=0;

        //File Chooser
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String actualFile = dialog.getDirectory() + dialog.getFile();
        //System.out.println(actualFile + " chosen.");

        //File Reading
        File input = new File(actualFile);
        testImage = ImageIO.read(input);
        newWidth = testImage.getWidth();
        newHeight = testImage.getHeight();

        creatingCorrespondingImage(testImage, newWidth, newHeight);

        String predictionFileDirectory =
                dialog.getDirectory().replace("Actual","Prediction") + dialog.getFile();
        ImageIO.write(testImage,"jpg", new File( predictionFileDirectory));

        System.out.println("Skin Predicted Successfully!");

        if(Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(predictionFileDirectory));
        }
    }

    public void predictingAllImagesFromDirectoryAtOnce() throws IOException {
        File folder = new File(
                Main.codeFile + "Tests\\Multiple Test\\Naked Women\\Actual");

        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String filename = file.getAbsolutePath();

                File input = new File(filename);

                BufferedImage testImage = ImageIO.read(input);
                double newWidth = testImage.getWidth();
                double newHeight = testImage.getHeight();

                creatingCorrespondingImage(testImage, newWidth, newHeight);

                //writing image to a file
                String predictionFileDirectory = filename.replace("Actual","Prediction");
                ImageIO.write(testImage,"jpg", new File(predictionFileDirectory));

            }
        }
    }

    private void creatingCorrespondingImage(BufferedImage testImage, double newWidth, double newHeight) {
        for(int i=0; i<newWidth; i++)
        {
            for(int j=0; j<newHeight; j++)
            {
                //prediction
                Color C = new Color(testImage.getRGB(i, j));
                int r = C.getRed();
                int g = C.getGreen();
                int b = C.getBlue();

                //less than threshold means the pixel is non-skin
                if(ProbabilityFromTrainedData[r][g][b] < Main.thresholdOfSkin) testImage.setRGB(i, j, Color.black.getRGB());
                else testImage.setRGB(i, j, Color.white.getRGB());
            }
        }
    }

    private void predictingImageData(BufferedImage maskImage,
                                     BufferedImage testImage,
                                     double newWidth,
                                     double newHeight,
                                     double[][] prediction,
                                     double[][] actual)
    {
        for(int i=0; i<newWidth; i++)
        {
            for(int j=0; j<newHeight; j++)
            {
                //actual
                Color B = new Color (maskImage.getRGB(i, j));
                int r1 = B.getRed();
                int g1 = B.getGreen();
                int b1 = B.getBlue();

                if (r1==255 && g1==255 && b1==255) actual[i][j] = -1;
                else actual[i][j]= 1;

                //prediction
                Color C = new Color (testImage.getRGB(i, j));
                int r = C.getRed();
                int g = C.getGreen();
                int b = C.getBlue();

                if (ProbabilityFromTrainedData[r][g][b] < Main.thresholdOfSkin) {
                    testImage.setRGB(i, j, Color.black.getRGB());
                    prediction[i][j] = -1;
                }
                else {
                    prediction[i][j] = 1;
                    testImage.setRGB(i, j, Color.white.getRGB());
                }

            }
        }
    }
}
