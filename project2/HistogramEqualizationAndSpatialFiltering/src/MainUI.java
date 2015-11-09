
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class MainUI {
  public static void main(String[] args) throws IOException {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame frame = null;
        try {
        	frame = new ImageViewerFrame();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      }
    });
  }
}

class ImageViewerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
  private JFileChooser chooser;
  
  private static final int DEFAULT_WIDTH = 500;
  private static final int DEFAULT_HEIGHT = 500;
  
  private String imagePath;
  private String fileName;
  public BufferedImage image;

	public ImageViewerFrame() throws IOException { 
		initialFrame();
		setMenuBar();
  }
	
	public void initialFrame() {
		setTitle("Histogram Equalization and Spatial Filtering");
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    label = new JLabel();
    add(label);

    chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));
    
    imagePath = "";
    fileName = "";
    image = null;
	}
	
	public void setMenuBar() {
		JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);

    addFileMenu(menubar);
    add2_2Menu(menubar);
    add2_3Menu(menubar);
	}
  
  public void addFileMenu(JMenuBar menubar) {
    JMenu menu = new JMenu("File");
    menubar.add(menu);

    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem exitItem = new JMenuItem("Close");

    openItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
          // TODO Auto-generated method stub
        int result = chooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
          String name = chooser.getSelectedFile().getPath();
          
          imagePath = name.substring(0, name.lastIndexOf('/') + 1);
          fileName = name.substring(name.lastIndexOf('/') + 1, name.lastIndexOf('.'));
          
          File f = new File(name);
      		try {
						image = ImageIO.read(f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

          label.setIcon(new ImageIcon(name));
          setSize(image.getWidth(), image.getHeight() + 100);
          setTitle(fileName);
        }
      }
    });

    exitItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        System.exit(0);
      }
    });
    
    menu.add(openItem);
    menu.add(exitItem);
  }

  public void add2_2Menu(JMenuBar menubar) {

    JMenu menu = new JMenu("2.2 直方图均衡化");
    menubar.add(menu);

    JMenuItem histogramEqualization = new JMenuItem("均衡化");
    JMenuItem createSourceHistogram = new JMenuItem("原图像直方图");
    JMenuItem createDistHistogram = new JMenuItem("均衡化图像直方图");
    
    histogramEqualization.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					HistogramEqualization he = new HistogramEqualization();
					BufferedImage img = he.equalize_hist(image);
					File output = new File(imagePath + fileName + "_HE.png");
					try {
						ImageIO.write(img, "png", output);
						label.setIcon(new ImageIcon(imagePath + fileName + "_HE.png"));
						setTitle(fileName + "_HE.png");
						setSize(img.getWidth(), img.getHeight() + 100);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    
    createSourceHistogram.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					createHistogram(image, "src");
				}
			}
		});
    
    createDistHistogram.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File(imagePath + fileName + "_HE.png");
    		try {
					BufferedImage distImage = ImageIO.read(f);
					createHistogram(distImage, "dist");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("图像不存在");
				}
			}
		});
    
    menu.add(histogramEqualization);
    menu.add(createSourceHistogram);
    menu.add(createDistHistogram);
  }
  
  public void add2_3Menu(JMenuBar menubar) {
  	JMenu menu = new JMenu("2.3 空间滤波");
    menubar.add(menu);

    JMenuItem smooth3_3 = new JMenuItem("Smooth 3*3");
    JMenuItem smooth7_7 = new JMenuItem("Smooth 7*7");
    JMenuItem smooth11_11 = new JMenuItem("Smooth 11*11");
    
    JMenuItem sharpen3_3 = new JMenuItem("Sharpen 3*3");
    
    JMenuItem highBoostfilter = new JMenuItem("High Boostfilter");
    
    smooth3_3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					performSmooth(3);
				}
			}
		});
    
    smooth7_7.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					performSmooth(7);
				}
			}
		});
    
    smooth11_11.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (image != null) {
					performSmooth(11);
				}
			}
		});
    
    sharpen3_3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					SpatialFiltering sf = new SpatialFiltering();
					int[][] filter = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
					
					BufferedImage img = sf.filterLaplacian(image, filter);
					String name = fileName + "_laplacian_sharpen.png";
					File output = new File(imagePath + name);
					
					try {
						ImageIO.write(img, "png", output);
						label.setIcon(new ImageIcon(imagePath + name));
						setTitle(name);
						setSize(img.getWidth(), img.getHeight() + 100);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    
    highBoostfilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (image != null) {
					SpatialFiltering sf = new SpatialFiltering();
					
					BufferedImage img = sf.highBoostfiltering(image);
					String name = fileName + "_high_boost_filter.png";
					File output = new File(imagePath + name);
					
					try {
						ImageIO.write(img, "png", output);
						label.setIcon(new ImageIcon(imagePath + name));
						setTitle(name);
						setSize(img.getWidth(), img.getHeight() + 100);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    
    menu.add(smooth3_3);
    menu.add(smooth7_7);
    menu.add(smooth11_11);
    menu.add(sharpen3_3);
    menu.add(highBoostfilter);
  }
  
  public void performSmooth(int filterWidth) {
		SpatialFiltering sf = new SpatialFiltering();
		int[][] filter = new int[filterWidth][filterWidth];
		
		for (int i = 0; i < filterWidth; i++)
			for (int j = 0; j < filterWidth; j++)
				filter[i][j] = 1;
		
		BufferedImage img = sf.filter2d(image, filter);
		String name = fileName + "_" + filterWidth + "X" + filterWidth +"_smooth.png";
		File output = new File(imagePath + name);
		
		try {
			ImageIO.write(img, "png", output);
			label.setIcon(new ImageIcon(imagePath + name));
			setTitle(name);
			setSize(img.getWidth(), img.getHeight() + 100);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  }
  
  public void createHistogram(BufferedImage bufferedImage, String type) {
    PlaneHistogram planeHistogram = new PlaneHistogram();
    HistogramEqualization histEqua = new HistogramEqualization();
    JavaRGBImage javaRGBImage = new JavaRGBImage();
    
    int width = image.getWidth();
    int height = image.getHeight();
    
    int[] grayArray = javaRGBImage.getImageGrayArray(bufferedImage);
    
    int[] histogramArray = histEqua.getHistogramArray(width, height, grayArray);
    
    String[] labels = new String[256];
    for (int i = 0; i < 256; i++) labels[i] = i + "";
    
    Color[] colors = new Color[256];
    for (int i = 0; i < 256; i++) colors[i] = Color.BLACK;

    BufferedImage image = planeHistogram.paintPlaneHistogram(
    		"直方图", histogramArray, labels, colors);
    
    File output = new File(imagePath + fileName + "_" + type + "_histogram.jpg");
    try {
        ImageIO.write(image, "jpg", output);
        label.setIcon(new ImageIcon(imagePath + fileName + "_" + type + "_histogram.jpg"));
        setSize(image.getWidth(), image.getHeight() + 100);
        setTitle(fileName + "_" + type + "_histogram.jpg");
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
