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

除去其他相关属性，比如宽度，高度等属性，`BufferedImage`类型存储的最终要的数据是RGBA的信息。就是红色值、绿色值、蓝色值、alpha通道值的信息。

考虑以下的例子：

```
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

灰度值其实也是用RGB来表示的，唯一的区别就是，灰度值的RGB值`R = G = B`，三值相等，范围都在0到255之间。其中0表示全黑，255表示全白。Alpha值可以不予考虑。

因此，在Java中

[百度百科-灰度值]: http://baike.baidu.com/link?url=2n0g8QBNTobQDz_Q_4em04EQkNei25eFGUZ4sdWWu0o7qKcpqX8rgGx2Yr3byq5GDolru_fTBPZySaoKqBiDTa


## 2 图像放缩

### 2.1 二线性内插法

### 2.2 实例代码分析

## 3 图像量化

### 3.1 灰度级别与K比特图像

### 3.2 量化策略

## 4 小程序展示