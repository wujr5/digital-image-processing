
import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;

public class ScaleImage {
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
	
	public BilineInterpolationScale imageScale;
	public int[] imageRGBArray;
	
	private JLabel label;
  private JFileChooser chooser;
  private static final int DEFAULT_WIDTH = 500;
  private static final int DEFAULT_HEIGHT = 500;
  private String imagePath;
  private String fileName;

	public ImageViewerFrame() throws IOException { 
    
    setTitle("ImageViewer");
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    label = new JLabel();
    add(label);

    chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));

    JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);

    addFileMenu(menubar);
    add2_2_1Menu(menubar);
    add2_2_2Menu(menubar);
    add2_2_3Menu(menubar);
    add2_2_4Menu(menubar);
    
    imagePath = "";
  }
	
  public int[] scale(BufferedImage image, Size size) {
  	int srcWidth = imageScale.image.getWidth();
  	int srcHeight = imageScale.image.getHeight();
  	
  	int[] result = imageScale.imgScale(imageRGBArray, srcWidth, srcHeight, size.width, size.height);
  	
  	return result;
  }
	
	public void saveImageAsFile(int width, int height, int[] result, String fileName) throws IOException {
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for(int i= 0 ; i < height; i++) {
		    for(int j = 0 ; j < width; j++) {
				grayImage.setRGB(j, i, result[i * width + j]);
		  }
		}
		
		File newFile = new File("./image/" + fileName);
		ImageIO.write(grayImage, "png", newFile);
	}
	
	public int[] getImageRGBArray() {
		int width = imageScale.image.getWidth();
		int height = imageScale.image.getHeight();
		
		int[] RGBArray = new int[width * height];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				RGBArray[i * width + j] = imageScale.image.getRGB(j, i);
			}
		}
		
		return RGBArray;
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
          imagePath = name;
          fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1, imagePath.lastIndexOf('.'));
          System.out.println(fileName);
          try {
						imageScale = new BilineInterpolationScale(imagePath);
						imageRGBArray = getImageRGBArray();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
          label.setIcon(new ImageIcon(name));
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
  
  public void scaleImageAndSaveTheFile(int width, int height) {
		if (imagePath != "") {
			Size size = new Size(width, height);
  		int[] result = scale(imageScale.image, size);
  		try {
  			String fileTail = "_" + width + "_" + height + ".png";
				saveImageAsFile(size.width, size.height, result, fileName + fileTail);
				label.setIcon(new ImageIcon("./image/" + fileName + fileTail));
				setTitle(width + "*" + height);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  }

  public void add2_2_1Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.1");
    menubar.add(menu);

    JMenuItem downScaleTo192_128 = new JMenuItem("Down-scale to 192*128");
    JMenuItem downScaleTo96_64 = new JMenuItem("Down-scale to 96*64");
    JMenuItem downScaleTo48_32 = new JMenuItem("Down-scale to 48*32");
    JMenuItem downScaleTo24_16 = new JMenuItem("Down-scale to 24*16");
    JMenuItem downScaleTo12_8 = new JMenuItem("Down-scale to 12*8");
    
    downScaleTo192_128.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(192, 128);
    	}
    });
    downScaleTo96_64.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(96, 64);
    	}
    });
    downScaleTo48_32.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(48, 32);
    	}
    });
    downScaleTo24_16.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(24, 16);
    	}
    });
    downScaleTo12_8.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(12, 8);
    	}
    });

    menu.add(downScaleTo192_128);
    menu.add(downScaleTo96_64);
    menu.add(downScaleTo48_32);
    menu.add(downScaleTo24_16);
    menu.add(downScaleTo12_8);
  }

  public void add2_2_2Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.2");
    menubar.add(menu);

    JMenuItem downScaleTo300_200 = new JMenuItem("Scale to 300*200");
    downScaleTo300_200.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(300, 200);
    	}
    });
    menu.add(downScaleTo300_200);
  }

  public void add2_2_3Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.3");
    menubar.add(menu);

    JMenuItem upScaleTo450_300 = new JMenuItem("Up-scale to 450*300");
    upScaleTo450_300.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(450, 300);
    	}
    });
    menu.add(upScaleTo450_300);
  }

  public void add2_2_4Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.4");
    menubar.add(menu);

    JMenuItem scaleTo500_200 = new JMenuItem("Up-scale to 500*200");
    scaleTo500_200.addActionListener(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		scaleImageAndSaveTheFile(500, 200);
    	}
    });
    menu.add(scaleTo500_200);
  }
  
}

class Size {
  Size(int w, int h) {
    width = w;
    height = h;
  }
  int width;
  int height;
}
