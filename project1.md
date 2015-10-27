# project1分析

## 1 前言

### 1.1 语言工具

#### 1.1.1 Java

> Java是我用来处理这次project的语言，因此首先说一下我对使用Java的感受。

我感觉使用Java来完成这次作业最大的好处就是，在实现project要求的功能的情况下，更好地拓展自己的程序，做出跨平台应用。

相比于Matlab，Java处理灰度图像相对复杂一点。拿png格式的图像来说，利用Java的`BufferedImage`包，将图像存储成`BufferedImage`类型，进而对该类型的数据进行处理。

#### 1.1.2 Matlab

> 我不是用Matlab来处理这次的图像的，因此不是很熟悉，但是据其他同学的反映，Matlab相对来说比较简单，可以直接操作灰度值，不需要转换。像素值与灰度值一一对应。

### 1.2 Java处理png图像

> 目前分析的是png格式的图像，其他格式的未做深究。关于png格式更详细的信息，参考[百度百科-png][]

[百度百科-png]: http://baike.baidu.com/link?url=2vLzuwvunplByud_l7Sb0PVhMtaUKtViUrNMrp8Mp5sjVLqh1kvrEK46heMYC4SApJNgSqYUx0fVNkM4GuXlXK


#### 1.2.1 BufferedImage存储数据的格式

除去其他相关属性，比如宽度，高度等属性，`BufferedImage`类型存储的最终要的数据是ARGB的信息。就是alpha通道值、红色值、绿色值、蓝色值的信息。

考虑以下的例子：

```java
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;

public class Example {
	public BufferedImage image;
	public static void main(String[] args) {
		File f = new File(filename);
		image = ImageIO.read(f);
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int ARGB = image.getRGB(j, i);
				int A = (ARGB >> 24) & 0xFF;
				int R = (ARGB >> 16) & 0xFF;
				int G = (ARGB >> 8) & 0xFF;
				int B = ARGB & 0xFF;
				System.out.println(AGRB);
				System.out.println(A);
				System.out.println(R);
				System.out.println(G);
				System.out.println(B);
			}
		}
	}
}
```

> 本段代码没有经过编译调试，读者注意一下。

正如程序给出的变量，`ARGB`就是存储在`BufferedImage`图片中Alpha值，Red值，Green值和Blue值。每一个只占据一个字节的位置，因此可以通过位运算来逐个取出。

#### 1.2.2 RGB值转灰度值

> 关于灰度值的定义，可以参考一下[百度百科-灰度值][]。下面讲一下我的通俗的理解，并不专业。

[百度百科-灰度值]: http://baike.baidu.com/link?url=2n0g8QBNTobQDz_Q_4em04EQkNei25eFGUZ4sdWWu0o7qKcpqX8rgGx2Yr3byq5GDolru_fTBPZySaoKqBiDTa

灰度值其实也是用RGB来表示的，唯一的区别就是，灰度值的RGB值`R = G = B`，三值相等，范围都在0到255之间。其中0表示全黑，255表示全白。Alpha值可以不予考虑。

RGB和Gray值的关系，有几种不同的计算方式，我选择的是下面的这种。

```java
int gray = R * 0.3 + G * 0.59 + B * 0.11;
```

因此只要求出新的ARGB然后赋值给原来的图像，就能得到灰度图。举例如下。

```java
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;

public class Example {
	public BufferedImage image;
	public static void main(String[] args) {
		File f = new File(filename);
		image = ImageIO.read(f);
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				int ARGB = image.getRGB(j, i);
				int A = (ARGB >> 24) & 0xFF;
				int R = (ARGB >> 16) & 0xFF;
				int G = (ARGB >> 8) & 0xFF;
				int B = ARGB & 0xFF;
				
				int gray = R * 0.3 + G * 0.59 + B * 0.11;
				
				int ARGB = ((A << 24) & 0xFF000000)
					| ((gray << 16) & 0x00FF0000)
					| ((gray << 8) & 0x0000FF00)
					| (gray & 0x000000FF);
				
				image.setRGB(j, i, ARGB);
			}
		}
	}
}
```

