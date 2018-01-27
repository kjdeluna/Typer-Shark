import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class Jellyfish extends Fish implements Runnable, Enemy{
  private final static String MOVING_JELLYFISH = "../images/jellyfish.gif";
  public final static int INITIAL_VELOCITY = 4;
  public final static int LENGTH = 88;
  public Jellyfish(String word, int score, int xPos, int yPos, Ocean ocean){
    super(word,score,xPos, yPos, Jellyfish.MOVING_JELLYFISH, ocean);
    this.velocity = Jellyfish.INITIAL_VELOCITY;
    this.xCoeff = 20;
    this.yCoeff = 35;
  }
  public void run(){
    // Three cases that a fish will die: 1. reached the diver 2. electrocuted 3. user typed correctly
    while(this.isAlive == true){ // will break if the fish is alive or the fish has already reached the diver
      this.moveUp();
      this.repaint();
      try{
        Thread.sleep(100);
      } catch(Exception e){}
    }
    this.resetWord();
    GameFrame.currentWord = "";

  }

  @Override // repaint is called
  public void paintComponent(Graphics g){
    if(this.isAlive == true){
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      Font font = new Font("arial", Font.BOLD, 15);
      FontMetrics fm = g.getFontMetrics(font);
      int font_width = fm.stringWidth(GameFrame.currentWord);
      g.setFont(font);
      if(this.getWord().startsWith(GameFrame.currentWord) && GameFrame.currentWord != ""){
        g.setColor(new Color(178,34,34));
        g2d.drawString(GameFrame.currentWord, this.getXPos() + this.xCoeff, this.getYPos() + this.yCoeff);
        g.setColor(Color.BLACK);
        g2d.drawString(this.getWord().substring(GameFrame.currentWord.length()), this.getXPos() + font_width + this.xCoeff, this.getYPos() + this.yCoeff);
      }
      else{
        g.setColor(Color.BLACK);
        g2d.drawString(this.getWord(), this.getXPos() + this.xCoeff, this.getYPos() + this.yCoeff);
      }
    }
  }
}
