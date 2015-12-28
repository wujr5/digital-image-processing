package HW4Tool;

import java.awt.image.BufferedImage;

public class HEOnColorImages {
  public static BufferedImage equalize_hist_color_image(BufferedImage inputImage) {
    int[] RArray = getSingleRGBArray(inputImage, "R");
    int[] GArray = getSingleRGBArray(inputImage, "G");
    int[] BArray = getSingleRGBArray(inputImage, "B");
    
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    int length = RArray.length;
    
    BufferedImage outputImage = 
        new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    RArray = getEqualizedArray(width, height, RArray);
    GArray = getEqualizedArray(width, height, GArray);
    BArray = getEqualizedArray(width, height, BArray);
    
    for (int i = 0; i < length; i++) {
      int row = i / width;
      int col = i % width;
      
      int temp = (255 << 24) |
          (RArray[i] << 16) |
          (GArray[i] << 8) |
          (BArray[i]);
      
      outputImage.setRGB(col, row, temp);
    }
    
    return outputImage;
  }
  
  public static int[] getSingleRGBArray(BufferedImage inputImage, String type) {
    int shiftSize = 0;
    if (type == "R") shiftSize = 16;
    else if (type == "G") shiftSize = 8;
    else if (type == "B") shiftSize = 0;
    
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    
    int[] grayArray = new int[height * width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int ARGB = inputImage.getRGB(j, i);
        grayArray[i * width + j] = ((ARGB >> shiftSize) & 0xFF);
      }
    }
    
    return grayArray;
  }
  
  public static int[] getEqualizedArray(int width, int height, int[] grayArray) {
    int[] equalizedArray = new int[width * height];
    int[] histogramArray = getHistogramArray(width, height, grayArray);
    int[] hash = new int[256];
    
    for (int i = 0; i < 256; i++) {
      double rate = 0;
      for (int j = 0; j < i; j++) {
        rate += (double)(histogramArray[j]) / (double)(width * height);
      }
      hash[i] = (int)(255 * rate);
    }
    
    for (int i = 0; i < grayArray.length; i++) {
      equalizedArray[i] = hash[grayArray[i]];
    }
    
    return equalizedArray;
  }
  
  public static int[] getHistogramArray(int width, int height, int[] grayArray) {
    int[] histogramArray = new int[256];
    
    for (int i = 0; i < 256; i++) histogramArray[i] = 0;
    
    int length = width * height;
    
    for (int i = 0; i < length; i++) {
      histogramArray[grayArray[i]]++;
    }
    
    return histogramArray;
  }

  public static BufferedImage equalize_hist_color_image_using_average_histogram
  (BufferedImage inputImage) {
    int[] RArray = getSingleRGBArray(inputImage, "R");
    int[] GArray = getSingleRGBArray(inputImage, "G");
    int[] BArray = getSingleRGBArray(inputImage, "B");
    
    int height = inputImage.getHeight();
    int width = inputImage.getWidth();
    int length = RArray.length;
    
    BufferedImage outputImage = 
        new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    int[] RHistogramArray = getHistogramArray(width, height, RArray);
    int[] GHistogramArray = getHistogramArray(width, height, GArray);
    int[] BHistogramArray = getHistogramArray(width, height, BArray);
    
    int[] meanHistogramArray = new int[256];
    
    for (int i = 0; i < 256; i++) {
      meanHistogramArray[i] = (RHistogramArray[i] 
          + GHistogramArray[i] + BHistogramArray[i]) / 3;
    }
    
    int[] hash = new int[256];
    
    for (int i = 0; i < 256; i++) {
      double rate = 0;
      for (int j = 0; j < i; j++) {
        rate += (double)(meanHistogramArray[j]) / (double)(width * height);
      }
      hash[i] = (int)(255 * rate);
    }
    
    int[] newRArray = new int[length];
    int[] newGArray = new int[length];
    int[] newBArray = new int[length];
    
    for (int i = 0; i < length; i++) {
      newRArray[i] = hash[RArray[i]];
      newGArray[i] = hash[GArray[i]];
      newBArray[i] = hash[BArray[i]];
    }
    
    for (int i = 0; i < length; i++) {
      int row = i / width;
      int col = i % width;
      
      int temp = (255 << 24) |
          (newRArray[i] << 16) |
          (newGArray[i] << 8) |
          (newBArray[i]);
      
      outputImage.setRGB(col, row, temp);
    }
    
    return outputImage;
  }
  
}
