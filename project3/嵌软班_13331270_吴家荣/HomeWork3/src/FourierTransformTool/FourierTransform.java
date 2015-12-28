package FourierTransformTool;

import java.awt.image.BufferedImage;
import java.lang.Math;

public class FourierTransform {
  public static int[][] getPaddingImage(BufferedImage image, int newHeight, int newWidth) {
    int srcHeight = image.getHeight();
    int srcWidth = image.getWidth();
    
    int[][] paddingImage = new int[newHeight][newWidth];
    
    int pixel, newred;
    for (int i = 0; i < newHeight; i++) {
      for (int j = 0; j < newWidth; j++) {
        if (i < srcHeight && j < srcWidth) {
          pixel = image.getRGB(j, i);
          if ((i+j)%2==0) {
            newred = pixel & 0x00ff0000 >> 16;
          }
          else {
            newred = -(pixel & 0x00ff0000 >> 16);
          }
          paddingImage[i][j] = newred;
        }
        else {
          paddingImage[i][j] = 0;
        }
      }
    }
    
    return paddingImage;
  }

  // 根据图像的长获得2的整数次幂
  public static int get2PowerEdge(int e) {
    if (e == 1) return 1;
    int cur = 1;
    while(true) {
      if (e > cur && e <= 2 * cur) return 2*cur;
      else cur *= 2;
    }
  }
  
  // 返回傅里叶频谱图
  public static BufferedImage getFourierSpectrumImage(Complex[][] fourierComplex) {
    int height = fourierComplex.length;
    int width = fourierComplex[0].length;
    double max = 0;
    double min = 0;
    BufferedImage destimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    //-------------------First get abs(取模)--------------------------
    double[][] abs = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        abs[i][j] = fourierComplex[i][j].abs();
      }
    }   
    
    //-------------------Second get log(取log + 1)-------------------
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        abs[i][j] = Math.log(abs[i][j]+1);
      }
    }
    
    //-------------------Third quantization(量化)---------------------
    max = abs[0][0];
    min = abs[0][0];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (abs[i][j] > max)
          max = abs[i][j];
        if (abs[i][j] < min)
          min = abs[i][j];
      }
    }
    
    int level = 255;
    double interval = (max - min) / level;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int k = 0; k <= level; k++) {
          if (abs[i][j] >= k * interval && abs[i][j] < (k + 1) * interval) {
            abs[i][j] = (k * interval / (max - min)) * level;
            break;
          }
        }
      }
    }
    
    //-------------------Fourth setImage----------------------------
    int newalpha = 255 << 24;
    int newred, newblue, newgreen, newrgb;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        newred = (int)abs[i][j] << 16;
        newgreen = (int)abs[i][j] << 8;
        newblue = (int)abs[i][j];
        newrgb = newalpha | newred | newgreen | newblue;
        destimg.setRGB(j, i, newrgb);
      } 
    }
    return destimg;
  }
}