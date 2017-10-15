package com.company.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;



public class GUI {

    public static JLabel myView;
    public BufferedImage mySurface;

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
        // repaint
        Graphics g = mySurface.getGraphics();

        // background
        g.setColor(Color.darkGray);
        g.fillRect(0,0, this.myWidth, this.myHeight);

        // playground (whole)



        // playground (in game)
            // depends on game state



        // neuron (visualization)
            // paint

    }

    public BufferedImage getSurface() {
        return mySurface;
    }



}
