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
    private int width = 1800;
    private int height = 1000;

    // Visible Components
    // GUI
    private GUI gui;
    // Window
    private JFrame frame;
    // Buttons
    private JButton btnLoadGame;    // load game
    private JButton btnHuman;       // human player
    private JButton btnAI;          // ai player
    private JButton btnTraining;    //
    private JButton btnMenue;       // back to start
    private JButton btnLoadAI;      // load AI from file
    private JButton btnNewAI;       // calculate new AI pool
    private JButton btnShowAllAI;   // show all AI in pool (structure only)
    private JButton btnRunBestAI;   // show best AI
    private JButton btnSaveAI;      // save AI to file
    // Label
    private JLabel lblScore;        // shows actual score
    private JLabel lblEnter;        // information to hit ENTER for move
    private JLabel lblResult;       // Result
    private JLabel lblAISettings;   // Session-settings
    private JLabel lblAIProperties; // Properties of specific AI

    // Textbox
    private JTextField txtScore;
    // Checkbox (Input)
    private JCheckBox chkUP;
    private JCheckBox chkRIHGT;
    // Instances
    // Game
    private Game game;

    // AI
    // ... ... ...


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
        btnLoadGame = new JButton("Load Game");
        btnLoadGame.setBounds(20,20,100,40);
        btnLoadGame.requestFocus();
        btnLoadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.start(gui);
                double [][] gameMap = game.getMap();
                frame.getContentPane().remove(btnLoadGame);
                showCompletePlayground(gui, gameMap);

                // - - - - - - - - - - - -

                // Play yourself
                btnHuman = new JButton("Human Player");
                btnHuman.setBounds(220,20,150,40);
                btnHuman.requestFocus();
                btnHuman.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { playHuman(); }
                });
                frame.getContentPane().add(btnHuman);

                // - - - - - - - - - - - -

                // Training
                btnTraining = new JButton("Compute AI");
                btnTraining.setBounds(420,20,150,40);
                btnTraining.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AIworkout();
                    }
                });
                frame.getContentPane().add(btnTraining);

                // - - - - - - - - - - - -

                // Show best Calculation
                btnMenue = new JButton("Menue (Esc)");
                btnMenue.setBounds(width-200,20,150,40);
                btnMenue.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { showMenue(); }
                });
                frame.getContentPane().add(btnMenue);

                // - - - - - - - - - - - -

                frame.repaint(100);
            }
        });
        frame.getContentPane().add(btnLoadGame);
    }



    private void showCompletePlayground (GUI gui, double [][] map) {

        BufferedImage image = gui.getSurface();
        Graphics g = image.getGraphics();

        // 10 free pixels each side
        // TilesWidthNum / Width = pixels/tile in sqrt
        int tileSide =  (gui.myWidth-10) / (map[0].length);
        int bias = (( (gui.myWidth-10)%(tileSide*map[0].length) )/2)-5;
        // TilesHeightNum * pixels = HeightReduction
        int newHeight = (gui.myHeight) - ( ((map.length+1) * tileSide) + 50);

        // show state
        g.setColor(Color.black);
        g.drawRect(bias+5,newHeight-15,(tileSide*map[0].length)+10,(tileSide*(map.length+1))+10 );
        g.fillRect(bias+5,newHeight-15,(tileSide*map[0].length)+10,(tileSide*(map.length+1))+10 );
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
                    g.drawRect(bias+10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    g.fillRect(bias+10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    if (h == map.length-1) {
                        g.setColor(Color.red);
                        g.drawRect(bias+10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                        g.fillRect(bias+10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                    }
                } else {
                    // Wall & Floor
                    g.setColor(Color.black);
                    g.drawRect(bias+10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    g.fillRect(bias+10+tileSide*w,newHeight-10+tileSide*h,tileSide,tileSide);
                    if (h == map.length-1) {
                        g.drawRect(bias+10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
                        g.fillRect(bias+10+tileSide*w,newHeight-10+tileSide*(h+1),tileSide,tileSide);
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
        frame.getContentPane().remove(btnTraining);

        // add components
        // Labels
        // ... for Score
        lblScore = new JLabel("Score :   ", SwingConstants.CENTER);
        lblScore.setBounds(20,20,130,40);
        lblScore.setFont(new Font(lblScore.getFont().getName(),Font.PLAIN, 20));
        lblScore.setForeground(Color.white);
        lblScore.setBackground(Color.lightGray);
        lblScore.setVisible(true);
        frame.getContentPane().add(lblScore);
        // ... for Enter
        lblEnter = new JLabel("Press  ENTER  when ready.", SwingConstants.CENTER);
        lblEnter.setBounds(650,20,250,40);
        lblEnter.setFont(new Font(lblEnter.getFont().getName(),Font.PLAIN, 20));
        lblEnter.setForeground(Color.white);
        lblEnter.setBackground(Color.lightGray);
        lblEnter.setVisible(true);
        frame.getContentPane().add(lblEnter);
        // Textfield
        // ... for Score
        txtScore = new JTextField(""+game.playersFitness(),1);
        txtScore.setBounds(170,20,50,40);
        txtScore.setFont(new Font(lblEnter.getFont().getName(),Font.PLAIN, 20));
        txtScore.setForeground(Color.white);
        txtScore.setBackground(Color.darkGray);
        txtScore.setFocusable(true);
        txtScore.setVisible(true);
        frame.getContentPane().add(txtScore);
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
        txtScore.addKeyListener(this);
        txtScore.requestFocus();

        frame.repaint(100);

        // game open for input (key listener)
    }


    private void AIworkout () {

        // remove buttons
        frame.getContentPane().remove(btnHuman);
        frame.getContentPane().remove(btnTraining);

        // ... AI ...
        // New
        btnNewAI = new JButton("New AI");
        btnNewAI.setBounds(220,20,150,40);
        btnNewAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { }
        });
        frame.getContentPane().add(btnNewAI);

        // - - - - - - - - - - - -

        // Load
        btnLoadAI = new JButton("Load AI");
        btnLoadAI.setBounds(420,20,150,40);
        btnLoadAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { }
        });
        frame.getContentPane().add(btnLoadAI);

        // - - - - - - - - - - - -

        // Show best Calculations
        btnShowAllAI = new JButton("Show Pool");
        btnShowAllAI.setEnabled(false);
        btnShowAllAI.setBounds(620,20,150,40);
        btnShowAllAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        frame.getContentPane().add(btnShowAllAI);

        // - - - - - - - - - - - -

        // Show best Calculations
        btnRunBestAI = new JButton("Run Best");
        btnRunBestAI.setEnabled(false);
        btnRunBestAI.setBounds(620,20,150,40);
        btnRunBestAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        frame.getContentPane().add(btnRunBestAI);

        // - - - - - - - - - - - -

        // New
        btnSaveAI = new JButton("Save AI");
        btnSaveAI.setEnabled(false);
        btnSaveAI.setBounds(820,20,150,40);
        btnSaveAI.requestFocus();
        btnSaveAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { saveAI(); }
        });
        frame.getContentPane().add(btnSaveAI);

        // - - - - - - - - - - - -
        gui.pntAIArea();
        // - - - - - - - - - - - -
        // Labels
        lblAISettings = new JLabel("Setting", SwingConstants.CENTER);
        lblAISettings.setBounds(20,150,130,40);
        lblAISettings.setFont(new Font(lblAISettings.getFont().getName(),Font.PLAIN, 20));
        lblAISettings.setForeground(Color.white);
        lblAISettings.setBackground(Color.lightGray);
        lblAISettings.setVisible(true);
        frame.getContentPane().add(lblAISettings);
        // - - - - - - - - - - - -
        lblAIProperties = new JLabel("AI Properties", SwingConstants.CENTER);
        lblAIProperties.setBounds(width-200,150,130,40);
        lblAIProperties.setFont(new Font(lblAIProperties.getFont().getName(),Font.PLAIN, 20));
        lblAIProperties.setForeground(Color.white);
        lblAIProperties.setBackground(Color.lightGray);
        lblAIProperties.setVisible(true);
        frame.getContentPane().add(lblAIProperties);
        // - - - - - - - - - - - -
        frame.repaint(100);


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



    private void loadAI() {
        // toDooo;
    }



    private void computeNewAI() {
        // toDooo;
    }



    private void saveAI() {
        // toDooo;
    }



    private void showMenue () {
        // clear GUI
        frame.getContentPane().removeAll();
        // reset objects
        game = null;
        // Buttons
        btnLoadGame = null;
        btnHuman = null;
        btnAI = null;
        btnTraining = null;
        btnMenue = null;
        btnLoadAI = null;
        btnNewAI = null;
        btnSaveAI = null;
        // Label
        lblScore = null;
        lblEnter = null;
        lblResult = null;
        // Textbox
        txtScore = null;
        // Checkbox
        chkUP = null;
        chkRIHGT = null;
        // Clear gui
        gui.UpdateGui();
        // - - - - -
        // restart
        operate();

        frame.repaint(100);
    }



    public void keyPressed(KeyEvent e) {
        // System.out.println("- keyPressed -");

        // reset gui
        txtScore.setText(""+game.playersFitness());
        txtScore.requestFocus();
    }



    public void keyReleased(KeyEvent e) {
        System.out.println("- - - - - - KeyReleased - - - - - - ");
        if (game.isOpen()) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                // Create inputField
                double [] newInputs = new double [2] ;
                // Calculate values
                if (chkRIHGT.isSelected()){
                    newInputs[0] = 0.9;
                } else {
                    newInputs[0] = 0.0;
                }
                if (chkUP.isSelected()){
                    newInputs[1] = 0.9;
                } else {
                    newInputs[1] = 0.0;
                }

                System.out.println("... ENTER ...  Right = " + newInputs[0] + "   Up = " + newInputs[1]);

                // send to Game
                game.nextGameState(newInputs);
                frame.repaint(100);

                // reset checkboxes
                chkUP.setSelected(false);
                chkRIHGT.setSelected(false);

            } else if (e.getKeyCode() == KeyEvent.VK_W){
                if (chkUP.isSelected()) {
                    chkUP.setSelected(false);
                } else {
                    chkUP.setSelected(true);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_J){
                if (chkUP.isSelected()) {
                    chkUP.setSelected(false);
                } else {
                    chkUP.setSelected(true);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_D){
                if (chkRIHGT.isSelected()) {
                    chkRIHGT.setSelected(false);
                } else {
                    chkRIHGT.setSelected(true);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_R){
                if (chkRIHGT.isSelected()) {
                    chkRIHGT.setSelected(false);
                } else {
                    chkRIHGT.setSelected(true);
                }
            }
        } else {
            game.showGameState();
            // txtScore.removeKeyListener(this);
            // Labels
            // ... for Result
            lblResult = new JLabel("Final score :   " + game.playersFitness(), SwingConstants.CENTER);
            lblResult.setBounds(50,height-200,300,40);
            lblResult.setFont(new Font(lblScore.getFont().getName(),Font.PLAIN, 20));
            lblResult.setForeground(Color.red);
            lblResult.setBackground(Color.darkGray);
            lblResult.setVisible(true);
            lblResult.requestFocus();
            frame.getContentPane().add(lblResult);

            frame.repaint(100);

        }

        System.out.println("Taste:  " + e.getKeyChar() + " , Code:  " + e.getKeyCode());
        System.out.println("");
        System.out.println("");
        // reset gui
        txtScore.setText(""+game.playersFitness());
        txtScore.requestFocus();
    }



    public void keyTyped(KeyEvent e) {
        // System.out.println("----- keyTyped -----");

        // reset gui
        txtScore.setText(""+game.playersFitness());
        txtScore.requestFocus();
    }

}
