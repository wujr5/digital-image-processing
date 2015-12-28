package HW3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import FourierTransformTool.*;

public class Answer {
  public BufferedImage bufferImage;
  public String inputFileName;
  public String relativePath;
  public String postfix;
  
  Answer() {
    inputFileName = "70";
    relativePath = ".\\images\\";
    postfix = ".png";
    
    File file = new File(relativePath + inputFileName + postfix);
    try {
      bufferImage = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void HW3_2_2_DFT() throws IOException {
    Complex[][] fourierComplexs = DFT.dft_2d(bufferImage);
    BufferedImage spectrumImage = FourierTransform.getFourierSpectrumImage(
        fourierComplexs);
    ImageIO.write(spectrumImage, "png", new File(".\\images\\70_DFT_Spectrum.png"));
    
    BufferedImage destimg = DFT.idft_2d(fourierComplexs,
        bufferImage.getHeight(), bufferImage.getWidth());
    ImageIO.write(destimg, "png", new File(".\\images\\70_IDFT.png"));
  }
  
  public void HW3_2_3_FFT() throws IOException {
    int srcWidth = bufferImage.getWidth(null);
    int srcHeight = bufferImage.getHeight(null);
    int newWidth = FourierTransform.get2PowerEdge(srcWidth); // 获得2的整数次幂
    int newHeight = FourierTransform.get2PowerEdge(srcHeight);
    
    int[][] paddingImage = FourierTransform.getPaddingImage(
        bufferImage, newHeight, newWidth);
    Complex[][] fourierComplexs = FFT.fft_2d(paddingImage);
    
    BufferedImage destImage = 
        FourierTransform.getFourierSpectrumImage(fourierComplexs);
    ImageIO.write(destImage, "png", new File(".\\images\\70_FFT.png"));
    
    BufferedImage ifftImage =
        FFT.ifft_2d(fourierComplexs, srcHeight, srcWidth);
    ImageIO.write(ifftImage, "png", new File(".\\images\\70_IFFT.png"));
  }
  
  public void HW3_2_4_FrequencyDomainFilter() throws IOException {
    int srcWidth = bufferImage.getWidth(null);
    int srcHeight = bufferImage.getHeight(null);
    int newWidth = FourierTransform.get2PowerEdge(srcWidth); // 获得2的整数次幂
    int newHeight = FourierTransform.get2PowerEdge(srcHeight);
    
    int[][] paddingImage = FourierTransform.getPaddingImage(
        bufferImage, newHeight, newWidth);
    
    Complex[][] fourierComplexs = FFT.fft_2d(paddingImage);
    Complex[][] filterLaplace = Filter.getLaplaceFilter(newHeight, newWidth);
    Complex[][] laplaceFilteredImage = Filter.performFilter(fourierComplexs, filterLaplace);
    
    Complex[][] filterAverage = Filter.getAverageFilter(newHeight, newWidth);
    Complex[][] averageFilteredImage = Filter.performFilter(fourierComplexs, filterAverage);
    
    BufferedImage destAverageFilterImage = FFT.ifft_2d(averageFilteredImage, srcHeight, srcWidth);
    ImageIO.write(destAverageFilterImage, "png", new File(".\\images\\70_Average.png"));
    
    BufferedImage destimg = FFT.ifft_2d(laplaceFilteredImage, srcHeight, srcWidth);
    ImageIO.write(destimg, "png", new File(".\\images\\70_Laplace.png"));
    
    BufferedImage filterSpectrumImage = FourierTransform.getFourierSpectrumImage(filterLaplace);
    ImageIO.write(filterSpectrumImage, "png", new File(".\\images\\70_Laplace_Filter.png"));
    
    BufferedImage AverageFilterSpectrumImage = FourierTransform.getFourierSpectrumImage(filterAverage);
    ImageIO.write(AverageFilterSpectrumImage, "png", new File(".\\images\\70_Average_Filter.png"));
  }
}
