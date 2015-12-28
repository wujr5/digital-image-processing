package FourierTransformTool;

import java.awt.image.BufferedImage;

public class JavaRGBImage {
  public static int[] getImageGrayArray(BufferedImage img) {
    int width = img.getWidth();
    int height = img.getHeight();
    
    int[] grayArray = new int[width * height];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int temp = img.getRGB(j, i);
        grayArray[i * width + j] = temp & 0xFF;
      }
    }
    
    return grayArray;
  }
  
  public static BufferedImage getBufferedImage(int width, int height, int[] grayArray) {
    BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for(int i= 0 ; i < height; i++) {
      for(int j = 0 ; j < width; j++) {
        int gray = grayArray[i * width + j];
        
        int tem = ((255 << 24) & 0xFF000000) // a
            | ((gray << 16) & 0x00FF0000)     // r
            | ((gray << 8) & 0x0000FF00)      // g
            | (gray & 0x000000FF);            // b
        
        grayImage.setRGB(j, i, tem);
      }
    }
    
    return grayImage;
  }
  
  public static int[][] oneDimToTwoDim(int width, int height, int[] oneDimArray) {
    int[][] twoDimArray = new int[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        twoDimArray[i][j] = oneDimArray[i * width + j];
      }
    }
    
    return twoDimArray;
  }
  
  public static int[] twoDimToOneDim(int width, int height, int[][] twoDimArray) {
    int[] oneDimArray = new int[width * height];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        oneDimArray[i * width + j] = twoDimArray[i][j];
      }
    }
    
    return oneDimArray;
  }
}
