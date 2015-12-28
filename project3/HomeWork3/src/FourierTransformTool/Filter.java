package FourierTransformTool;

public class Filter {
  public static Complex[][] performFilter(Complex[][] fourierComplexs, Complex[][] filter) {
    int height = fourierComplexs.length;
    int width = fourierComplexs[0].length;
    
    Complex[][] filteredImage = new Complex[height][width];
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        filteredImage[x][y] = filter[x][y].times(fourierComplexs[x][y]);
      }
    }
    
    return filteredImage;
  }
  
  public static Complex[][] getLaplaceFilter(int height, int width) {
    // 构造原始滤波函数
    Complex[][] filter = new Complex[height][width];
    
    //下面这个是拉普拉斯滤波
    filter[0][0] = new Complex(0, 0);
    filter[0][1] = new Complex(-1, 0);
    filter[0][2] = new Complex(0, 0);
    filter[1][0] = new Complex(-1, 0);
    filter[1][1] = new Complex(4, 0);
    filter[1][2] = new Complex(-1, 0);
    filter[2][0] = new Complex(0, 0);
    filter[2][1] = new Complex(-1, 0);
    filter[2][2] = new Complex(0, 0);
    
    for (int x = 0; x < height; x++)
      for (int y = 0; y < width; y++)
        if (x < 3 && y < 3) {}
        else filter[x][y] = new Complex(0, 0);

    filter = FFT.fft_2d(filter);
    
    return filter;
  }
  
  public static Complex[][] getAverageFilter(int height, int width) {
    
    // 构造原始滤波函数
    Complex[][] filter = new Complex[height][width];
    //这个是7*7均值滤波
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        if (x < 7 && y < 7) {
          if ((x+y)%2==0)
            filter[x][y] = new Complex(1/49d, 0); // double 后面赋值数字记得加d！！！！！！！
          else
            filter[x][y] = new Complex(-1/49d, 0);
        }
        else {
          filter[x][y] = new Complex(0, 0);
        }
      }
    }
    
    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        if (x < 7 && y < 7) {/*上面已经写好了*/}
        else {
          filter[x][y] = new Complex(0, 0);
        }
      }
    }
    
    filter = FFT.fft_2d(filter);
    
    return filter;
  }
}
