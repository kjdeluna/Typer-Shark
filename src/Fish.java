import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class Fish extends Sprite implements Runnable, Enemy{
  protected String word;
  protected int score;
  protected boolean isAlive;
  protected int velocity;
  protected int width;
  protected int xCoeff; // where the words are positioned within the fish
  protected int yCoeff;
  protected Ocean ocean;
  private final static String ALIVE_FISH = "../images/fish_alive.png";
  private final static String ZAPPED_FISH = "../images/fish_zapped.png";
  private final static String DEAD_FISH = "../images/fish_dead.png";
  private static int INITIAL_VELOCITY = 3;
  public final static int WIDTH = 107;
  private final static int XCOEFF = 35;
  private final static int YCOEFF = 75;
  //Overloaded constructor
  public Fish(String letter, int score, int xPos, int yPos, String filename, Ocean ocean){
    super(xPos, yPos, filename); // Shark Constructor
    this.word = letter.toUpperCase();
    this.score = score;
    this.isAlive = true;
    this.velocity = Shark.INITIAL_VELOCITY;
    this.width = Shark.WIDTH;
    this.xCoeff = Shark.XCOEFF;
    this.yCoeff = Shark.YCOEFF;
    this.ocean = ocean;
  }
  public Fish(String letter, int score, int xPos, int yPos, Ocean ocean){
    super(xPos, yPos, Fish.ALIVE_FISH); // Actual fish constructor
    this.word = letter;
    this.score = score;
    this.isAlive = true;
    this.velocity = Fish.INITIAL_VELOCITY;
    this.width = Fish.WIDTH;
    this.xCoeff = Fish.XCOEFF;
    this.yCoeff = Fish.YCOEFF;
    this.ocean = ocean;
  }
  protected String getWord(){
    return this.word;
  }
  protected boolean getIsAlive(){
    return this.isAlive;
  }
  protected void resetWord(){
    this.word = "";
  }
  protected int getScore(){
    return this.score;
  }
  protected void setIsAlive(boolean status){
    this.isAlive = status;
  }
  protected void moveLeft(int velocity){
    super.decXPos(velocity);
  }
  protected void moveUp(){
    super.decYPos(4);
  }
  @Override
  public void run(){
    // Three cases that a fish will die: 1. reached the diver 2. electrocuted 3. user typed correctly
    while(this.isAlive == true){ // will break if the fish is alive or the fish has already reached the diver
      this.moveLeft(this.velocity);
      this.repaint();
      try{
        Thread.sleep(100);
      } catch(Exception e){}
    }
    this.resetWord();
    GameFrame.currentWord = "";
    if(this.ocean.getDiver().isAlive() == true && this.isAlive == false){
      if(super.getXPos() + this.width > 0){
        if(this instanceof Shark) super.loadImage(Shark.ZAPPED_SHARK);
        else if(this instanceof Fish) super.loadImage(Fish.ZAPPED_FISH);
        this.repaint();
        try{
          Thread.sleep(200); // take some time to display image
        } catch(Exception e){}
        if(this instanceof Shark) super.loadImage(Shark.DEAD_SHARK);
        else if(this instanceof Fish) super.loadImage(Fish.DEAD_FISH);
        this.repaint();
        while(super.getYPos() > 0){ // going up after killed
          try{
            Thread.sleep(100); // faster to go up
          } catch(Exception e){}
            this.moveUp();
            this.repaint();
        }
      }
    }
  }
  @Override // repaint is called
  public void paintComponent(Graphics g){
    if((this.isAlive == true || super.getYPos() > 0) && this.getXPos() + this.width > 0){ // if its xpos and ypos is still at the screen, paint
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      Font font = new Font("arial", Font.BOLD, 15);
      FontMetrics fm = g.getFontMetrics(font);
      int font_width = fm.stringWidth(GameFrame.currentWord);
      g.setFont(font); // sets font
      if(this.getWord().startsWith(GameFrame.currentWord) && GameFrame.currentWord != ""){ // reset the word typed by the user (accepted)
        g.setColor(new Color(178,34,34));
        g2d.drawString(GameFrame.currentWord, this.getXPos() + this.xCoeff, this.getYPos() + this.yCoeff); // common letters
        g.setColor(Color.WHITE);
        g2d.drawString(this.getWord().substring(GameFrame.currentWord.length()), this.getXPos() + font_width + this.xCoeff, this.getYPos() + this.yCoeff); // gets the remaining of the word
      }
      else{
        g.setColor(Color.WHITE); // no common letters
        g2d.drawString(this.getWord(), this.getXPos() + this.xCoeff, this.getYPos() + this.yCoeff);
      }
    }
  }
}


/* Notes: Create final variables to the Shark and Fish, so that words will center
*/