> 本段代码没有经过编译调试，读者注意一下。

现在已经实现了RGB值和灰度值的转换，把这个作为函数封装一下，就相当于可以直接处理灰度值了，而不需要考虑ARGB每个值的信息。

## 2 图像放缩

图像放缩是对数字图像很常见很基础的处理。放缩分为放大和缩小，无论是放大还是缩小，都是根据原图像的相关像素的灰度值来估计目的像素的灰度值，放缩的质量取决于估计方式。

简单且常见的估计方式有两种，最近邻插值法，双线性插值法。对于两种方式的分析，可以参考一下：[图像缩放--OpenCV cvResize函数--最近邻插值---双线性插值--基本原理][]

[图像缩放--OpenCV cvResize函数--最近邻插值---双线性插值--基本原理]: http://www.cnblogs.com/yanyojun/p/3515721.html

> 下面我根据自己的理解讲一下双线性插值法。


### 2.1 双线性插值法

> 对于双线性，其实可以这样理解，就是根据给定的值，两次线性地需找合适的值。

#### 2.1.1 第一步 根据目的像素寻找原图像相关像素

双线性插值的根据目的像素坐标，寻找原图像相关像素坐标的策略是，根据比例求浮点坐标，再把浮点坐标分成整数部分和小数部分。示例如下。

```java
// srcWidth：原图像宽，srcHeight：原图像高
// destWidth：目的图像宽，destHeight：目的图像高
// x：目的像素的x坐标，y：目的像素的y坐标
// 目的像素值得函数为：D(x, y)，原图像对应浮点坐标为S(j+c, k+u)
// 其中j，k为浮点坐标的整数部分，c，u为对应的小数部分，在0和1之间

double rowRatio = (double)(srcWidth) / (double)(destWidth);
double colRatio = (double)(srcHeight) / (double)(destHeight);

int j = (int)(x * rowRatio);
int k = (int)(y * colRatio);

double c = (double)(x) * rowRatio - j;
double u = (double)(y) * colRatio - k;

```

这样就求出了在原图像对应的四个像素坐标位置：

```java
S(j, k)     , S(j + 1, k)
S(j, K + 1) , S(j + 1, k + 1)

```

`S(j + c, k + u)`大概处于下面圆点所在位置。

```java
----------------------
|         |          |
| S(j, k) | S(j+1, k)|
|         |          |
----------|-----------
|         | .        |
| S(j,k+1)|S(j+1,k+1)|
|         |          |
----------------------
```

因为`0 =< c, u =< 1`，因此`S(j+c, k+u)`在`S(j, k)`和`S(j+1, k+1)`之间。

#### 2.1.2 第二步 X方向上的插值

现在我们已经取得了`j, k, c, u`的值了，也就是取得了最相关的4个原图像坐标的值了。现在进行第一个线性插值，也就是x方向上的插值。

插值的思路是，确定权重。对于`S(j+c, k+u)`，我找到了与它最相邻的四个点。现在我需要根据`S(j+c, k+u)`与其他四个点的相邻关系来确定权重。

越接近`S(j+c, k+u)`的值理应取得更大的权重，由于c，u均是小数，因此得到如下的权重分配的插值。

```java
int Q11 = S(j, k)(1 - c) + S(j+1, k) * c;
int Q22 = S(j, k+1) * (1 - c) + S(j+1, k+1) * c;
```

c越小，说明`S(j+c, k+u)`在x方向上，相对于`S(j, k+1)`越接近`S(j, k)`，对`S(j, k+1)`和`S(j+1, k+1)`同理。

#### 2.1.3 第三步 Y方向上的插值

y方向上的插值的思路，跟x方向上的插值思路一致。不过现在我们操作的对象是，在x方向上插值得到的`Q11`和`Q22`。如下所示：

