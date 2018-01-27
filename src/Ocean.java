import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Iterator;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JButton;
public class Ocean extends JPanel implements Runnable{
  public static TopPanel north;
  private Image img;
  private final static String BACKGROUND = "../images/background.png";
  private Diver diver;
  private Thread diverThread;
  private EnergyTimer energyTimer;
  private Thread energyTimerThread;
  private Meter meter;
  private Thread meterThread;
  private ArrayList<Enemy> enemies;
  private ArrayList<String> listOfKeys;
  private HashMap<String, Integer> listOfWords;
  private ArrayList<String> listOfKeys_3Letters;
  private HashMap<String, Integer> listOfWords_3Letters;
  private Boss boss;
  private JButton back;
  public Ocean(JButton back){
    this.setBackground(Color.BLACK);
    this.setLayout(null);
    this.loadImage(Ocean.BACKGROUND);
    this.loadWordsFile("words.txt");
    this.enemies = new ArrayList<Enemy>();
    this.back = back;
    this.add(this.back);
  }
  public void resetOcean(){
    this.meter = null;
    this.boss = null;
    this.diver = null;
    this.energyTimer = null;
    Ocean.north = null;
    this.getListOfEnemies().clear();
  }
  public synchronized ArrayList<Enemy> getListOfEnemies(){
    return this.enemies;
  }

  public EnergyTimer getEnergyTimer(){
    return this.energyTimer;
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
      this.listOfKeys = new ArrayList<String>(listOfWords.keySet());
    } catch(Exception e){ e.getMessage(); }

