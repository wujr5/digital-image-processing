package HW4Tool;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageFilter {
  public static BufferedImage arithmeticMeanFilters(
      BufferedImage inputImage, int size) throws InterruptedException {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, 
        JavaRGBImage.getImageGrayArray(inputImage));
    
    double[][] filter = getArithmeticMeanFilter(size);
    int[][] filteredArray = performAMF(grayArray, filter);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(width, height, filteredArray));
        
    return outputImage;
  }
  
  public static double[][] getArithmeticMeanFilter(int size) {
    double[][] filter = new double[size][size];
    
    double mean = 1d / (double)(size * size);
    
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        filter[i][j] = mean;
      }
    }
    
    return filter;
  }
  
  public static int[][] performAMF(int[][] grayArray, double [][] filter) 
      throws InterruptedException {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int result = getConvoluteValue_AMF(i, j, grayArray, filter);
        filteredArray[i][j] = result;
      }
    }
    
    return filteredArray;
  }
  
  public static int getConvoluteValue_AMF(
      int x, int y, int[][] grayArray, double[][] filter) 
          throws InterruptedException {
    
    int filterSize = filter.length;
    double result = 0;
    
    for (int i = 0; i < filterSize; i++) {
      for (int j = 0; j < filterSize; j++) {
        int row = x - (filterSize / 2) + i;
        int col = y - (filterSize / 2) + j;
        
        if (row < 0 || col < 0 || row >= grayArray.length || 
            col >= grayArray[0].length)
          result += 0;
        else 
          result += (double)(grayArray[row][col]) * filter[i][j];
      }
    }
    
    return (int)result;
  }
  
  public static BufferedImage harmonicMeanFilter(
      BufferedImage inputImage, int size) {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[] oneDimArray = JavaRGBImage.getImageGrayArray(inputImage);
    int[][] towDimArray = 
        JavaRGBImage.oneDimToTwoDim(width, height, oneDimArray);
    int[][] filteredImage = performHMF(towDimArray, size);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(
            width, height, filteredImage));
    
    return outputImage;
  }
  
  public static int[][] performHMF(int[][] grayArray, int size) {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredImage = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        filteredImage[i][j] = getConvoluteValue_HMF(i, j, grayArray, size);
      }
    }
    
    return filteredImage;
  }
  
  public static int getConvoluteValue_HMF(
      int x, int y, int[][] grayArray, int size) {
    double result = 0;
    int height = grayArray.length;
    int width = grayArray[0].length;
    
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int row = x - (size / 2) + i;
        int col = y - (size / 2) + j;
        
        if (row < 0 || col < 0 || row >= height || col >= width) {
//          result += 0;
        } else {
          if (grayArray[row][col] != 0) 
            result += 1d / (double)(grayArray[row][col]);
        }
      }
    }
    
    
    
    if (result != 0) result = (double)(size * size) / result;
    if (result > 255) result = 255;
    
    return (int)result;
  }

  public static BufferedImage contraHarmonicMeanFilter(
      BufferedImage inputImage, int size, double Q) {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[] oneDimArray = JavaRGBImage.getImageGrayArray(inputImage);
    int[][] towDimArray = 
        JavaRGBImage.oneDimToTwoDim(width, height, oneDimArray);
    int[][] filteredImage = performCHMF(towDimArray, size, Q);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(
            width, height, filteredImage));
    
    return outputImage;
  }
  
  public static int[][] performCHMF(int[][] grayArray, int size, double Q) {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredImage = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        filteredImage[i][j] = getConvoluteValue_CHMF(i, j, grayArray, size, Q);
      }
    }
    
    return filteredImage;
  }
  
  public static int getConvoluteValue_CHMF(
      int x, int y, int[][] grayArray, int size, double Q) {
    double numerator = 0;
    double denominator = 0;
    int height = grayArray.length;
    int width = grayArray[0].length;
    
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        int row = x - (size / 2) + i;
        int col = y - (size / 2) + j;
        
        if (row < 0 || col < 0 || row >= height || col >= width) {
          if (Q + 1 >= 0) numerator += Math.pow(0, Q + 1);
          if (Q >= 0) denominator += Math.pow(0, Q);
        } else {
          if (!(Q + 1 < 0 && grayArray[row][col] == 0))
            numerator += Math.pow((double)grayArray[row][col], Q + 1);
          
          if (!(Q < 0 && grayArray[row][col] == 0))
            denominator += Math.pow((double)grayArray[row][col], Q);
        }
      }
    }
    
    double result = 0;
    
    if (denominator != 0) result = numerator / denominator;
    if (result > 255) result = 255;
    
    return (int)result;
  }
  
  public static BufferedImage geometricMeanFilters(
      BufferedImage inputImage, int size) {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, 
        JavaRGBImage.getImageGrayArray(inputImage));
    
    int[][] filteredArray = performGMF(grayArray, size);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(width, height, filteredArray));
        
    return outputImage;
  }
  
  public static int[][] performGMF(int[][] grayArray, int filterSize) {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int result = getConvoluteValue_GMF(i, j, grayArray, filterSize);
        filteredArray[i][j] = result;
      }
    }
    
    return filteredArray;
  }
  
  public static int getConvoluteValue_GMF(int x, int y, int[][] grayArray, int filterSize) {
    double result = 1;
    
    for (int i = 0; i < filterSize; i++) {
      for (int j = 0; j < filterSize; j++) {
        int row = x - (filterSize / 2) + i;
        int col = y - (filterSize / 2) + j;
        
        if (!(row < 0 || col < 0 || row >= grayArray.length || 
            col >= grayArray[0].length)) {

          if (grayArray[row][col] != 0) 
            result *= Math.pow((double)(grayArray[row][col]), 
                1d / (double)(filterSize * filterSize));
        }
      }
    }
    return (int)result;
  }

  public static BufferedImage medianFilters(
      BufferedImage inputImage, int size) {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, 
        JavaRGBImage.getImageGrayArray(inputImage));
    
    int[][] filteredArray = performMF(grayArray, size);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(width, height, filteredArray));
        
    return outputImage;
  }
  
  public static int[][] performMF(int[][] grayArray, int filterSize) {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int result = getConvoluteValue_GMF(i, j, grayArray, filterSize);
        filteredArray[i][j] = result;
      }
    }
    
    return filteredArray;
  }
  
  public static int getConvoluteValue_MF(
      int x, int y, int[][] grayArray, int filterSize) {
    
    int totalSize = filterSize * filterSize;
    int[] num = new int[totalSize];
    int count = 0;
    
    for (int i = 0; i < totalSize; i++) {
      num[i] = 0;
    }
    
    for (int i = 0; i < filterSize; i++) {
      for (int j = 0; j < filterSize; j++) {
        int row = x - (filterSize / 2) + i;
        int col = y - (filterSize / 2) + j;
        
        if (!(row < 0 || col < 0 || row >= grayArray.length || 
            col >= grayArray[0].length)) {
          num[count++] = grayArray[i][j];
        }
      }
    }
    
    Arrays.sort(num);
    
    int median = 0;
    
    if (count % 2 == 0) 
      median = (num[totalSize - count / 2] + num[totalSize - count / 2 - 1]) / 2;
    else 
      median = num[totalSize - count / 2 + 1];
    
    return median;
  }

  public static BufferedImage maxFilters(
      BufferedImage inputImage, int size) throws InterruptedException {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, 
        JavaRGBImage.getImageGrayArray(inputImage));
    
    int[][] filteredArray = performMaxF(grayArray, size);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(width, height, filteredArray));
        
    return outputImage;
  }
  
  public static int[][] performMaxF(int[][] grayArray, int filterSize) 
      throws InterruptedException {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int result = getConvoluteValue_MaxF(i, j, grayArray, filterSize);
        filteredArray[i][j] = result;
      }
    }
    
    return filteredArray;
  }
  
  public static int getConvoluteValue_MaxF(
      int x, int y, int[][] grayArray, int filterSize) 
          throws InterruptedException {
    
    int max = 0;
    
    for (int i = 0; i < filterSize; i++) {
      for (int j = 0; j < filterSize; j++) {
        int row = x - (filterSize / 2) + i;
        int col = y - (filterSize / 2) + j;
        
        if (!(row < 0 || col < 0 || row >= grayArray.length || 
            col >= grayArray[0].length)) {
          if (grayArray[row][col] > max) {
            max = grayArray[row][col];
          }
        }
      }
    }
    
    return max;
  }
 
  public static BufferedImage minFilters(
      BufferedImage inputImage, int size) throws InterruptedException {
    BufferedImage outputImage = null;
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[][] grayArray = JavaRGBImage.oneDimToTwoDim(width, height, 
        JavaRGBImage.getImageGrayArray(inputImage));
    
    int[][] filteredArray = performMinF(grayArray, size);
    
    outputImage = JavaRGBImage.getBufferedImage(
        width, height, JavaRGBImage.twoDimToOneDim(width, height, filteredArray));
        
    return outputImage;
  }
  
  public static int[][] performMinF(int[][] grayArray, int filterSize) 
      throws InterruptedException {
    int height = grayArray.length;
    int width = grayArray[0].length;
    int[][] filteredArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int result = getConvoluteValue_MinF(i, j, grayArray, filterSize);
        filteredArray[i][j] = result;
      }
    }
    
    return filteredArray;
  }
  
  public static int getConvoluteValue_MinF(
      int x, int y, int[][] grayArray, int filterSize) 
          throws InterruptedException {
    
    int min = 255;
    
    for (int i = 0; i < filterSize; i++) {
      for (int j = 0; j < filterSize; j++) {
        int row = x - (filterSize / 2) + i;
        int col = y - (filterSize / 2) + j;
        
        if (!(row < 0 || col < 0 || row >= grayArray.length || 
            col >= grayArray[0].length))
          if (grayArray[row][col] < min) {
            min = grayArray[row][col];
          }
      }
    }
    
    return min;
  }
}
