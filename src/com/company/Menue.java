package com.company;

import com.company.GUI.GUI;
import com.company.Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

class Menue implements KeyListener {

    // given Properties
    // Dimension of window / canvas
    int width = 1800;
    int height = 1000;

    // Visible Components
    // GUI
    GUI gui;
    // Window
    JFrame frame;
    // Buttons
    JButton btnloadGame;
    JButton btnHuman;
    JButton btnAI;
    JButton btnTraining;
    // Label
    JLabel lblScore;        // shows actual score
    JLabel lblEnter;        // information to hit ENTER for move
    // Checkbox
    JCheckBox chkUP;
    JCheckBox chkRIHGT;
    // Instances
    // Game
    Game game;

    // AI
    // ... ... ...

    // Input
    boolean goUp = false;
    boolean goRight = false;


    Menue (int width, int height) {
        this.width = width;
        this.height = height;

        gui = new GUI(this.width, this.height, true);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui.myView);
        frame.pack();
        frame.setLocation(10,10);
        frame.setVisible(true);
        frame.repaint(100);



    }

    void operate () {
        // call GUI

        game = new Game(8,5);
        btnloadGame = new JButton("Load Game");
        btnloadGame.setBounds(20,20,100,40);
        btnloadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.start(gui);
                double [][] gameMap = game.getMap();
                frame.getContentPane().remove(btnloadGame);
                showCompletePlayground(gui, gameMap);

                // Play yourself
                btnHuman = new JButton("Human Player");
                btnHuman.setBounds(20,20,150,40);
                btnHuman.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { playHuman(); }
                });
                frame.getContentPane().add(btnHuman);

                // Training
                btnTraining = new JButton("train AI");
                btnTraining.setBounds(220,20,150,40);
                btnTraining.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        trainAI();
                    }
                });
                frame.getContentPane().add(btnTraining);

                // Show best Calculation
                btnAI = new JButton("show best AI");
                btnAI.setBounds(420,20,150,40);
                btnAI.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // represents Pool of AIs
                        int [] myAiPool = {1};
                        // shows best out of Pool
                        playAI(getBestAI(myAiPool));
                    }
                });
                frame.getContentPane().add(btnAI);


                frame.repaint(100);
            }
        });
        frame.getContentPane().add(btnloadGame);
    }



    private void showCompletePlayground (GUI gui, double [][] map) {

        BufferedImage image = gui.getSurface();
        Graphics g = image.getGraphics();

        // 10 free pixels each side
        // TilesWidthNum / Width = pixels/tile in sqrt
        int tileSide =  (gui.myWidth-20) / (map[0].length);
        // TilesHeightNum * pixels = HeightReduction
        int newHeight = (gui.myHeight) - ( ((map.length+1) * tileSide) + 50);
        gui.myHeight = newHeight;

        // show state
        g.setColor(Color.black);
        g.drawRect(5,newHeight-15,(tileSide*map[0].length)+10,(tileSide*(map.length+1))+10 );
        g.fillRect(5,newHeight-15,(tileSide*map[0].length)+10,(tileSide*(map.length+1))+10 );
        for (int h = 0; h < map.length; h++) {
            for (int w = 0; w < map[0].length; w++) {
                /**
                 System.out.println("h = " + h + " (0-4)    w = " + w + " (0-189)" );
                 *	Empty = -0.5
                 *	Wall = -0.25
                 */
                if (map[h][w] < -0.3) {
                    // Empty
                    g.setColor(Color.lightGray);
                    g.drawRect(10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    g.fillRect(10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    if (h == map.length-1) {
                        g.setColor(Color.red);
                        g.drawRect(10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                        g.fillRect(10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                    }
                } else {
                    // Wall & Floor
                    g.setColor(Color.black);
                    g.drawRect(10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    g.fillRect(10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    if (h == map.length-1) {
                        g.drawRect(10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                        g.fillRect(10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                    }
                }
            }
        }
    }


    /**
     * Key listener
     *      w = UP
     *      d = Right
     *      enter = ENTER
     */
    private void playHuman () {

        // remove buttons
        frame.getContentPane().remove(btnHuman);
        frame.getContentPane().remove(btnAI);
        frame.getContentPane().remove(btnTraining);

        // add components
        // score
        lblScore = new JLabel("Score :   " + game.playersFitness(), SwingConstants.CENTER);
        lblScore.setBounds(20,20,250,40);
        lblScore.setFont(new Font(lblScore.getFont().getName(),Font.PLAIN, 20));
        lblScore.setForeground(Color.white);
        lblScore.setBackground(Color.lightGray);
        lblScore.setVisible(true);
        frame.getContentPane().add(lblScore);
        // Label for Enter
        lblEnter = new JLabel("Press  ENTER  when ready.", SwingConstants.CENTER);
        lblEnter.setBounds(650,20,250,40);
        lblEnter.setFont(new Font(lblEnter.getFont().getName(),Font.PLAIN, 20));
        lblEnter.setForeground(Color.white);
        lblEnter.setBackground(Color.lightGray);
        lblEnter.setVisible(true);
        frame.getContentPane().add(lblEnter);

        // checkBox
        // UP
        chkUP = new JCheckBox("Jump  (w)",false);
        chkUP.setBounds(500,15,100,25);
        chkUP.setForeground(Color.darkGray);
        chkUP.setBackground(Color.lightGray);
        chkUP.setVisible(true);
        frame.getContentPane().add(chkUP);
        // RIGHT
        chkRIHGT = new JCheckBox("Right   (d)", false);
        chkRIHGT.setBounds(500,45,100,25);
        chkRIHGT.setForeground(Color.darkGray);
        chkRIHGT.setBackground(Color.lightGray);
        chkRIHGT.setVisible(true);
        frame.getContentPane().add(chkRIHGT);

        // Keylistener
        frame.addKeyListener(this);


        frame.repaint(100);

        // make learning platform visible (play game)
        //while ( game.isOpen() ) {
          //  game.nextGameState();
        //}
    }


    private void trainAI () {

        // while (time/ repitition is not reached)

            // for all AI in stock
                // if not played
                    // play AI (measure)

            // sort all by performance

            // cut out bad

            // create new

        // end training

    }


    private void playAI (int actualAI) {
        // start Game

        // start repeat (while game is running)

            // update Inputs

            // calculate

            // push result

        // end repeat
    }

    private int getBestAI(int [] thePool) {
        return thePool[0];
    }



    public void keyPressed(KeyEvent e) {
        System.out.println("--- keyPressed ---");
        System.out.println("Taste: " + e.getKeyChar() + ", Code: " + e.getKeyCode());
        System.out.println("Tastenposition: " + e.getKeyLocation());
        System.out.println("---");
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("--- KeyReleased ---");
        /**
         System.out.println("KeyReleased: ");
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            System.out.println("Programmabbruch!");
            System.exit(0);
        }
        System.out.println("Taste: " + e.getKeyChar() + ", Code: " + e.getKeyCode());
        System.out.println("---");
         */
    }

    public void keyTyped(KeyEvent e) {
        System.out.println("--- keyTyped ---");
        /**
        System.out.println("KeyTyped: ");
        if(e.getKeyChar() == KeyEvent.CHAR_UNDEFINED){
            System.out.println("Kein Unicode-Character gedr\u00FCckt!");
        }else{
            System.out.println(e.getKeyChar() + " gedr\u00FCckt!");
        }
        System.out.println("---");
        */
    }

}
