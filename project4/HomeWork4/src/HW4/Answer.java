package HW4;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import HW4Tool.HEOnColorImages;
import HW4Tool.ImageDenoising;
import HW4Tool.ImageFilter;

public class Answer {
  public BufferedImage task_1_image;
  public BufferedImage task_2_image;
  public BufferedImage task_3_image;
  
  public Answer() throws IOException {
    File file1 = new File(".\\images\\task_1.png");
    File file2 = new File(".\\images\\task_2.png");
    File file3 = new File(".\\images\\70.png");
    task_1_image = ImageIO.read(file1);
    task_2_image = ImageIO.read(file2);
    task_3_image = ImageIO.read(file3);
  }
  
  public void HW4_2_2_ImageFilter() throws IOException, InterruptedException {
    System.out.println("2.2.1：3*3 算术均值滤波");
    File file1 = new File(".\\images\\task_1_1_AMF_3X3.png");
    BufferedImage image_AMF_3X3 = ImageFilter.arithmeticMeanFilters(task_1_image, 3);
    ImageIO.write(image_AMF_3X3, "png", file1);
    System.out.println("2.2.1.1 done!");
    
    System.out.println("2.2.1：9*9 算术均值滤波");
    File file2 = new File(".\\images\\task_1_2_AMF_9X9.png");
    BufferedImage image_AMF_9X9 = ImageFilter.arithmeticMeanFilters(task_1_image, 9);
    ImageIO.write(image_AMF_9X9, "png", file2);
    System.out.println("2.2.1.2 done!");
    
    System.out.println("2.2.2：3*3 调和均值滤波");
    File file3 = new File(".\\images\\task_1_3_HMF_3X3.png");
    BufferedImage image_HMF_3X3 = ImageFilter.harmonicMeanFilter(task_1_image, 3);
    ImageIO.write(image_HMF_3X3, "png", file3);
    System.out.println("2.2.2.1 done!");
    
    System.out.println("2.2.2：9*9 调和均值滤波");
    File file4 = new File(".\\images\\task_1_4_HMF_9X9.png");
    BufferedImage image_HMF_9X9 = ImageFilter.harmonicMeanFilter(task_1_image, 9);
    ImageIO.write(image_HMF_9X9, "png", file4);
    System.out.println("2.2.2.2 done!");
    
    System.out.println("2.2.3：3*3 反调和均值滤波");
    File file5 = new File(".\\images\\task_1_5_CMHF_3X3.png");
    BufferedImage image_CHMF_3X3 = ImageFilter.contraHarmonicMeanFilter(task_1_image, 3, -1.5);
    ImageIO.write(image_CHMF_3X3, "png", file5);
    System.out.println("2.2.3.1 done!");
    
    System.out.println("2.2.3：9*9 反调和均值滤波");
    File file6 = new File(".\\images\\task_1_6_CMHF_9X9.png");
    BufferedImage image_CHMF_9X9 = ImageFilter.contraHarmonicMeanFilter(task_1_image, 9, -1.5);
    ImageIO.write(image_CHMF_9X9, "png", file6);
    System.out.println("2.2.3.2 done!");
  }

  public void HW4_2_3_ImageDenoising() throws IOException, InterruptedException {
    HW4_2_3_1_ImageFilter();
    HW4_2_3_2_ImageFilter();
    HW4_2_3_3_ImageFilter();
    HW4_2_3_4_ImageFilter();
  }
  
  public void HW4_2_3_1_ImageFilter() throws IOException {
    System.out.println("2.3.1：加性噪声：高斯噪声产生器");
    File file1 = new File(".\\images\\task_2_1_GN.png");
    BufferedImage image_gaussianNoise = ImageDenoising.addGaussianNoise(
        task_2_image, 100, 30);
    ImageIO.write(image_gaussianNoise, "png", file1);
    System.out.println("2.3.1.1 done!");
    
    System.out.println("2.3.1：加性噪声：椒盐噪声产生器");
    File file2 = new File(".\\images\\task_2_2_SPN.png");
    BufferedImage image_soltAndPepperNoise = ImageDenoising.addSaltAndPepperNoise(
        task_2_image, 0.1, 0.1);
    ImageIO.write(image_soltAndPepperNoise, "png", file2);
    System.out.println("2.3.1.2 done!");
  }
  
