
import java.awt.EventQueue;
import java.awt.event.*;
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
	public int[] imageRGBArray;
	private JLabel label;
  private JFileChooser chooser;
  
  private static final int DEFAULT_WIDTH = 500;
  private static final int DEFAULT_HEIGHT = 500;
  
  private String imagePath;
  private String fileName;

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
	}
	
	public void setMenuBar() {
		JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);

    addFileMenu(menubar);
    add2_2Menu(menubar);
    add2_2_3Menu(menubar);
    add2_2_4Menu(menubar);
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
          fileName = name.substring(name.lastIndexOf('/') + 1);
          System.out.println(fileName);

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

  public void add2_2Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2");
    menubar.add(menu);

    JMenuItem downScaleTo300_200 = new JMenuItem("Histogram Equalize this image");
    menu.add(downScaleTo300_200);
  }

  public void add2_2_3Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.3");
    menubar.add(menu);

    JMenuItem upScaleTo450_300 = new JMenuItem("Up-scale to 450*300");
    
    menu.add(upScaleTo450_300);
  }

  public void add2_2_4Menu(JMenuBar menubar) {
    JMenu menu = new JMenu("2.2.4");
    menubar.add(menu);

    JMenuItem scaleTo500_200 = new JMenuItem("Up-scale to 500*200");
    
    menu.add(scaleTo500_200);
  }
  
  public void add2_3Menu(JMenuBar menubar) {
  	JMenu menu = new JMenu("2.3");
  	menubar.add(menu);
  	
  	JMenuItem quantizeTo128Level = new JMenuItem("quantize to 128 level");
  	JMenuItem quantizeTo32Level = new JMenuItem("quantize to 32 level");
  	JMenuItem quantizeTo8Level = new JMenuItem("quantize to 8 level");
  	JMenuItem quantizeTo4Level = new JMenuItem("quantize to 4 level");
  	JMenuItem quantizeTo2Level = new JMenuItem("quantize to 2 level");
  	
  	menu.add(quantizeTo128Level);
  	menu.add(quantizeTo32Level);
  	menu.add(quantizeTo8Level);
  	menu.add(quantizeTo4Level);
  	menu.add(quantizeTo2Level);
  	
  	
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
