import java.util.HashMap;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class GameFrame extends JFrame{
  final static JPanel cards = new JPanel(new CardLayout());
  static String currentWord = "";
  JPanel menuCard; //main menu
 /*For Card Layout, Panels or cards that switch*/
  JPanel playCard; //the stage --> ocean
  JPanel instructionCard;
  JPanel leaderboardCard;
  JPanel creatorsCard;
/************************************/
  JLabel background;//Background Image
  JPanel halfPanel;//For dividing the window size into two for aesthetic
  JPanel buttonContainer;//Panel that contains the 4 buttons
  JButton playButton;//Play Button for New Game
  JButton instructButton;//Instruction Button for How to Play
  JButton hsButton;//Leaderboard Button for High Score
  JButton creatorsButton;// Button for the Creators of the Game
  JButton back;//Back button


  /*Screen's Window Display Size*/
  public static volatile boolean EXIT = false;
  public final static int SCREEN_WIDTH = 1280;
  public final static int SCREEN_HEIGHT  = 710;
  /******************************/
  private Ocean ocean;
  private static Container container;
  //private Thread oceanThread;
  private static JTextArea textArea = new JTextArea();
  private static HashMap<Integer, String> players;
  private int[] listOfScores;

  public GameFrame(){
    this.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));//Sets size of the window display
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//For exit button and minimize button (window display)
    this.setResizable(false);//The window is not resizable
    this.setTitle("TyperShark");//Window Display Title

    this.container = this.getContentPane();//Container

    /**menu card panel**/
    this.menuCard = new JPanel();
    this.menuCard.setLayout(new FlowLayout());
    this.menuCard.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));

    //to add image in background
    this.background = new JLabel(new ImageIcon("../images/MenuBG.png"));
    this.background.setLayout(new FlowLayout()); //Uses JLabel for other components to be seen (if not,other components are unseen)

    /*Graphics on the right side of the window and the buttons on the left side*/
    this.halfPanel=new JPanel(new GridLayout(0,2,0,0)); //row, col, horizontal gap, vertical gap
    this.halfPanel.setPreferredSize(new Dimension(1100,640));//width,height
    this.halfPanel.setOpaque(false);//Transparency

    /*Contains the buttons in horizontal manner, 4 rows*/
    this.buttonContainer=new JPanel(new GridLayout(4,0,0,5));
    this.buttonContainer.setPreferredSize(new Dimension(720,640));//width,height
    this.buttonContainer.setOpaque(false);
    this.buttonContainer.setBorder(new EmptyBorder(50, 0, 0, 0)); //top left down right //padding purposes


    /*Creating buttons and their customizations*/
    /*NEW GAME BUTTON*/
    //playButton
    this.playButton=new JButton(new ImageIcon("../images/newGame.png"));
    this.playButton.setBorderPainted(false); //Transparent border
    this.playButton.setFocusPainted(false); //Transparent box for hyperlink
    this.playButton.setContentAreaFilled(false); //Removes the default gradient background


    //add MouseListener for playButton
    playButton.addMouseListener(new MouseListener(){
  		public void mouseClicked(MouseEvent e){}

  		public void mousePressed(MouseEvent e){}

  		public void mouseReleased(MouseEvent e){}

  		public void mouseEntered(MouseEvent e){ //hover
       ImageIcon hover=new ImageIcon("../images/newGameHover.png");//the variable hover gets the image when mouseEntered
        playButton.setIcon(hover);//parameter should be a variable
      }

  		public void mouseExited(MouseEvent e){//exits
        ImageIcon exit=new ImageIcon("../images/newGame.png");//the variable exit gets the image when mouseExited
        playButton.setIcon(exit);//parameter should be a variable
      }
  	});
    /***************************************************/

    /*HOW TO PLAY BUTTON*/
    //instructButton
    this.instructButton=new JButton(new ImageIcon("../images/howToPlay.png"));
    this.instructButton.setBorderPainted(false); //Transparent border
    this.instructButton.setFocusPainted(false); //Transparent box for hyperlink
    this.instructButton.setContentAreaFilled(false); //Removes the default gradient background



    //add MouseListener for instructButton
    instructButton.addMouseListener(new MouseListener(){
  		public void mouseClicked(MouseEvent e){}

  		public void mousePressed(MouseEvent e){}

  		public void mouseReleased(MouseEvent e){}

  		public void mouseEntered(MouseEvent e){
        ImageIcon hover=new ImageIcon("../images/howToPlayHover.png");//the variable hover gets the image when mouseEntered
        instructButton.setIcon(hover);
      }

  		public void mouseExited(MouseEvent e){
        ImageIcon exit=new ImageIcon("../images/howToPlay.png");
        instructButton.setIcon(exit);
      }
  	});
    /***************************************************/
    //hsButton
    this.hsButton=new JButton(new ImageIcon("../images/hs.png"));
    this.hsButton.setBorderPainted(false); //Transparent border
    this.hsButton.setFocusPainted(false); //Transparent box for hyperlink
    this.hsButton.setContentAreaFilled(false); //Removes the default gradient background


    //add MouseListener for instructButton
    hsButton.addMouseListener(new MouseListener(){
      public void mouseClicked(MouseEvent e){}

      public void mousePressed(MouseEvent e){}

      public void mouseReleased(MouseEvent e){}

      public void mouseEntered(MouseEvent e){
       ImageIcon hover=new ImageIcon("../images/hsHover.png");//the variable hover gets the image when mouseEntered
        hsButton.setIcon(hover);
      }

      public void mouseExited(MouseEvent e){
       ImageIcon exit=new ImageIcon("../images/hs.png");
        hsButton.setIcon(exit);
      }
    });

    this.creatorsButton=new JButton(new ImageIcon("../images/creators.png"));
    this.creatorsButton.setBorderPainted(false); //Transparent border
    this.creatorsButton.setFocusPainted(false); //Transparent box for hyperlink
    this.creatorsButton.setContentAreaFilled(false); //Removes the default gradient background

    creatorsButton.addMouseListener(new MouseListener(){
     public void mouseClicked(MouseEvent e){}

     public void mousePressed(MouseEvent e){}

     public void mouseReleased(MouseEvent e){}

     public void mouseEntered(MouseEvent e){ //hover
        ImageIcon hover=new ImageIcon("../images/creatorHover.png");//the variable hover gets the image when mouseEntered
        creatorsButton.setIcon(hover);
      }

     public void mouseExited(MouseEvent e){
        ImageIcon exit=new ImageIcon("../images/creators.png");
        creatorsButton.setIcon(exit);
      }
   });





    //add the buttons in JPanel buttonContainer
    buttonContainer.add(playButton);
    buttonContainer.add(instructButton);
    buttonContainer.add(hsButton);
    buttonContainer.add(creatorsButton);
    //add buttonContiainer in a JPanel halfPanel
    halfPanel.add(buttonContainer);
    //add halfPanel in JLabel background
    background.add(halfPanel);



    this.menuCard.add(background);
    this.revalidate();
    /**end of menuCard**/



    /*ocean is a card*/
    this.back = new JButton("Back");
    this.back.setBounds(1150, 630, 80, 45);
    this.ocean = new Ocean(back);
    /*end of playCard*/

    /**instructionCard**/
    JLabel howToPlayLabel=new JLabel(new ImageIcon("../images/instructions.png"));
    howToPlayLabel.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH,680));
    this.instructionCard = new JPanel();

    this.instructionCard.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));

    JButton backButton=new JButton("Back");
    backButton.setContentAreaFilled(false); //Removes the default gradient background
    backButton.setBorderPainted(false); //Transparent border
    backButton.setFocusPainted(false); //Transparent box for hyperlink
    this.instructionCard.add(backButton);
    this.instructionCard.add(howToPlayLabel);
    backButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
      CardLayout c= (CardLayout) cards.getLayout();
      c.show(cards, "Main");
    }
  });
    this.instructionCard.revalidate();//Serves like repaint in the position of panels


    /**leaderboardCard**/
    JLabel hsLabel=new JLabel(new ImageIcon("../images/MenuBG.png"));
    hsLabel.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH,680));
    this.leaderboardCard = new JPanel();
    this.textArea = new JTextArea();
    this.leaderboardCard.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));
    JButton backButton2=new JButton("Back");
    backButton2.setContentAreaFilled(false); //Removes the default gradient background
    backButton2.setBorderPainted(false); //Transparent border
    backButton2.setFocusPainted(false); //Transparent box for hyperlink
    this.leaderboardCard.add(backButton2);
    this.leaderboardCard.add(textArea);
    this.leaderboardCard.add(hsLabel);
     backButton2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
      CardLayout c= (CardLayout) cards.getLayout();
      c.show(cards, "Main");
    }
    });
    this.leaderboardCard.revalidate();//Serves like repaint in the position of panels
    /**end of leaderboardCard**/



    /**creatorsCard*/
    JLabel creatorLabel=new JLabel(new ImageIcon("../images/creatorbg.png"));
    creatorLabel.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH,680));
    this.creatorsCard = new JPanel();

    this.creatorsCard.setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));

    JButton backButton3=new JButton("Back");
    backButton3.setContentAreaFilled(false); //Removes the default gradient background
    backButton3.setBorderPainted(false); //Transparent border
    backButton3.setFocusPainted(false); //Transparent box for hyperlink
    this.creatorsCard.add(backButton3);
    this.creatorsCard.add(creatorLabel);
     backButton3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
      CardLayout c= (CardLayout) cards.getLayout();
      c.show(cards, "Main");
    }
    });
    this.creatorsCard.revalidate();//Serves like repaint in the position of panels
    /**end of creatorsCard**/

    //adding the cards
    this.cards.add(this.menuCard, "Main");
    this.cards.add(this.ocean, "Play");
    this.cards.add(this.instructionCard, "Instructions");
    this.cards.add(this.leaderboardCard, "Highscores");
    this.cards.add(this.creatorsCard, "Creators");
    this.cards.revalidate();//Serves like repaint in the position of panels
    //add to the content pane
    container.add(this.cards, BorderLayout.CENTER);
    // this.setContentPane(this.cards);



    /**add actionlisteners to the buttons in the menuCard**/
   playButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){

        addKeyListener(new KeyListener(){
          public void keyPressed(KeyEvent ke){
            if(ke.getKeyCode() == KeyEvent.VK_ENTER){
              ocean.getDiver().electrocute(ocean, ocean.getListOfEnemies(), ocean.getEnergyTimer());
            }

            else ocean.checkWordsTyped(ke.getKeyChar());
          }
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){}
        });
        setFocusable(true);

        back.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            JDialog.setDefaultLookAndFeelDecorated(false);
            int response = JOptionPane.showConfirmDialog(null, "Do you want to continue? "
              + "Your progress won't be saved.", "Confirm", JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE);

            if(response == JOptionPane.YES_OPTION){
              stopThreads();
              CardLayout c = (CardLayout) cards.getLayout();
              c.show(cards, "Main");
            }
          }
        });


        CardLayout c = (CardLayout) cards.getLayout();
        c.show(cards, "Play");
        if(GameFrame.EXIT == true){
          // ocean.resetOcean();

          setVisible(false);

          GameFrame.EXIT = false;
          ocean = null;
          restart();
        }
        Thread oceanThread = new Thread(ocean);
        oceanThread.start();
        ocean.revalidate();
      }
    });

    instructButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) cards.getLayout();
        c.show(cards, "Instructions");
      }
    });


    hsButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) cards.getLayout();
        c.show(cards, "Highscores");
        players = new HashMap<Integer, String>();
        try{
          String[] tokens;
          String line;
          BufferedReader reader = new BufferedReader(new FileReader("scores.txt"));
          while((line = reader.readLine()) != null){
            tokens = line.split(" ");
            players.put(Integer.parseInt(tokens[1]), tokens[0]);
            reader.close();
          }
        } catch(Exception f){ f.getMessage(); }
        ArrayList<Integer> integers = new ArrayList<Integer>(players.keySet());
        int size = players.keySet().size();
        listOfScores = new int[size];
        for(int i = 0; i < size; i++){
          listOfScores[i] = integers.get(i);
        }
        Arrays.sort(listOfScores);

        textArea.setPreferredSize(new Dimension(500, 500));
        textArea.setEditable(false);
        for(int i = 0; i < size; ++i){
          textArea.append(players.get(listOfScores[i]) + " " + listOfScores[i] + "\n");
        }
      }
    });

    creatorsButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) cards.getLayout();
        c.show(cards, "Creators");
      }
    });

    this.pack();//Compact
    this.setLocationRelativeTo(null);
    this.setVisible(true);//Window/Game Visibility
  }

  private void stopThreads(){
    GameFrame.EXIT = true;
  }

  private void restart(){

    this.ocean = new Ocean(this.back);
  }
}
