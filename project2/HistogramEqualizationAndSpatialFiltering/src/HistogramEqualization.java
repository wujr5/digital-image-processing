import java.awt.image.BufferedImage;

public class HistogramEqualization {
	
	public BufferedImage equalize_hist(BufferedImage bufferedImage) {
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		int[] grayArray = javaRGBImage.getImageGrayArray(bufferedImage);
		int[] equalizedArray = getEqualizedArray(width, height, grayArray);
		BufferedImage image = javaRGBImage.getBufferedImage(width, height, equalizedArray);
		
		return image;
	}
	
	public int[] getEqualizedArray(int width, int height, int[] grayArray) {
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
	
	public int[] getHistogramArray(int width, int height, int[] grayArray) {
		int[] histogramArray = new int[256];
		
		for (int i = 0; i < 256; i++) histogramArray[i] = 0;
		
		int length = width * height;
		
		for (int i = 0; i < length; i++) {
			histogramArray[grayArray[i]]++;
		}
		
		return histogramArray;
	}
	
}
