import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
// contains the top panel in the game
public class TopPanel extends JPanel{
  Diver diver;
  int level;
  public TopPanel(Diver diver, int level){
    this.diver = diver;
    this.level = level;
    this.setBounds(0,0, GameFrame.SCREEN_WIDTH, 30);
    this.setBackground(new Color(0,0,0,127));
    this.setOpaque(true);
  }

@Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(Color.WHITE);
    Font font = new Font("rockwell", Font.PLAIN, 17);
    g.setFont(font);
    g2d.drawString("LEVEL: " + this.level, GameFrame.SCREEN_WIDTH/8, 20); // displays th elevel, points and lives
    g2d.drawString(this.diver.getScore() + " Points", GameFrame.SCREEN_WIDTH/2, 20);
    g2d.drawString("Lives left: " + this.diver.getLives(), GameFrame.SCREEN_WIDTH - GameFrame.SCREEN_WIDTH/6, 20);
  }
}
