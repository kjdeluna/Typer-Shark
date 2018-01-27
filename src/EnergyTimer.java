import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class EnergyTimer extends JPanel implements Runnable{
  private Diver diver;
  private final static int TIMER_HEIGHT = 30;
  private final static int TIMER_XPOS = 15;
  private final static int TIMER_YPOS = GameFrame.SCREEN_HEIGHT - 75;
  private final static int MAX_ENERGY_WIDTH = 450;
  public final static int PROGRESS = 50;
  public EnergyTimer(Diver diver){
    this.diver = diver;
    this.setOpaque(false);
    this.setBounds(0,0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
  }
    //
  public void countUp(){ // call this if the user typed the word
    if(this.diver.getEnergy() < EnergyTimer.PROGRESS) this.diver.incEnergy();
  }
  public void resetTimer(){
    this.diver.resetEnergy(); // will only happen if electrocute method is used
  }
  @Override //Overrides an abstract method in Runnable interface
  public void run(){
    while(this.diver.isAlive() == true){ // will only increase while diver is alive
      try{
        Thread.sleep(500);
      } catch(Exception e){}
      this.countUp(); // will count up (works like a timer)
      this.repaint();
    }
  }
  @Override //Overrides the method paintComponent from JPanel Class
  public void paintComponent(Graphics g){
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(new Color(174,198,207)); // will seem like a bottom panel
      g2d.fillRect(0, EnergyTimer.TIMER_YPOS - 10, GameFrame.SCREEN_WIDTH, EnergyTimer.TIMER_HEIGHT + 20);
      g2d.setColor(Color.BLACK);
      g2d.fillRoundRect(EnergyTimer.TIMER_XPOS, EnergyTimer.TIMER_YPOS, EnergyTimer.MAX_ENERGY_WIDTH, EnergyTimer.TIMER_HEIGHT, 10, 10); // for the actual bar
      g2d.setColor(new Color(28,107,160)); // set color
      g2d.fillRoundRect(EnergyTimer.TIMER_XPOS, EnergyTimer.TIMER_YPOS, this.diver.getEnergy() * (EnergyTimer.MAX_ENERGY_WIDTH/EnergyTimer.PROGRESS), EnergyTimer.TIMER_HEIGHT, 10, 10); //progress bar
      Font font = new Font("SANS_SERIF",Font.PLAIN, 18);
      g.setFont(font);
      g2d.setColor(Color.WHITE);
      if(this.diver.getEnergy() == EnergyTimer.PROGRESS){ // change the text in the energy bar by drawing it
        g2d.drawString("READY", EnergyTimer.TIMER_XPOS + 140, EnergyTimer.TIMER_YPOS + 20);
      }
      else g2d.drawString("CHARGING", EnergyTimer.TIMER_XPOS + 120, EnergyTimer.TIMER_YPOS + 20); // while not max
  }
}
