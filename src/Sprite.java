import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

public abstract class Sprite extends JPanel{ // abstract class no instantiation
  private Image img;
  private int xPos;
  private int yPos;

  public Sprite(int xPos, int yPos, String filename){
    this.setOpaque(false);
    this.setSize(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
    this.xPos = xPos;
    this.yPos = yPos;
    this.loadImage(filename);
  }

  protected void loadImage(String filename){
    try{ // load image
      this.img = Toolkit.getDefaultToolkit().getImage(filename);
    } catch(Exception e){}
  }

  public Image getImage(){
    return this.img;
  }

  public void incYPos(int depth){
    this.yPos += depth;
  }

  public void decYPos(int depth){
    this.yPos -= depth;
  }

  public void incXPos(int width){
    this.xPos += width;
  }

  public void decXPos(int width){
    this.xPos -= width;
  }

  public synchronized int getYPos(){
    return this.yPos;
  }

  public synchronized int getXPos(){
    return this.xPos;
  }
  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.getImage(), this.getXPos(), this.getYPos(), null); // draw the image (jpanels)
    Toolkit.getDefaultToolkit().sync();
    }
}