    this.listOfWords_3Letters = new HashMap<String, Integer>();
    try{
      String[] tokens;
      String line;
      BufferedReader reader = new BufferedReader(new FileReader("words_3_letter.txt"));

      while((line = reader.readLine()) != null){
        tokens = line.split(" ");
        listOfWords_3Letters.put(tokens[0], Integer.parseInt(tokens[1]));
      }
      this.listOfKeys_3Letters = new ArrayList<String>(listOfWords_3Letters.keySet());
    } catch(Exception e){ e.getMessage(); }
  }

  public synchronized void checkXPos(){ // checks the xPos of all the enemies
    Fish fish;
    Missile missile;
    Jellyfish jellyfish;
    Shark shark;
    ArrayList<Enemy> toRemove = new ArrayList<Enemy>();
    for(Enemy enemy : this.getListOfEnemies()){
      if(enemy instanceof Jellyfish){
        jellyfish = (Jellyfish) enemy;
        if(jellyfish.getYPos() + Jellyfish.LENGTH <= 0){
          jellyfish.setIsAlive(false); // fish is already not alive
          toRemove.add(enemy); // if it is at the top of the frame, add it to the toRemove arraylist (this is to avoid concurrent modification exception)
          if(jellyfish.getWord().startsWith(GameFrame.currentWord)) GameFrame.currentWord = "";
        }
      }
      else if(enemy instanceof Shark){
        shark = (Shark) enemy;
        if(shark.getXPos() + Shark.WIDTH <= 0){
          shark.setIsAlive(false);
          this.diver.decLives();
          toRemove.add(enemy);
          if(shark.getWord().startsWith(GameFrame.currentWord)) GameFrame.currentWord = "";
        }
      }
      else if(enemy instanceof Fish){
        fish = (Fish) enemy;
        if(fish.getXPos() + Fish.WIDTH <= 0){
          fish.setIsAlive(false);
          this.diver.decLives();
          toRemove.add(enemy);
          if(fish.getWord().startsWith(GameFrame.currentWord)) GameFrame.currentWord = "";
        }
      }
      else if(enemy instanceof Missile){
        missile = (Missile) enemy;
        if(missile.getXPos() + Missile.WIDTH <= 0){
          missile.setIsTyped(true);
          this.diver.decLives(); // decrement the life of the diver once it passed through the diver
          toRemove.add(enemy);
          if(missile.getWord().startsWith(GameFrame.currentWord)) GameFrame.currentWord = "";
        }
        else if(missile.getXPos() + Missile.WIDTH >= this.boss.getXPos() && missile.getIsTyped() == true){
          toRemove.add(enemy);
        }
      }
    }
    Ocean.north.repaint(); // make sure to update
    this.getListOfEnemies().removeAll(toRemove); // remove all the enemies that passed through the borders
  }

  public synchronized void checkWordsTyped(char e){ // GameFrame.currentWord is a shared resource
    Shark shark;
    Fish fish;
    Missile missile;
    DisplayScore appear;
    Thread appearThread;
    String keyPress = Character.toString(e).toUpperCase();
    for(Iterator<Enemy> iter = this.getListOfEnemies().iterator(); iter.hasNext();){
      Enemy antagonist = iter.next();
      if(antagonist instanceof Shark){
        shark = (Shark) antagonist;
        if(shark.getWord().startsWith(GameFrame.currentWord + keyPress)){ // if there is a word that matches the current word correctly typed by the user
          GameFrame.currentWord += keyPress; // accept the key press
          shark.incXPos(10); // push it rightwards
          if(shark.getWord().equals(GameFrame.currentWord)){ // if already equals
            this.diver.incScore(shark.getScore()); // score will be incremented
            shark.setIsAlive(false); // shark will die
            GameFrame.currentWord = ""; // reset the word
            iter.remove(); // remove it to the arraylist
            this.energyTimer.countUp();
            appear = new DisplayScore(shark.getXPos(), shark.getYPos(), shark.getScore()); // the score will appear
            appearThread = new Thread(appear);
            this.add(appear);
            appearThread.start();
          }
        }
      }
      else if(antagonist instanceof Fish){
        fish = (Fish) antagonist;
        if(fish.getWord().startsWith(GameFrame.currentWord + keyPress)){
          GameFrame.currentWord += keyPress;
          if(fish.getWord().equals(GameFrame.currentWord)){
            this.diver.incScore(fish.getScore());
            fish.setIsAlive(false);
            iter.remove();
            GameFrame.currentWord = "";
            this.energyTimer.countUp();
            appear = new DisplayScore(fish.getXPos(), fish.getYPos(), fish.getScore());
            appearThread = new Thread(appear);
            this.add(appear);
            appearThread.start();
          }
        }
      }
      else if(antagonist instanceof Missile){
        missile = (Missile) antagonist;
        if(missile.getWord().startsWith(GameFrame.currentWord + keyPress)){
          GameFrame.currentWord += keyPress;
          if(missile.getWord().equals(GameFrame.currentWord)){
            this.diver.incScore(missile.getScore());
            missile.setIsTyped(true);
            iter.remove();
            GameFrame.currentWord = "";
            this.energyTimer.countUp();
            appear = new DisplayScore(missile.getXPos(), missile.getYPos(), missile.getScore());
            appearThread = new Thread(appear);
            this.add(appear);
            appearThread.start();
          }
        }
      }
    }
  }

  private void spawnSharks(){
    Random r = new Random();
    int numOfSharks = r.nextInt(2) + 3; // maximum of 4 can be spawned at a time
    for(int i = 0; i < numOfSharks; i++){
      String randomWord = listOfKeys.get(r.nextInt(listOfKeys.size()));
      this.getListOfEnemies().add((Enemy) new Shark(randomWord, this.listOfWords.get(randomWord), GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT - (100 * (i + 2)), this));
    }
  }

  private void spawnFishes(){
    Random r = new Random();
    int numOfFishes = r.nextInt(3) + 4;
    for(int i = 0; i < numOfFishes; i++){
      int letter = r.nextInt(26) + 65;
      this.getListOfEnemies().add((Enemy) new Fish(Character.toString((char) letter), letter - 64, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT - (100 * (i + 2)), this));
    }
  }

  private void spawnJellyfish(){
    Random r = new Random();
    int numOfJellyfish = r.nextInt(5) + 3;
    for(int i = 0; i < numOfJellyfish; i++){
      String rand = listOfKeys_3Letters.get(r.nextInt(listOfKeys_3Letters.size()));
      this.getListOfEnemies().add((Enemy) new Jellyfish(rand, this.listOfWords_3Letters.get(rand), 100 * i + 360, GameFrame.SCREEN_HEIGHT, this));
    }
  }

  private void spawnMissiles(){
    Random r = new Random();
    int noOfMissiles = this.boss.getMissiles().size(); // the actual missiles are created in the boss
    for(int i = 0; i < noOfMissiles; i++){
      Missile m = this.boss.getMissiles().get(i);
      this.getListOfEnemies().add((Enemy)m);
    }
    this.boss.getMissiles().clear();
  }

  public Diver getDiver(){
    return this.diver;
  }

  private void loadImage(String filename){
    try{
      this.img = Toolkit.getDefaultToolkit().getImage(filename);
    } catch(Exception e){}
  }

  @Override
  public void run(){
    Random r = new Random();
    Shark shark;
    Fish fish;
    Jellyfish jellyfish;
    Thread sharkThread;
    Thread fishThread;
    Thread jellyFishThread;
    int rand;
    this.diver = new Diver("diver", 20, 0);
    this.diverThread = new Thread(diver);
    this.add(diver);

    /* Energy Timer */
    this.energyTimer = new EnergyTimer(diver);
    this.energyTimerThread = new Thread(energyTimer);
    this.add(energyTimer);
    Ocean.north = new TopPanel(diver,1);
    this.add(north);

    /* Meter */
    this.meter = new Meter(diver);
    this.meterThread = new Thread(meter);
    this.add(meter);

    meterThread.start();
    diverThread.start();
    this.energyTimerThread.start();

    int flag = 0;
    int flag2 = 0;
    while(this.diver.getDepth() != Diver.TOTAL_DEPTH && this.diver.isAlive() == true){
      this.checkXPos();
      rand = r.nextInt(3); // randomize spawn
      if(this.diver.hasDescended() == true && this.enemies.size() == 0){
        switch(rand){
          case 0: this.spawnFishes(); break;
          case 1: this.spawnSharks(); break;
          case 2: if(flag2 == 0){
            this.spawnJellyfish(); // jellyfish is only a bonus, appear only once
            flag2 = 1;
          }
          break;
        }
      }
      try{
        Thread.sleep(500);
      } catch(Exception e){}

    if(this.diver.isAlive() == true){
      for(Iterator<Enemy> iter = this.getListOfEnemies().iterator(); iter.hasNext();){ // then iterate throughout the arraylist
        Enemy temp = iter.next();
        if(temp instanceof Shark){
          shark = (Shark) temp;
          sharkThread = new Thread(shark);
          this.add(shark);
          sharkThread.start();
        }
        else if(temp instanceof Jellyfish){
          jellyfish = (Jellyfish) temp;
          jellyFishThread = new Thread(jellyfish);
          this.add(jellyfish);
          jellyFishThread.start();
        }
        else if(temp instanceof Fish){
          fish = (Fish) temp;
          fishThread = new Thread(fish);
          this.add(fish);
          fishThread.start();
        }
      }
    }
      try{
        Thread.sleep(500);
      } catch(Exception e){}
    }
    while(flag == 0){ // to ensure that before the boss come out, the ocean should be empty of enemies
      this.checkXPos();
      if(this.diver.getDepth() >= Diver.TOTAL_DEPTH && this.getListOfEnemies().isEmpty()){
        flag = 1; // to break the loop
        this.boss = new Boss(GameFrame.SCREEN_WIDTH, 130, this.diver, this);
        this.boss.setPreferredSize(new Dimension(1280, 720));
        Thread bossThread = new Thread(this.boss);
        this.add(this.boss);
        bossThread.start();
        HPBar hb = new HPBar(this.boss); // instantiate the hp bar
        this.add(hb);
        try{
          Thread.sleep(1000);
        } catch(Exception e){}
        while(this.boss.getHp() > 0 && this.diver.isAlive() == true){
          this.checkXPos();
          if(this.getListOfEnemies().size() == 0) this.spawnMissiles(); // if the missiles are already typed, spawn missiles
            this.startMissileThreads();
          try{
            Thread.sleep(200);
          } catch(Exception e){ e.getMessage(); }
        }
      }
    }
  }

  private synchronized void startMissileThreads(){
    for(Iterator<Enemy> iter = this.getListOfEnemies().iterator(); iter.hasNext();){
      Enemy temp = iter.next();
      if(temp instanceof Missile){
        Missile m = (Missile) temp;
        Thread missileThread = new Thread(m);
        this.add(m);
        missileThread.start();
      }
    }
  }
  @Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(this.img, 0, 0,null);//display this image as a background
    Font font = new Font("arial", Font.BOLD, 20);
    g.setColor(Color.WHITE);
    g.setFont(font);
    Toolkit.getDefaultToolkit().sync();//makes animations smooth
	}
}