```java
// Dxy 为通过两次插值得到的目的像素坐标上的灰度值。
int Dxy = Q11 * (1 - u) + Q22 * u;
```

展开后得到

```java
int Dxy = S(j, k) * (1 - c) * (1 - u)
			+ S(j+1, k) * c * (1 - u)
			+ S(j, k+1) * (1 - c) * u
			+ S(j+1, k+1) * c * u;
```

因此得到了四个处理因子：

```java
int f1 = (1 - c) * (1 - u);
int f2 = c * (1 - u);
int f3 = (1 - c) * u;
int f4 = c * u;
```

### 2.2 实例代码分析

> 现在给出完整的代码实现方案。

```java
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;

public class BilineInterpolationScale {
	public BufferedImage image;
	
	public BilineInterpolationScale(String filename) throws IOException {
		File f = new File(filename);
		image = ImageIO.read(f);
	}
	
	public int[] imgScale(int[] inPixelsData, int srcW, int srcH, int destW, int destH) {
		double[][][] input3DData = processOneToThreeDeminsion(inPixelsData, srcH, srcW);
		
		int[][][] outputThreeDeminsionData = new int[destH][destW][4];
		float rowRatio = ((float)srcH) / ((float)destH);
		float colRatio = ((float)srcW) / ((float)destW);
		
		for(int row = 0; row < destH; row++) {
			
			double srcRow = ((float)row) * rowRatio;
			double j = Math.floor(srcRow);
			double t = srcRow - j;
			
			for(int col = 0; col < destW; col++) {
				
				double srcCol = ((float)col) * colRatio;
				double k = Math.floor(srcCol);
				double u = srcCol - k;
				
				double coffiecent1 = (1.0d - t) * (1.0d - u);
				double coffiecent2 = (t) * (1.0d - u);
				double coffiecent3 = t * u;
				double coffiecent4 = (1.0d - t) * u;
				
				for (int i = 0; i < 4; i++) {
					outputThreeDeminsionData[row][col][i] = (int)(
						coffiecent1 * input3DData[getClip((int)j, srcH - 1, 0)][getClip((int)k, srcW - 1, 0)][i] +
						coffiecent2 * input3DData[getClip((int)(j + 1), srcH - 1, 0)][getClip((int)k, srcW - 1, 0)][i] +
						coffiecent3 * input3DData[getClip((int)(j + 1), srcH - 1, 0)][getClip((int)(k + 1),srcW - 1, 0)][i] +
						coffiecent4 * input3DData[getClip((int)j, srcH - 1, 0)][getClip((int)(k + 1), srcW - 1, 0)][i]
					);
					
				}
			}
		}
		
		return convertToOneDim(outputThreeDeminsionData, destW, destH);
	}
	
	private int getClip(int x, int max, int min) {
		return x > max ? max : x < min ? min : x;
	}
	
	public int[] convertToOneDim(int[][][] data, int imgCols, int imgRows) {
		int[] oneDPix = new int[imgCols * imgRows * 4];

		for (int row = 0, cnt = 0; row < imgRows; row++) {
			for (int col = 0; col < imgCols; col++) {
				
				oneDPix[cnt] = 
						  ((data[row][col][0] << 24) & 0xFF000000)
						| ((data[row][col][1] << 16) & 0x00FF0000)
						| ((data[row][col][2] << 8) & 0x0000FF00)
						| ((data[row][col][3]) & 0x000000FF);
				
				cnt++;
			}
		}
		return oneDPix;
	}
	
	private double [][][] processOneToThreeDeminsion(int[] oneDPix2, int imgRows, int imgCols) {
		double[][][] tempData = new double[imgRows][imgCols][4];
		for(int row=0; row<imgRows; row++) {
			
			int[] aRow = new int[imgCols];
			for (int col = 0; col < imgCols; col++) {
				int element = row * imgCols + col;
				aRow[col] = oneDPix2[element];
			}
			
			for(int col = 0; col < imgCols; col++) {
				tempData[row][col][0] = (aRow[col] >> 24) & 0xFF; // alpha
				tempData[row][col][1] = (aRow[col] >> 16) & 0xFF; // red
				tempData[row][col][2] = (aRow[col] >> 8) & 0xFF;  // green
				tempData[row][col][3] = (aRow[col]) & 0xFF;       // blue
			}
			
		}
		return tempData;
	}
}

```