  public void HW4_2_3_2_ImageFilter() throws IOException, InterruptedException {
    System.out.println("2.3.2：生成mean=0，standardV=40的高斯噪声图像");
    File file3 = new File(".\\images\\task_2_3_GN_mean0_sv40.png");
    BufferedImage image_gaussianNoise_mean0_sv40 = 
        ImageDenoising.addGaussianNoise(task_2_image, 0, 40);
    ImageIO.write(image_gaussianNoise_mean0_sv40, "png", file3);
    System.out.println("2.3.2.1 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行3*3模板的算术均值滤波");
    File file4 = new File(".\\images\\task_2_4_gn_AMF_3X3.png");
    BufferedImage image_gn_AMF_3X3 = 
        ImageFilter.arithmeticMeanFilters(image_gaussianNoise_mean0_sv40, 3);
    ImageIO.write(image_gn_AMF_3X3, "png", file4);
    System.out.println("2.3.2.2 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行9*9模板的算术均值滤波");
    File file5 = new File(".\\images\\task_2_5_gn_AMF_9X9.png");
    BufferedImage image_gn_AMF_9X9 = 
        ImageFilter.arithmeticMeanFilters(image_gaussianNoise_mean0_sv40, 9);
    ImageIO.write(image_gn_AMF_9X9, "png", file5);
    System.out.println("2.3.2.3 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行3*3模板的几何均值滤波");
    File file6 = new File(".\\images\\task_2_6_gn_GMF_3X3.png");
    BufferedImage image_gn_GMF_3X3 = 
        ImageFilter.geometricMeanFilters(image_gaussianNoise_mean0_sv40, 3);
    ImageIO.write(image_gn_GMF_3X3, "png", file6);
    System.out.println("2.3.2.4 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行9*9模板的几何均值滤波");
    File file7 = new File(".\\images\\task_2_7_gn_GMF_9X9.png");
    BufferedImage image_gn_GMF_9X9 = 
        ImageFilter.geometricMeanFilters(image_gaussianNoise_mean0_sv40, 9);
    ImageIO.write(image_gn_GMF_9X9, "png", file7);
    System.out.println("2.3.2.5 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行3*3模板的中值滤波");
    File file8 = new File(".\\images\\task_2_8_gn_MF_3X3.png");
    BufferedImage image_gn_MF_3X3 = 
        ImageFilter.geometricMeanFilters(image_gaussianNoise_mean0_sv40, 3);
    ImageIO.write(image_gn_MF_3X3, "png", file8);
    System.out.println("2.3.2.6 done!");
    
    System.out.println("2.3.2：对高斯噪声图像进行9*9模板的中值滤波");
    File file9 = new File(".\\images\\task_2_9_gn_MF_9X9.png");
    BufferedImage image_gn_MF_9X9 = 
        ImageFilter.geometricMeanFilters(image_gaussianNoise_mean0_sv40, 9);
    ImageIO.write(image_gn_MF_9X9, "png", file9);
    System.out.println("2.3.2.7 done!");
  }
  
  public void HW4_2_3_3_ImageFilter() throws IOException {
    System.out.println("2.3.3：对图像添加盐噪声，salt rate = 0.2");
    File file1 = new File(".\\images\\task_2_10_salt_rate20%.png");
    BufferedImage image_SP_SR02 = 
        ImageDenoising.addSaltAndPepperNoise(task_2_image, 0.2, 0);
    ImageIO.write(image_SP_SR02, "png", file1);
    System.out.println("2.3.3.1 done!");
    
    System.out.println("2.3.3：对盐噪声图像进行3*3模板的调和滤波");
    File file2 = new File(".\\images\\task_2_11_SP_HMF_3X3.png");
    BufferedImage image_SP_HMF_3X3 = 
        ImageFilter.harmonicMeanFilter(image_SP_SR02, 3);
    ImageIO.write(image_SP_HMF_3X3, "png", file2);
    System.out.println("2.3.3.2 done!");
    
    System.out.println("2.3.3：对盐噪声图像进行3*3模板的反调和滤波, Q = 1.5");
    File file3 = new File(".\\images\\task_2_12_SP_CHMF_3X3_Q1p5.png");
    BufferedImage image_SP_CHMF_3X3_Q1p5 = 
        ImageFilter.contraHarmonicMeanFilter(image_SP_SR02, 3, 1.5);
    ImageIO.write(image_SP_CHMF_3X3_Q1p5, "png", file3);
    System.out.println("2.3.3.3 done!");
    
    System.out.println("2.3.3：对盐噪声图像进行3*3模板的反调和滤波, Q = -1.5");
    File file4 = new File(".\\images\\task_2_12_SP_CHMF_3X3_Qn1p5.png");
    BufferedImage image_SP_CHMF_3X3_Qn1p5 = 
        ImageFilter.contraHarmonicMeanFilter(image_SP_SR02, 3, -1.5);
    ImageIO.write(image_SP_CHMF_3X3_Qn1p5, "png", file4);
    System.out.println("2.3.2.4 done!");
  }
  
