package FourierTransformTool;

import java.awt.image.BufferedImage;


public class FFT {
  // 快速二维傅里叶变换
  public static Complex[][] fft_2d(Complex[][] fourierComplexs) {
    int newHeight = fourierComplexs.length;
    int newWidth = fourierComplexs[0].length;
    
    Complex[] temp1 = new Complex[newWidth];
    for (int x = 0; x < newHeight; x++) {
      for (int y = 0; y < newWidth; y++) {
        Complex c = new Complex(fourierComplexs[x][y].getR(), fourierComplexs[x][y].getI());
        temp1[y] = c;
      }
      fourierComplexs[x] = FFT.fft_1d(temp1);
    }

    Complex[] temp2 = new Complex[newHeight];
    for (int y = 0; y < newWidth; y++) {
      for (int x = 0; x < newHeight; x++) {
        Complex c = new Complex(fourierComplexs[x][y].getR(), fourierComplexs[x][y].getI());
        temp2[x] = c;
      }
      temp2 = FFT.fft_1d(temp2);
      for (int i = 0; i < newHeight; i++) {
        fourierComplexs[i][y] = temp2[i];
      }
    }
    
    return fourierComplexs;
  }
  
  public static Complex[][] fft_2d(int[][] paddingImage) {
    int height = paddingImage.length;
    int width = paddingImage[0].length;
    
    Complex[][] next = new Complex[height][width];
    
    Complex[] temp1 = new Complex[width];
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        Complex c = new Complex(paddingImage[x][y],0);
        temp1[y] = c;
      }
      next[x] = FFT.fft_1d(temp1);
    } 
    
    // 再把所有的列（已经被行的一维傅里叶变换所替代）都做一维傅里叶变换
    Complex[] temp2 = new Complex[height];
    for (int y = 0; y < width; y++) {
      for (int x = 0; x < height; x++) {
        Complex c = next[x][y];
        temp2[x] = c;
      }
      temp2 = FFT.fft_1d(temp2);
      for (int i = 0; i < height; i++) {
        next[i][y] = temp2[i];
      }
    }
    
    return next;
  }
      
  public static BufferedImage ifft_2d(Complex[][] g, int srcHeight, int srcWidth) {
    int height = g.length;
    int width = g[0].length;
    
    BufferedImage destimg = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_ARGB);
    
    Complex[] temp1 = new Complex[width];
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        Complex c = new Complex(g[x][y].getR(), g[x][y].getI());
        temp1[y] = c;
      }
      g[x] = FFT.ifft_1d(temp1);
    }
    
    Complex[] temp2 = new Complex[height];
    for (int y = 0; y < width; y++) {
      for (int x = 0; x < height; x++) {
        Complex c = g[x][y];
        temp2[x] = c;
      }
      temp2 = FFT.ifft_1d(temp2);
      for (int i = 0; i < height; i++) {
        g[i][y] = temp2[i];
      }
    }
    
    int[][] last = new int[height][width];
//----------------------------------------------------------------------
    // fifth：取实部
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        last[x][y] = (int)g[x][y].getR();
      }
    }
//----------------------------------------------------------------------    
    // sixth: move the image back and cut the image 乘以(-1)^(x+y)再剪裁图像
    int newalpha = 255 << 24;
    int newred, newblue, newgreen, newrgb;
    
    for (int i = 0; i < srcHeight; i++) {
      for (int j = 0; j < srcWidth; j++) {
        newred = last[i][j];
        if ((i+j)%2!=0) newred = -newred;
        newblue = newred; // 先写这个 ，如果先改变newred的值，newblue也会变成改过后的newred！
        newgreen = newred << 8; // 这个也一样，反正不能放到newred改变自己之前！
        newred = newred << 16;
        newrgb = newalpha | newred | newgreen | newblue;
        destimg.setRGB(j, i, newrgb);
      }
    }
//----------------------------------------------------------------------    
    return destimg;
  }
  
  // 快速一维傅里叶变换
  public static Complex[] fft_1d(Complex[] x) { //传入的全都是206
    int N = x.length;

    // base case
    if (N == 1) {
      return x;
    }
    
    // radix 2 Cooley-Turkey FFT
    if (N % 2 != 0) {
      throw new RuntimeException("N is not a power of 2");
    }
    
    // fft of even terms
    Complex[] even = new Complex[N/2];
    for (int k = 0; k < N/2; k++) {
      even[k] = x[2*k];
    }
    Complex[] q = fft_1d(even);
    
    // fft of odd terms
    Complex[] odd = new Complex[N/2];
    for (int k = 0; k < N/2; k++) {
      odd[k] = x[2*k+1]; //DEBUG  之前这里忘记+1  差点搞死我
    }
    Complex[] r = fft_1d(odd);
    
    // combine
    Complex[] y = new Complex[N];
    for (int k = 0; k < N/2; k++) {
      double kth = -2 * k * Math.PI / N;
      Complex wk = new Complex(Math.cos(kth), Math.sin(kth)); // all small number not 0
      y[k] = q[k].plus(wk.times(r[k]));
      y[k + N/2] = q[k].minus(wk.times(r[k]));
    }
    return y;
  }
  
  // 快速一维傅里叶逆变换
  public static Complex[] ifft_1d(Complex[] x) {
    int N = x.length;
    Complex[] y = new Complex[N];
    
    // take conjugate
    for (int i = 0; i < N; i++) {
      y[i] = x[i].conjugate();
    }
    
    // compute forward fft
    y = fft_1d(y);
    
    // take conguate again
    for (int i = 0; i < N; i++) {
      y[i] = y[i].conjugate();
    }
    
    // divide by N
    for (int i = 0; i < N; i++) {
      y[i] = y[i].times(1.0/N);
    }
    
    return y;
  }
  
  // 快速一维卷积
  public Complex[] convolve(Complex[] x, Complex[] y) {
    
    if (x.length != y.length) {
      throw new RuntimeException("Dimension don't agree");
    }
    
    int N = x.length;
    
    // compute fft of each sequence;
    Complex[] a = fft_1d(x);
    Complex[] b = fft_1d(y);
    
    // point-wise multiply
    Complex[] c = new Complex[N];
    for (int i = 0; i < N; i++) {
      c[i] = a[i].times(b[i]);
    }
    return c;
  }
}
