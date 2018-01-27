import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

// Displays the hp of the boss
public class HPBar extends JPanel{
  private Boss boss;
  public HPBar(Boss boss){
    this.boss = boss;
    this.setOpaque(false);
    this.setBounds(0,0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
  }

  @Override
  public void paintComponent(Graphics g){
    if(this.boss.getHp() > 0){
      super.paintComponent(g);
      float fraction = 480f * ((float) this.boss.getHp() / (float) Boss.MAX_HP); // gets the fraction part then multiply to 480
      int bar = (int) fraction; // typecast
      Graphics2D g2d = (Graphics2D) g;
      g.setColor(new Color(0,0,0));
      g2d.fillRect(360, 50, 480, 40);
      g.setColor(Color.GREEN); // for the actual hp
      g2d.fillRect(360,50, bar, 40);
      g.setColor(Color.WHITE); // for the string boss
      g2d.drawString("BOSS", 600, 110);
    }
  }
}
