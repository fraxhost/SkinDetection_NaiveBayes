import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrainingMachine
{
    double [][][] probability = new double[256][256][256];
    double thresholdOfMask = 240;

    public void train(List<Integer> trainset, int totalData)
    {
        try
        {
            BufferedImage maskImage = null;
            BufferedImage mainImage = null;
            int width = 0;
            int height = 0;

            double[][][] skinArray = new double[256][256][256];
            double[][][] nonSkinArray = new double[256][256][256];

            for (int ii = 0; ii < 256; ii++) {
                for (int jj = 0; jj < 256; jj++) {
                    for (int kk = 0; kk < 256; kk++) {
                        skinArray[ii][jj][kk] = 0;
                        nonSkinArray[ii][jj][kk] = 0;
                    }
                }
            }

            for (int k = 0; k < 554; k++) {
                if(!trainset.contains(k)) continue;

                String filename = Integer.toString(k);

                if (k < 10) filename = "000" + filename;
                else if (k < 100) filename = "00" + filename;
                else filename = "0" + filename;

                String MaskFilename = Main.mask + filename + ".bmp";
                String MainFilename = Main.ibtd + filename + ".jpg";

                File input1 = new File(MaskFilename);
                maskImage = ImageIO.read(input1);
                width = maskImage.getWidth();
                height = maskImage.getHeight();

                File input2 = new File(MainFilename);
                mainImage = ImageIO.read(input2);

                //Number of times a pixel(rgb) is identified as a skin pixel
                //Kon pixel kotobar skin er pixel er shathe milse
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {

                        Color maskC = new Color(maskImage.getRGB(j, i));
                        Color mainC = new Color(mainImage.getRGB(j, i));

                        if (maskC.getRed() > thresholdOfMask && maskC.getGreen() > thresholdOfMask && maskC.getBlue() > thresholdOfMask) {
                            nonSkinArray[mainC.getRed()][mainC.getGreen()][mainC.getBlue()]++;
                        } else {
                            skinArray[mainC.getRed()][mainC.getGreen()][mainC.getBlue()]++;
                        }

                    }
                }
            }

            double nonSkinArraySum = 0;
            double skinArraySum = 0;

            //Calculating sum of skin and non-skin arrays
            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        nonSkinArraySum = nonSkinArraySum + nonSkinArray[i][j][k];
                        skinArraySum = skinArraySum + skinArray[i][j][k];
                    }
                }
            }


            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        nonSkinArray[i][j][k] = nonSkinArray[i][j][k] / nonSkinArraySum;
                        skinArray[i][j][k] = skinArray[i][j][k] / skinArraySum;
                    }
                }
            }

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        if (skinArray[i][j][k] == 0 && nonSkinArray[i][j][k] == 0) probability[i][j][k] = 0;
                        else if (nonSkinArray[i][j][k] == 0) probability[i][j][k] = 10;
                        else probability[i][j][k] = skinArray[i][j][k] / nonSkinArray[i][j][k];
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void train()
    {
        try
        {
            BufferedImage maskImage = null;
            BufferedImage mainImage = null;
            int width = 0;
            int height = 0;

            double[][][] skinArray = new double[256][256][256];
            double[][][] nonSkinArray = new double[256][256][256];

            for (int ii = 0; ii < 256; ii++) {
                for (int jj = 0; jj < 256; jj++) {
                    for (int kk = 0; kk < 256; kk++) {
                        skinArray[ii][jj][kk] = 0;
                        nonSkinArray[ii][jj][kk] = 0;
                    }
                }
            }

            for (int k = 0; k < 554; k++) {
                String filename = Integer.toString(k);

                if (k < 10) filename = "000" + filename;
                else if (k < 100) filename = "00" + filename;
                else filename = "0" + filename;

                String MaskFilename = Main.mask + filename + ".bmp";
                String MainFilename = Main.ibtd + filename + ".jpg";

                File input1 = new File(MaskFilename);
                maskImage = ImageIO.read(input1);
                width = maskImage.getWidth();
                height = maskImage.getHeight();

                File input2 = new File(MainFilename);
                mainImage = ImageIO.read(input2);

                //Number of times a pixel(rgb) is identified as a skin pixel
                //Kon pixel kotobar skin er pixel er shathe milse
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {

                        Color maskC = new Color(maskImage.getRGB(j, i));
                        Color mainC = new Color(mainImage.getRGB(j, i));

                        if (maskC.getRed() > thresholdOfMask && maskC.getGreen() > thresholdOfMask && maskC.getBlue() > thresholdOfMask) {
                            nonSkinArray[mainC.getRed()][mainC.getGreen()][mainC.getBlue()]++;
                        } else {
                            skinArray[mainC.getRed()][mainC.getGreen()][mainC.getBlue()]++;
                        }

                    }
                }
            }

            double nonSkinArraySum = 0;
            double skinArraySum = 0;

            //Calculating sum of skin and non-skin arrays
            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        nonSkinArraySum = nonSkinArraySum + nonSkinArray[i][j][k];
                        skinArraySum = skinArraySum + skinArray[i][j][k];
                    }
                }
            }


            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        nonSkinArray[i][j][k] = nonSkinArray[i][j][k] / nonSkinArraySum;
                        skinArray[i][j][k] = skinArray[i][j][k] / skinArraySum;
                    }
                }
            }

            for (int i = 0; i < 256; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        if (skinArray[i][j][k] == 0 && nonSkinArray[i][j][k] == 0) probability[i][j][k] = 0;
                        else if (nonSkinArray[i][j][k] == 0) probability[i][j][k] = 10;
                        else probability[i][j][k] = skinArray[i][j][k] / nonSkinArray[i][j][k];
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void store()
    {
        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(Main.trainingDataStorageFile);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(probability);

            out.close();
            file.close();

            System.out.println("Training Data has been stored!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}