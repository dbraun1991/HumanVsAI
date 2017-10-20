package com.company;

import com.company.GUI.GUI;
import com.company.Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

class Menue {

    // Dimension of window / canvas
    int width = 1800;
    int height = 1000;
    // GUI
    GUI gui;
    // Window
    JFrame frame;
    // Buttons
    JButton btnloadGame;
    JButton btnHuman;
    JButton btnAI;
    JButton btnTraining;
    // Instance
    Game game;
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
        frame.setLocationByPlatform(true);
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
                game.start();
                double [][] gameMap = game.getMap();
                showCompletePlayground(gui, gameMap);
                frame.getContentPane().remove(btnloadGame);

                // Play yourself
                btnHuman = new JButton("Human Player");
                btnHuman.setBounds(20,20,150,40);
                btnHuman.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // play myself
                    }
                });
                frame.getContentPane().add(btnHuman);

                // Training
                btnTraining = new JButton("train AI");
                btnTraining.setBounds(220,20,150,40);
                btnTraining.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // train AI
                    }
                });
                frame.getContentPane().add(btnTraining);

                // Show best Calculation
                btnAI = new JButton("show best AI");
                btnAI.setBounds(420,20,150,40);
                btnAI.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Show best Calculation
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

        // make learning platform visible

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


    private void playAI () {
        // start Game

        // start repeat (while game is running)

            // update Inputs

            // calculate

            // push result

        // end repeat
    }

}
