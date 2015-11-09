import java.awt.image.BufferedImage;

public class SpatialFiltering {
	
	public BufferedImage highBoostfiltering(BufferedImage bufferedImage) {
		
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		int[][] filter = new int[3][3];
		
		for(int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				filter[i][j] = 1;
		
		int[] grayArray = javaRGBImage.getImageGrayArray(bufferedImage);
		
		int[] filteredArray = getFilterArray(width, height, grayArray, filter);
		
		int[] highBoostArray = new int[width * height];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int len = i * width + j;
				highBoostArray[len] = grayArray[len] + 3 * (grayArray[len] - filteredArray[len]); 
			}
		}
		
		BufferedImage image = javaRGBImage.getBufferedImage(width, height, highBoostArray);
		
		return image;
	}
	
	public BufferedImage filterLaplacian(BufferedImage bufferedImage, int[][] filter) {
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		
		int[] grayArray = javaRGBImage.getImageGrayArray(bufferedImage);
		
		int[] filteredArray = getFilterLaplacianArray(bufferedImage.getWidth(), 
				bufferedImage.getHeight(), grayArray, filter);
		
		BufferedImage image = javaRGBImage.getBufferedImage(bufferedImage.getWidth(), 
				bufferedImage.getHeight(), filteredArray);
		
		return image;
	}
	
	public int[] getFilterLaplacianArray(int width, int height, int[] grayArray, int[][] filter) {
		int[] filteredArray = new int[width * height];
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		int[][] twoDimGrayArray = javaRGBImage.oneDimToTwoDim(width, height, grayArray);
		
		int templateWidth = filter.length;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				filteredArray[i * width + j] = grayArray[i * width + j] + getFilteredValue(i, j, twoDimGrayArray,
						filter, templateWidth, width, height);
				if (filteredArray[i * width + j] < 0) filteredArray[i * width + j] = 0;
			}
		}
		
		return filteredArray;
	}
	
	public int getLaplacianFilteredValue(int row, int col, int[][] twoDimGrayArray, 
			int[][] filter, int templateWidth, int width, int height) {
		
		int sum = 0;
		int gap = (templateWidth - 1) / 2;
		
		for (int i = 0; i < templateWidth; i++) {
			for (int j = 0; j < templateWidth; j++) {
				if (row + i - gap >= 0 && row + i - gap < height && 
						col + j - gap >= 0 && col + j - gap < width)
					sum += twoDimGrayArray[row + i - gap][col + j - gap] * filter[i][j];
			}
		}
		
		return sum;
	}
	
	public BufferedImage filter2d(BufferedImage bufferedImage, int[][] filter) {
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		
		int[] grayArray = javaRGBImage.getImageGrayArray(bufferedImage);
		
		int[] filteredArray = getFilterArray(bufferedImage.getWidth(), 
				bufferedImage.getHeight(), grayArray, filter);
		
		BufferedImage image = javaRGBImage.getBufferedImage(bufferedImage.getWidth(), 
				bufferedImage.getHeight(), filteredArray);
		
		return image;
	}
	
	public int[] getFilterArray(int width, int height, int[] grayArray, int[][] filter) {
		int[] filteredArray = new int[width * height];
		JavaRGBImage javaRGBImage = new JavaRGBImage();
		int[][] twoDimGrayArray = javaRGBImage.oneDimToTwoDim(width, height, grayArray);
		
		int templateWidth = filter.length;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				filteredArray[i * width + j] = getFilteredValue(i, j, twoDimGrayArray,
						filter, templateWidth, width, height);
			}
		}
		
		return filteredArray;
	}
	
	public int getFilteredValue(int row, int col, int[][] twoDimGrayArray, 
			int[][] filter, int templateWidth, int width, int height) {
		
		int filterValue = 0;
		int sum = 0;
		int gap = (templateWidth - 1) / 2;
		
		for (int i = 0; i < templateWidth; i++) {
			for (int j = 0; j < templateWidth; j++) {
				if (row + i - gap >= 0 && row + i - gap < height && 
						col + j - gap >= 0 && col + j - gap < width)
					sum += twoDimGrayArray[row + i - gap][col + j - gap];
			}
		}
		
		filterValue = sum / (templateWidth * templateWidth);
		
		return filterValue;
	}
	
}
