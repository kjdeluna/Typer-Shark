import javax.swing.JOptionPane;
import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;
class Diver extends Sprite implements Runnable{
  public final static int FINAL_YPOS = 300;
  private static String name;
  private int depth;
  private boolean isAlive;
  private int energy;
  private int score;
  private boolean descended;
  private int lives;
  public final static int TOTAL_DEPTH = 100;
  private final static String DIVER_IMAGE = "../images/diver.gif";

  public Diver(String name, int xPos, int yPos){
    super(xPos, yPos, Diver.DIVER_IMAGE);
    this.name = name;
    this.lives = 3;
    this.depth = 0;
    this.isAlive = true;
    this.energy = 0;
    this.descended = false;
  }

  public void decLives(){
    if(this.lives - 1 < 0) this.lives = 0;
    else this.lives--;
  }
  public synchronized int getDepth(){
    return this.depth;
  }
  public int getEnergy(){
    return this.energy;
  }
  public int getLives(){
    return this.lives;
  }
  private void move(){
      this.depth = Meter.GAP + (5 * 4);
      if(this.getYPos() < Diver.FINAL_YPOS) this.incYPos(7);
      else this.descended = true;
  }
  public boolean hasDescended(){
    return this.descended;
  }

  public boolean isAlive(){
    return this.isAlive;
  }
  public int getScore(){
    return this.score;
  }
  public void incScore(int score){
    this.score += score;
  }

  public void electrocute(Ocean ocean, ArrayList<Enemy> listOfEnemies, EnergyTimer energyTimer){
    DisplayScore appear; // displays the score when killed
    Thread appearThread;
    Fish fish;
    if(this.getEnergy() == EnergyTimer.PROGRESS){ // can only be used if the bar is max
      Iterator<Enemy> iter = listOfEnemies.iterator();
      while(iter.hasNext()){
        Enemy temp = iter.next();
        if(temp instanceof Fish){ // missiles are immune to electrocution
          fish = (Fish) temp;
          appear = new DisplayScore(fish.getXPos(), fish.getYPos(), fish.getScore());
          appearThread = new Thread(appear);
          ocean.add(appear);
          appearThread.start();
          this.incScore(fish.getScore());
          ((Fish)(temp)).setIsAlive(false);
          energyTimer.countUp(); // since everytime an enemy is killed, the progress bar increases
          iter.remove(); // remove the creature killed
        }
      }
      this.resetEnergy(); // if electrocute is used, reset the energy bar
    }
  }

  public void incEnergy(){
    this.energy += 1;
  }

  public void resetEnergy(){
    this.energy = 0;
  }

  @Override
  public void run(){
    while(this.isAlive == true){
      if(this.descended == true && this.depth < Diver.TOTAL_DEPTH){
        this.score++;
      }
      this.move();
      this.repaint();
      if(this.lives <= 0) this.isAlive = false;
      try{
        Thread.sleep(100);
      } catch(Exception e){}
    }

    if(Boss.flag == false || this.isAlive == false){
      this.name =  JOptionPane.showInputDialog("Enter name");
      try{
        FileWriter out = new FileWriter("scores.txt");
        out.append(this.name + " " + this.score + "\n");
        out.close();
      }catch(Exception e){}
    }
  }
}
