import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Missile extends Sprite implements Runnable, Enemy{
  private final static int DAMAGE = 40;
  private final static int VELOCITY = 15;
  private final static String MISSILE_ICON = "../images/missile1.png";
  private final static String MISSILE_REVERSE = "../images/missile2.png"; // reverse icon if the word is typed
  public final static int WIDTH = 179;

  private int damage;
  private int score;
  private String word;
  private boolean isTyped; // if it is successfully typed by the user
  private boolean hit; // if it already hit the boss
  private Boss boss;

  public Missile(String word, int score, int xPos, int yPos, Boss boss){
    super(xPos, yPos, Missile.MISSILE_ICON);
    this.damage = Missile.DAMAGE;
    this.word = word;
    this.score = score;
    this.boss = boss;
    this.isTyped = false;
    this.hit = false;
  }

  public String getWord(){
    return this.word;
  }

  public int getScore(){
    return this.score;
  }

  public boolean getIsTyped(){
    return this.isTyped;
  }

  public void setIsTyped(boolean status){
    this.isTyped = status;
  }
  private void hitBoss(Boss boss){
    if(boss.getHp() - this.damage <= 0) boss.setHp(boss.getHp());
    else boss.setHp(this.damage);
    this.hit = true;
  }

  @Override
  public void run(){
    while(this.boss.getDiver().isAlive() == true && this.isTyped == false && this.getXPos() + Missile.WIDTH > 0){ // if still in the screen, not yet typed and diver is still alive
      this.decXPos(1);
      this.repaint();
      try{
        Thread.sleep(300);
      } catch(Exception e){}
    }
    if(this.isTyped == true && this.getXPos() + Missile.WIDTH > 0){
      this.loadImage(Missile.MISSILE_REVERSE);
      this.word = ""; // resets the word, so no word will be drawn using drawString once in reverse direction
      while(this.hit != true){
        this.incXPos(1);
        if(this.getXPos() + Missile.WIDTH > this.boss.getXPos()){
          this.hitBoss(boss); // if it becomes greater than the position of the boss, boss is hit
        }
        this.repaint();
        try{
          Thread.sleep(300);
        } catch(Exception e){}
      }
    }
  }

  public void paintComponent(Graphics g){
    if(this.getXPos() + Missile.WIDTH > 0 && this.hit == false && this.boss.getHp() != 0){
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      Font font = new Font("arial", Font.BOLD, 15);
      FontMetrics fm = g.getFontMetrics(font);
      int font_width = fm.stringWidth(GameFrame.currentWord);
      g.setFont(font);
      if(this.getWord().startsWith(GameFrame.currentWord) && GameFrame.currentWord != ""){
        g.setColor(Color.GREEN);
        g2d.drawString(GameFrame.currentWord, this.getXPos() + 50, this.getYPos() + 30);
        g.setColor(Color.BLACK);
        g2d.drawString(this.getWord().substring(GameFrame.currentWord.length()), this.getXPos() + font_width + 50, this.getYPos() + 30);
      }
      else{
        g.setColor(Color.BLACK);
        g2d.drawString(this.getWord(), this.getXPos() + 50, this.getYPos() + 30);
      }
    }
  }
}
