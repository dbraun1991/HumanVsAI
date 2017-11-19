package com.company.GUI;

import com.company.AI.Neuron;
import com.company.AI.Pool;
import com.company.AI.Species;
import com.company.Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
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
        int minX = 250;
        int minY = 150;
        int maxX = this.myWidth-550;
        int maxY = this.myHeight-500;
        // Game
        int sqrPix = 21;
        int gameX = 5;
        int gameY = 8;
        int canvasX = (this.myWidth/2) - (sqrPix * gameY)/2;
        int canvasY = (int)(this.myHeight*0.75) - (sqrPix * gameX)/2;
        // Neuron
        int neuronSize = 21;
        int linkSize = 5;

        // Background
        g.setColor(Color.darkGray);
        g.fillRect(minX,minY, maxX, this.myHeight-300);
        g.setColor(Color.black);
        g.fillRect(minX,minY, maxX, maxY);

        minX = minX+25;
        minY = minY+25;
        maxX = maxX - 50;
        maxY = maxY - 50 - neuronSize;

        // game
        myGame.showGameState();

        // temp
        pntIdeas(g);

        // show AI
        if (!(AI == null)) {
            // paint that

            // low layer to top layer
            int layers = AI.length-1;
            double [][] gameWindow = myGame.getGameWindow();

            if (!(pool.input == null)) {
                Neuron [] input = pool.input;
                // scope = field of inputs
                int scope = input.length;

                int maxLayer = AI[layers].getLast().getLayer();
                int stepY = (maxY) / (maxLayer);
                int stepX;

                int i = -1;              // layer Iterator
                int [] stepSizes = new int [layers+1];   // stepX of each layer
                int indexInput;         // index in Input matrix
                int intoLayer;          // layer of incoming Neuron
                int startLinkX;         // Link Start X
                int startLinkY;         // Link Start Y
                int endLinkX;           // Link End X
                int endLinkY;           // Link End Y

                for (LinkedList<Neuron> LayerList : AI) {
                    i++;
                    stepX = (maxX) / (LayerList.size()+1);  // maxX / (neurons in Layer)
                    stepSizes[i] = stepX;

                    for (int j = 0; j < LayerList.size(); j++) {
                        // Paint neuron
                        g.setColor(Color.blue);
                        g.fillRect(minX + stepX*(j+1), minY + maxY - stepY*i, neuronSize, neuronSize);

                        // Paint links
                        g.setColor(Color.red);
                        // Just painted Neuron
                        Neuron n = LayerList.get(j);
                        // Incoming list of n
                        LinkedList <Neuron> into = n.getIncoming();
                        // Links
                        for (int k = 0; k < into.size(); k++) {
                            intoLayer = into.get(k).getLayer();
                            if (intoLayer == -1) {
                                // Neuron from Input
                                indexInput = Arrays.asList(pool.input).indexOf(into.get(k));
                                if (!(indexInput < 0)) {
                                    // Exists in inputs
                                    // Index - Location in matrix
                                    gameX = indexInput % 8;                 // walk
                                    gameY = Math.floorDiv(indexInput,8);    // jump
                                    // find positions
                                    startLinkX = canvasX+(sqrPix*gameX)+(neuronSize/2)-(linkSize/2);
                                    startLinkY = canvasY+(sqrPix*gameY)+(neuronSize/2)-(linkSize/2);
                                    // g.fillRect(canvasX+(sqrPix*i1)+1,canvasY+(sqrPix*i0)+1, sqrPix-1,sqrPix-1);
                                    endLinkX = minX + stepX*(j+1) + (neuronSize/2) -(linkSize/2);
                                    endLinkY = minY + maxY - stepY*i + (neuronSize/5*4)-(linkSize/2);

                                    g.setColor(Color.red);
                                    // Start Square
                                    g.fillRect(startLinkX, startLinkY, linkSize, linkSize);
                                    // End Square
                                    g.fillRect(endLinkX, endLinkY, linkSize, linkSize);

                                    g.setColor(Color.red);
                                    // Into -> Neuron ... link it
                                    g.drawLine(startLinkX+(linkSize/2), startLinkY+(linkSize/2), endLinkX+(linkSize/2), endLinkY+(linkSize/2));
                                }
                            } else {
                                // Neuron from AI

                                // find in Layer
                                indexInput = AI[intoLayer].indexOf(into.get(k));    // into.get(k) // intoLayer

                                // find positions
                                startLinkX = minX + stepSizes[intoLayer]*(indexInput+1) + (neuronSize/2) -(linkSize/2);
                                startLinkY = minY + maxY - stepY*indexInput + (neuronSize/5*1)-(linkSize/2);
                                // g.fillRect(canvasX+(sqrPix*i1)+1,canvasY+(sqrPix*i0)+1, sqrPix-1,sqrPix-1);
                                endLinkX = minX + stepX*(j+1) + (neuronSize/2) -(linkSize/2);
                                endLinkY = minY + maxY - stepY*i + (neuronSize/5*4)-(linkSize/2);

                                g.setColor(Color.red);
                                // Start Square
                                g.fillRect(startLinkX,startLinkY, linkSize, linkSize);
                                // End Square
                                g.fillRect(endLinkX,endLinkY, linkSize, linkSize);

                                // Into -> Neuron ... link it
                                g.setColor(Color.red);
                                g.drawLine(startLinkX+(linkSize/2), startLinkY+(linkSize/2), endLinkX+(linkSize/2), endLinkY+(linkSize/2));

                            }

                        }
                    }
                }

            }
            // done
        }

    }



    private void pntIdeas (Graphics g) {

        int X = 300 + (int)Math.random()*30;
        int Y = 200 + (int)Math.random()*30;

        int tileSize = 21;
        int linkSize = 5;

        int linkX = 0;
        int linkY = 0;

        g.setColor(Color.lightGray);
        g.fillRect(X,Y, tileSize, tileSize);

        g.setColor(Color.red);
        g.fillRect(X+1+((1+linkX)*3)+(linkSize*linkX),Y+1+((1+linkY)*3)+(linkSize*linkY), linkSize, linkSize);

        linkX = 1;
        g.setColor(Color.yellow);
        g.fillRect(X+1+((1+linkX)*3)+(linkSize*linkX),Y+1+((1+linkY)*3)+(linkSize*linkY), linkSize, linkSize);

        linkX = 0;
        linkY = 1;
        g.setColor(Color.blue);
        g.fillRect(X+1+((1+linkX)*3)+(linkSize*linkX),Y+1+((1+linkY)*3)+(linkSize*linkY), linkSize, linkSize);

        linkX = 1;
        g.setColor(Color.green);
        g.fillRect(X+1+((1+linkX)*3)+(linkSize*linkX),Y+1+((1+linkY)*3)+(linkSize*linkY), linkSize, linkSize);

    }



    public BufferedImage getSurface() {
        return mySurface;
    }



}


