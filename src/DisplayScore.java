import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
class DisplayScore extends JPanel implements Runnable{
  private int xPos;
  private int yPos;
  private int score;
  private int countDown;
  public DisplayScore(int xPos, int yPos, int score){
    this.xPos = xPos;
    this.yPos = yPos;
    this.score = score;
    this.countDown = 5;
    this.setOpaque(false);
    this.setBounds(0,0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
  }
  public void decYPos(){
    this.yPos -= 10;
  }

  public void run(){
    while(countDown != 0){
      this.decYPos();
      this.repaint();
      this.countDown -= 1; // countdown is 5; decrement by 1 everytime
      try{
        Thread.sleep(200); // sleep for 200 millisec
      } catch(Exception e){}
    }
  }
  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if(countDown != 0){
      Font font = new Font("arial", Font.BOLD, 30);
      if(this.countDown >= 4) g.setColor(Color.RED); // set the color of the string (varying opacity)
      else if(this.countDown > 1) g.setColor(new Color(255,0,0, 127));
      else g.setColor(new Color(255,0,0,50)); // yellow
      g.setFont(font); // sets the font
      g2d.drawString("+" + Integer.toString(this.score), this.xPos, this.yPos);
    }
  }
}
