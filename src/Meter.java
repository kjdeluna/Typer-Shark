import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
// Meter at the leftmost side of the game
public class Meter extends JPanel implements Runnable{
  private int xPos;
  private int yPos;
  private Diver diver;
  public static int GAP = 5; // initial gap between the lines is 5; also corresponds to depth; gap is the value of the first line
  private final static int pixelYPos = 80;
  public Meter(Diver diver){
    this.xPos = 0;
    this.yPos = Meter.pixelYPos;
    this.diver = diver;
    this.setOpaque(false);
    this.setBounds(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
  }

  public void run(){
    while(this.diver.getDepth() != Diver.TOTAL_DEPTH && this.diver.isAlive() == true){
      this.repaint();
      if(this.diver.hasDescended() == true) this.yPos -= 3; // meter will move only if the diver has descended
      if(this.yPos <= 0){
        this.yPos = Meter.pixelYPos;
        Meter.GAP += 5; // increments the gap by 5m everytime the first line goes up
      }
      try{
        Thread.sleep(200);
      } catch(Exception e){}
    }
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics g2d = (Graphics2D) g;
    g.setColor(Color.WHITE);

    for(int i = 0; i < 9; i++){
      g2d.drawLine(this.xPos, this.yPos + (i * Meter.pixelYPos), this.xPos + 5, this.yPos + (i * Meter.pixelYPos));
      if((5 *i + Meter.GAP) % 20 == 0) g2d.drawString(Integer.toString(5 * i + Meter.GAP), this.xPos + 10, this.yPos + (i * Meter.pixelYPos) + 5);
    }
  }
}
