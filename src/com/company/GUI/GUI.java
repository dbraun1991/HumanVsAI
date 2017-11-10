package com.company.GUI;

import com.company.AI.Neuron;
import com.company.AI.Pool;
import com.company.AI.Species;
import com.company.Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;


public class GUI {

    public static JLabel myView;
    private BufferedImage mySurface;

    public int myWidth=0;
    public int myHeight=0;

    private boolean humanPlayer = true;

    public GUI(int myWidth, int myHeight, boolean human) {

        // set properties
        this.myWidth=myWidth;
        this.myHeight=myHeight;
        this.humanPlayer = human;

        // create Window
        mySurface = new BufferedImage(myWidth, myHeight-50, BufferedImage.TYPE_INT_RGB);
        myView = new JLabel(new ImageIcon(mySurface));
        Graphics g = mySurface.getGraphics();
        // background
        g.setColor(Color.darkGray);
        g.fillRect(0,0,myWidth,myHeight);

        g.setColor(Color.BLACK);
        // Keep this until I figured out if it's painted on load or not.
        // g.drawLine(10, 20, 350, 380);
        g.dispose();
    }



    public void UpdateGui () {
        // Repaint
        Graphics g = mySurface.getGraphics();

        // Background
        g.setColor(Color.darkGray);
        g.fillRect(0,0, this.myWidth, this.myHeight);
    }



    public void pntAIArea (Game myGame, LinkedList<Neuron> [] AI, Pool pool) {
        // Repaint
        Graphics g = mySurface.getGraphics();

        // Background
        g.setColor(Color.darkGray);
        g.fillRect(250,150, this.myWidth-500, this.myHeight-300);
        g.setColor(Color.black);
        g.fillRect(250,150, this.myWidth-500, this.myHeight-500);

        // game
        myGame.showGameState();

        // temp
        pntIdeas(g);

        // show AI
        if (!(AI == null)) {
            // paint that

            // low layer to top layer
            int layers = AI.length;
            double [][] gameWindow = myGame.getGameWindow();

            if (!(AI == null)) {
                Neuron [] input = pool.input;



            }
            // done
        }

    }



    private void pntIdeas (Graphics g) {

        int X = 300;
        int Y = 200;

        int tilesize = 21;
        int linksize = 5;

        int linkX = 0;
        int linkY = 0;

        g.setColor(Color.lightGray);
        g.fillRect(X,Y, tilesize, tilesize);

        g.setColor(Color.red);
        g.fillRect(X+1+((1+linkX)*3)+(linksize*linkX),Y+1+((1+linkY)*3)+(linksize*linkY), linksize, linksize);

        linkX = 1;
        g.setColor(Color.yellow);
        g.fillRect(X+1+((1+linkX)*3)+(linksize*linkX),Y+1+((1+linkY)*3)+(linksize*linkY), linksize, linksize);

        linkX = 0;
        linkY = 1;
        g.setColor(Color.blue);
        g.fillRect(X+1+((1+linkX)*3)+(linksize*linkX),Y+1+((1+linkY)*3)+(linksize*linkY), linksize, linksize);

        linkX = 1;
        g.setColor(Color.green);
        g.fillRect(X+1+((1+linkX)*3)+(linksize*linkX),Y+1+((1+linkY)*3)+(linksize*linkY), linksize, linksize);

    }



    public BufferedImage getSurface() {
        return mySurface;
    }



}


