import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;

public class BilineInterpolationScale {
	public BilineInterpolationScale(String filename) throws IOException {
		File f = new File(filename);
		image = ImageIO.read(f);
	}
	
	public int[] imgScale(int[] inPixelsData, int srcW, int srcH, int destW, int destH) {
		double[][][] input3DData = processOneToThreeDeminsion(inPixelsData, srcH, srcW);
		
		int[][][] outputThreeDeminsionData = new int[destH][destW][4];
		float rowRatio = ((float)srcH)/((float)destH);
		float colRatio = ((float)srcW)/((float)destW);
		for(int row=0; row<destH; row++) {
			// convert to three dimension data
			double srcRow = ((float)row)*rowRatio;
			double j = Math.floor(srcRow);
			double t = srcRow - j;
			for(int col=0; col<destW; col++) {
				double srcCol = ((float)col)*colRatio;
				double k = Math.floor(srcCol);
				double u = srcCol - k;
				double coffiecent1 = (1.0d-t)*(1.0d-u);
				double coffiecent2 = (t)*(1.0d-u);
				double coffiecent3 = t*u;
				double coffiecent4 = (1.0d-t)*u;
				
				outputThreeDeminsionData[row][col][0] = (int)(coffiecent1 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)k,srcW-1, 0)][0] +
				coffiecent2 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)k,srcW-1,0)][0] +
				coffiecent3 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)(k+1),srcW-1,0)][0] +
				coffiecent4 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)(k+1),srcW-1,0)][0]); // alpha
				
				outputThreeDeminsionData[row][col][1] = (int)(coffiecent1 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)k,srcW-1, 0)][1] +
						coffiecent2 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)k,srcW-1,0)][1] +
						coffiecent3 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)(k+1),srcW-1,0)][1] +
						coffiecent4 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)(k+1),srcW-1,0)][1]); // red
				
				outputThreeDeminsionData[row][col][2] = (int)(coffiecent1 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)k,srcW-1, 0)][2] +
						coffiecent2 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)k,srcW-1,0)][2] +
						coffiecent3 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)(k+1),srcW-1,0)][2] +
						coffiecent4 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)(k+1),srcW-1,0)][2]);// green
				
				outputThreeDeminsionData[row][col][3] = (int)(coffiecent1 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)k,srcW-1, 0)][3] +
						coffiecent2 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)k,srcW-1,0)][3] +
						coffiecent3 * input3DData[getClip((int)(j+1),srcH-1,0)][getClip((int)(k+1),srcW-1,0)][3] +
						coffiecent4 * input3DData[getClip((int)j,srcH-1,0)][getClip((int)(k+1),srcW-1,0)][3]); // blue
			}
		}
		
		return convertToOneDim(outputThreeDeminsionData, destW, destH);
	}
	
	private int getClip(int x, int max, int min) {
		return x>max ? max : x<min? min : x;
	}
	
	/* <p> The purpose of this method is to convert the data in the 3D array of ints back into </p>
	 * <p> the 1d array of type int. </p>
	 * 
	 */
	public int[] convertToOneDim(int[][][] data, int imgCols, int imgRows) {
		// Create the 1D array of type int to be populated with pixel data
		int[] oneDPix = new int[imgCols * imgRows * 4];


		// Move the data into the 1D array. Note the
		// use of the bitwise OR operator and the
		// bitwise left-shift operators to put the
		// four 8-bit bytes into each int.
		for (int row = 0, cnt = 0; row < imgRows; row++) {
			for (int col = 0; col < imgCols; col++) {
				oneDPix[cnt] = ((data[row][col][0] << 24) & 0xFF000000)
						| ((data[row][col][1] << 16) & 0x00FF0000)
						| ((data[row][col][2] << 8) & 0x0000FF00)
						| ((data[row][col][3]) & 0x000000FF);
				cnt++;
			}// end for loop on col
		}// end for loop on row
		
		return oneDPix;
	}// end convertToOneDim
	
	private double [][][] processOneToThreeDeminsion(int[] oneDPix2, int imgRows, int imgCols) {
		double[][][] tempData = new double[imgRows][imgCols][4];
		for(int row=0; row<imgRows; row++) {
			
			// per row processing
			int[] aRow = new int[imgCols];
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				aRow[col] = oneDPix2[element];
			}
			
			// convert to three dimension data
			for(int col=0; col<imgCols; col++) {
				tempData[row][col][0] = (aRow[col] >> 24) & 0xFF; // alpha
				tempData[row][col][1] = (aRow[col] >> 16) & 0xFF; // red
				tempData[row][col][2] = (aRow[col] >> 8) & 0xFF;  // green
				tempData[row][col][3] = (aRow[col]) & 0xFF;       // blue
			}
		}
		return tempData;
	}
	
	public BufferedImage image;
}

