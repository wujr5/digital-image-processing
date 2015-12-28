package HW4Tool;

import java.awt.image.BufferedImage;
import java.util.Random;


public class ImageDenoising {
  public static BufferedImage addGaussianNoise(
      BufferedImage inputImage, double mean, double standardVariance) {
    
    System.out.println("Adding gaussian noise...\nthe mean is :\n" + mean
        + "\nthe standar variance is:\n" + standardVariance);
    
    BufferedImage outputImage = null;
    
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[] oneDimArray = JavaRGBImage.getImageGrayArray(inputImage);
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, oneDimArray);
    
    Random random = new Random();
    
    int srcMax = 0;
    int destMax = 0;
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (grayArray[i][j] > srcMax) srcMax = grayArray[i][j];
        
        double gv = getGaussianValue(mean, standardVariance, random);
        grayArray[i][j] += gv;
        if (grayArray[i][j] > destMax) destMax = grayArray[i][j];
      }
    }
    
    double rate = (double)srcMax / (double)destMax;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        double temp = (double)grayArray[i][j] * rate;
        grayArray[i][j] = (int)temp;
      }
    }
    
    oneDimArray = JavaRGBImage.twoDimToOneDim(width, height, grayArray);
    outputImage = JavaRGBImage.getBufferedImage(width, height, oneDimArray);
    
    return outputImage;
  }
  
  public static double getGaussianValue(
      double mean, double standardVariance, Random random) {
    
    double result = random.nextGaussian() * standardVariance + mean;
    
    return result;
  }
  
  public static BufferedImage addSaltAndPepperNoise(
      BufferedImage inputImage, double saltRate, double pepperRate) {
    
    if (saltRate < 0 || pepperRate < 0 || saltRate + pepperRate > 1)
      throw new Error("The probabilities may by illegal");
    
    System.out.println("Adding Salt and Peper Noise...\n" + 
    "the salt rate is:\n" + saltRate + 
    "\nthe pepper rate is:\n" + pepperRate);
    
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    int totalSize = height * width;
    int[] oneDimArray = JavaRGBImage.getImageGrayArray(inputImage);
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, oneDimArray);
    
    int saltSize = (int)(totalSize * saltRate);
    int pepperSize = (int)(totalSize * pepperRate);
    
    for (int i = 0; i < saltSize; i++) {
      int randomPlace = (int)(Math.random() * totalSize);
      int row = randomPlace / width;
      int col = randomPlace % width;
      
      grayArray[row][col] = 255;
    }
    
    for (int i = 0; i < pepperSize; i++) {
      int randomPlace = (int)(Math.random() * totalSize);
      int row = randomPlace / width;
      int col = randomPlace % width;
      
      grayArray[row][col] = 0;
    }
    
    oneDimArray = JavaRGBImage.twoDimToOneDim(width, height, grayArray);
    outputImage = JavaRGBImage.getBufferedImage(width, height, oneDimArray);
    
    return outputImage;
  }
  
}
