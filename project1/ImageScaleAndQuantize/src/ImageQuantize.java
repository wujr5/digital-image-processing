
import java.awt.image.BufferedImage;

public class ImageQuantize {
	public BufferedImage grayImage;
	
	public ImageQuantize(BufferedImage img) {
		grayImage = changeRGBImageToGrayImage(img);
	}
	
	public BufferedImage changeRGBImageToGrayImage(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int ARGB = img.getRGB(j, i);
				img.setRGB(j, i, changeARGBToGray(ARGB));
			}
		}
		return img;
	}
	
	public int changeARGBToGray(int ARGB) {
		int A = (ARGB >> 24) & 0xFF;
		int R = (ARGB >> 16) & 0xFF;
		int G = (ARGB >> 8) & 0xFF;
		int B = ARGB & 0xFF;
		
		int gray = (int) (R * 0.3 + G * 0.59 + B * 0.11);
		
		int grayARGB = ((A << 24) & 0xFF000000)
				| ((gray << 16) & 0x00FF0000)
				| ((gray << 8) & 0x0000FF00)
				| (gray & 0x000000FF);
		
		return grayARGB;
	}
	
	public int changeAGRBByLevel(int gray, int gap) {
		int A = (gray >> 24) & 0xFF;
		int temp = gray & 0xFF;
		
		double ratio = ((double)temp - (int)(temp / gap) * gap) / gap;
		if (ratio > 0.4) {
			temp = (temp / gap + 1) * gap;
			if (temp > 255) temp = 255;
		} else {
			temp = (temp / gap) * gap;
		}
		
		int grayARGB = ((A << 24) & 0xFF000000)
				| ((temp << 16) & 0x00FF0000)
				| ((temp << 8) & 0x0000FF00)
				| (temp & 0x000000FF);
		
		return grayARGB;
	}
	
	public BufferedImage quantize(BufferedImage img, int level) {
		int gap = (int) (256 / (level - 1));
		int width = img.getWidth();
		int height = img.getHeight();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int grayARGB = img.getRGB(j, i);
				img.setRGB(j, i, changeAGRBByLevel(grayARGB, gap));
			}
		}
		return img;
	}
}