### 2.3 处理效果

#### 2.3.1 原图像 384*256

![](http://ww3.sinaimg.cn/large/ed796d65gw1exfm9muuuzj20ao074dge.jpg)

#### 2.3.2 放缩为 192*128

![](http://ww3.sinaimg.cn/large/ed796d65gw1exfma34ukpj205c03kgln.jpg)

#### 2.3.3 放缩为 96*64

![](http://ww1.sinaimg.cn/large/ed796d65gw1exfmao85brj202o01smwz.jpg)

#### 2.3.4 放缩为 48*32

![](http://ww1.sinaimg.cn/large/ed796d65gw1exfmayfs0bj201c00w741.jpg)

#### 2.3.5 放缩为 300*200

![](http://ww2.sinaimg.cn/large/ed796d65gw1exfmb9h5vsj208c05kjrq.jpg)

#### 2.3.6 放缩为 450*300

![](http://ww4.sinaimg.cn/large/ed796d65gw1exfmbny844j20ci08c3zd.jpg)

#### 2.3.7 放缩为 500*200

![](http://ww4.sinaimg.cn/large/ed796d65gw1exfmc4utssj20dw05kdgg.jpg)


## 3 图像量化

> 图像量化相对于图像放缩就简单很多了。问题关键就是对灰度级别的理解

### 3.1 灰度级别与K比特图像

K比特图像一般是跟灰度级别一一对应的。

比如1比特图像，那么就是使用1个比特的位来表示灰度值。1个比特只能表示0或者1，0代表全黑，1代表全白。再比如4比特图像，就能表示`2^4 = 16`个灰度级别的图像了，从0到15分别量化从全黑到全白的值。

但是，如果只能使用8比特图像来表示更低的格式，或者利用256个灰度级别来表示更低的灰度级别，这就是本次project要解决的问题。

### 3.2 量化策略

现在得到的图像是8比特图像，需要把原灰度级别映射到相应的从0到255之间划分的值。这个划分的gap就是需要降低到级别决定的。

比如，2 level，也就是1比特图像，那么映射到0到255区间，就是0和255。比如4 level，也就是2比特图像，那么映射到0到255区间，就是0，85，170，255。

现在的问题变成了怎样根据level的值来确定gap。

根据这个思路，可以发现，2级别，相应地在0到255之间有1个`gap = 255`；4级别，相应地在0到255之间有3个`gap = 85`；

以此类推，可以发现`gap = 255 / (level - 1)`。

然后，可以将与原灰度值最接近的值作为量化后的灰度值。

### 3.3 实例代码分析

```java

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

```

### 3.4 效果

#### 3.4.1 128 level

![](http://ww2.sinaimg.cn/large/ed796d65gw1exfqjnxxb4j20ao0743z5.jpg)

#### 3.4.2 32 level

![](http://ww3.sinaimg.cn/large/ed796d65gw1exfqjy8vhmj20ao07474z.jpg)

#### 3.4.3 8 level

![](http://ww4.sinaimg.cn/large/ed796d65gw1exfqk9amvrj20ao074q3p.jpg)

#### 3.4.4 4 level

![](http://ww3.sinaimg.cn/large/ed796d65gw1exfqkmtlztj20ao0740th.jpg)

#### 3.4.5 2 level

![](http://ww1.sinaimg.cn/large/ed796d65gw1exfqkyehnyj20ao074aak.jpg)


## 4 小程序展示

