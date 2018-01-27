import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
class Shark extends Fish implements Runnable, Enemy{
  // shark does not have any specialization
  private final static String ALIVE_SHARK = "../images/shark_alive.png";
  public final static String ZAPPED_SHARK = "../images/shark_zapped.png";
  public final static String DEAD_SHARK = "../images/shark_dead.png";
  public final static int INITIAL_VELOCITY = 1;
  public final static int WIDTH = 200;
  public final static int XCOEFF = 70;
  public final static int YCOEFF = 45;
  public Shark(String word, int score, int xPos, int yPos, Ocean ocean){
    super(word, score, xPos, yPos, Shark.ALIVE_SHARK, ocean);
    this.velocity = Shark.INITIAL_VELOCITY;
  }
}
