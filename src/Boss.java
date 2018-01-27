import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Boss extends Sprite implements Runnable{
  private final static int LEFT_BOUNDARY = 680;
  private final static int RIGHT_BOUNDARY = 720;
  private final static String BOSS_ICON = "../images/boss.png";
  private static int drc = 0; //0 for left; 1 for right
  public static boolean flag = true;
  public final static int MAX_HP = 500;
  private int hp;
  private ArrayList<Missile> missiles;
  public HashMap<String, Integer> listOfWords;
  public ArrayList<String> listOfKeys;
  private Diver diver;
  private Ocean ocean;
  public Boss(int xPos, int yPos, Diver diver, Ocean ocean){
    super(xPos,yPos, Boss.BOSS_ICON);
    this.ocean = ocean;
    this.missiles = new ArrayList<Missile>();
    this.hp = Boss.MAX_HP;
    this.diver = diver;
    this.loadWordsFile("words_g8_letter.txt"); // loads text file
  }
  public Diver getDiver(){
    return this.diver;
  }
  private void loadWordsFile(String filename){
    this.listOfWords = new HashMap<String, Integer>();
    try{
      String[] tokens;
      String line;
      BufferedReader reader = new BufferedReader(new FileReader(filename));

      while((line = reader.readLine()) != null){
        tokens = line.split(" ");
        listOfWords.put(tokens[0], Integer.parseInt(tokens[1]));
      }
      this.listOfKeys = new ArrayList<String>(this.listOfWords.keySet());
    } catch(Exception e){ e.getMessage(); }
  }

  public void addMissiles(){
    Random r = new Random();
    int rand = r.nextInt(2) + 3;
    for(int i = 0; i < rand; i++){
      int index = r.nextInt(this.listOfKeys.size());
      String word = this.listOfKeys.get(index); // gets a random word
      int score = this.listOfWords.get(word); // gets the score associated
      this.missiles.add(new Missile(word.toUpperCase(), score, Boss.LEFT_BOUNDARY - Missile.WIDTH, 65 * i + 120, this)); // sets the x position to the left boundary of the boss
    }
  }

  public ArrayList<Missile> getMissiles(){
    return this.missiles;
  }

  public synchronized int getHp(){
    return this.hp;
  }

  public void setHp(int damage){
    this.hp -= damage; // damages the boss
  }

  public void autoFlee(){ // movement of the boss (left or right)
    if(this.drc == 0) this.decXPos(3);
    else this.incXPos(3);
  }

  public void move(){
    if(this.getXPos() <= Boss.LEFT_BOUNDARY){
      this.incXPos(3);
      this.drc = 1;
    }
    else if(this.getXPos() >= Boss.RIGHT_BOUNDARY){
      this.decXPos(3);
      this.drc = 0;
    }
    else this.autoFlee();
  }

  public void run(){
    while(this.hp > 0 && this.diver.isAlive() == true){ // while hp of the boss is 0 and diver is still alive
      this.move();
      if(this.missiles.size() == 0 && this.ocean.getListOfEnemies().size() == 0 && this.getXPos() >= Boss.LEFT_BOUNDARY && this.getXPos() <= Boss.RIGHT_BOUNDARY)this.addMissiles();
      this.repaint();                                         // if the missiles are already typed or passed leftmost part, add missile
      try{
        Thread.sleep(100);
      } catch(Exception e){}
    }
    try{
      Thread.sleep(1000);
    } catch(Exception e){}
    if(this.hp <= 0) Boss.flag = false;
    this.repaint();
  }

  @Override
  public void paintComponent(Graphics g){
    if(this.hp > 0){
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage(this.getImage(), this.getXPos(), this.getYPos(), null);
      Toolkit.getDefaultToolkit().sync(); // makes animation smooth
    }
  }
}