  public void HW4_2_3_4_ImageFilter() throws IOException, InterruptedException {
    System.out.println("2.3.4：对图像添加盐噪声，salt rate = 0.2, pepper rate = 0.2");
    File file1 = new File(".\\images\\task_2_13_salt_rate20%_pepper_rate20%.png");
    BufferedImage image_SP_SR02_PR02 = 
        ImageDenoising.addSaltAndPepperNoise(task_2_image, 0.2, 0.2);
    ImageIO.write(image_SP_SR02_PR02, "png", file1);
    System.out.println("2.3.4.1 done!");
    
    System.out.println("2.3.4：对盐噪声图像进行3*3模板的算术均值滤波");
    File file2 = new File(".\\images\\task_2_14_SP_AMF_3X3.png");
    BufferedImage image_SP_AMF_3X3 = 
        ImageFilter.arithmeticMeanFilters(image_SP_SR02_PR02, 3);
    ImageIO.write(image_SP_AMF_3X3, "png", file2);
    System.out.println("2.3.4.2 done!");
    
    System.out.println("2.3.4：对盐噪声图像进行3*3模板的几何均值滤波");
    File file3 = new File(".\\images\\task_2_15_SP_GMF_3X3.png");
    BufferedImage image_SP_GMF_3X3 = 
        ImageFilter.arithmeticMeanFilters(image_SP_SR02_PR02, 3);
    ImageIO.write(image_SP_GMF_3X3, "png", file3);
    System.out.println("2.3.4.2 done!");
    
    System.out.println("2.3.4：对盐噪声图像进行3*3模板的最大值滤波");
    File file4 = new File(".\\images\\task_2_16_SP_MaxF_3X3.png");
    BufferedImage image_SP_MaxF_3X3 = 
        ImageFilter.maxFilters(image_SP_SR02_PR02, 3);
    ImageIO.write(image_SP_MaxF_3X3, "png", file4);
    System.out.println("2.3.4.3 done!");
    
    System.out.println("2.3.4：对盐噪声图像进行3*3模板的最小值滤波");
    File file5 = new File(".\\images\\task_2_17_SP_MinF_3X3.png");
    BufferedImage image_SP_MinF_3X3 = 
        ImageFilter.minFilters(image_SP_SR02_PR02, 3);
    ImageIO.write(image_SP_MinF_3X3, "png", file5);
    System.out.println("2.3.4.4 done!");
    
    System.out.println("2.3.4：对盐噪声图像进行3*3模板的中值滤波");
    File file6 = new File(".\\images\\task_2_18_SP_MedianF_3X3.png");
    BufferedImage image_SP_MedianF_3X3 = 
        ImageFilter.medianFilters(image_SP_SR02_PR02, 3);
    ImageIO.write(image_SP_MedianF_3X3, "png", file6);
    System.out.println("2.3.4.5 done!");
  }

  public void HW4_2_4_HE_on_Color_Images() throws IOException {
    System.out.println("2.4.1：对RGB三个通道进行均衡化处理，并重建图像");
    File file1 = new File(".\\images\\task_3_19_HE_On_Color_Image.png");
    BufferedImage image_HE_on_color_image = 
        HEOnColorImages.equalize_hist_color_image(task_3_image);
    ImageIO.write(image_HE_on_color_image, "png", file1);
    System.out.println("2.4.1.1 done!");
    
    System.out.println("2.4.2：取得RGB三个通道的平均直方图，根据平均直方图分别对RGB\n"
        + "三个分量进行均衡化处理，并重建图像");
    File file2 = new File(".\\images\\task_3_20_HE_with_mean_histogram_On_Color_Image.png");
    BufferedImage image_HE_with_mean_histogram_on_color_image = 
        HEOnColorImages.equalize_hist_color_image_using_average_histogram(task_3_image);
    ImageIO.write(image_HE_with_mean_histogram_on_color_image, "png", file2);
    System.out.println("2.4.2.1 done!");
  }
}
