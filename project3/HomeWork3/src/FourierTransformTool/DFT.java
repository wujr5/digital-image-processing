package FourierTransformTool;

import java.awt.image.BufferedImage;

import FourierTransformTool.Complex;
import FourierTransformTool.JavaRGBImage;

public class DFT {
  public static Complex[][] dft_2d(BufferedImage image) {
    int[] oneDimGrayArray = JavaRGBImage.getImageGrayArray(image);
    int[][] twoDimGrayArray = JavaRGBImage.oneDimToTwoDim(
        image.getWidth(), image.getHeight(), oneDimGrayArray);
    
    twoDimGrayArray = centralized(twoDimGrayArray);
    
    Complex fourierArray[][] = new Complex[image.getHeight()][image.getWidth()];
    
    for (int u = 0; u < image.getHeight(); u++) {
      for (int v = 0; v < image.getWidth(); v++) {
        fourierArray[u][v] = computeDFT(twoDimGrayArray, u, v);
      }
      
      System.out.println("DFT_2d: " + u);
    }
    
    return fourierArray;
  }
  
  public static BufferedImage idft_2d(Complex[][] fourierComplexs, int srcHeight, int srcWidth) {
    int height = fourierComplexs.length;
    int width = fourierComplexs[0].length;
    
    BufferedImage destimg = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_ARGB);
    Complex[][] destComplexs = new Complex[height][width];
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        destComplexs[i][j] = computeIDFT(fourierComplexs, i, j);
      }
      System.out.println("IDFT_2d: " + i);
    }
    
    int[][] last = new int[height][width];

    // fifth：取实部
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        last[x][y] = (int)destComplexs[x][y].getR();
      }
    }

    // sixth: move the image back and cut the image 乘以(-1)^(x+y)再剪裁图像
    int newalpha = 255 << 24;
    int newred, newblue, newgreen, newrgb;
    
    for (int i = 0; i < srcHeight; i++) {
      for (int j = 0; j < srcWidth; j++) {
        newred = last[i][j];
        if ((i+j)%2!=0) newred = -newred;
        newblue = newred;
        newgreen = newred << 8;
        newred = newred << 16;
        newrgb = newalpha | newred | newgreen | newblue;
        destimg.setRGB(j, i, newrgb);
      }
    }
    
    return destimg;
  }
  
  public static Complex computeIDFT(Complex[][] g, int x, int y) {
    int height = g.length;
    int width = g[0].length;
    Complex c = new Complex(0, 0);
    
    for (int u = 0; u < height; u++) {
      for (int v = 0; v < width; v++) {
        double a = (double)(u * x) / (double)height;
        double b = (double)(v * y) / (double)width;
        Complex temp = new Complex(0, 2 * Math.PI * (a + b));
        c = c.plus(temp.exp().times(g[u][v]));
      }
    }
    
    return c.times(1d / (double)(height * width));
  }
  
  public static Complex computeDFT(int[][] grayImage, int u, int v) {
    int M = grayImage.length;
    int N = grayImage[0].length;
    Complex c = new Complex(0, 0);
    for (int x = 0; x < M; x++) {
      for (int y = 0; y < N; y++) {
        double a = (double)(u * x) / (double)M;
        double b = (double)(v * y) / (double)N;
        Complex temp = new Complex(0, -2 * Math.PI * (a + b));
        c = c.plus(temp.exp().times(grayImage[x][y]));
      }
    }
    
    return c;
  }
  
  public static int[][] centralized(int[][] grayImage) {
    int height = grayImage.length;
    int width = grayImage[0].length;
    
    int[][] centralizedGrayImage = new int[height][width];
    
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        if ((x + y) % 2 == 0) centralizedGrayImage[x][y] = grayImage[x][y];
        else centralizedGrayImage[x][y] = -grayImage[x][y];
      }
    }
    
    return centralizedGrayImage;
  }
}
