package com.company.Game;

import com.company.GUI.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Viewer {


    // - - - Variablen - - -
    private double map [] [];
    public Player gPosition;
    private GUI gui;
    private int gameX;
    private int gameY;
    // - - - - - - - - - - -


    public Viewer (double newMap [] [], Player position, GUI gui, int dimX, int dimY) {
        this.map = newMap;
        this.gPosition = position;
        this.gui = gui;
        this.gameX = dimX;
        this.gameY = dimY;
    }


    public void setNewMap (double newMap [] []) {
        this.map = newMap;
    }


    public double[] getInput () {
        double [] newInputs = new double [2] ;
        newInputs[0] = 0.0;
        newInputs[1] = 0.0;

        System.out.println("Eingeben    'd' = right    OR    'w' = up    OR    'dw' = right & up    OR    '' = nothing");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equals("d")) { // right
            newInputs[0] = 0.9;
        } else if (input.equals("w")) { // only up
            newInputs[1] = 0.9;
        } else if (input.equals("dw")) { // right and up
            newInputs[0] = 0.9;
            newInputs[1] = 0.9;
        }

        return newInputs;
    }


    public void view (Player actualPosition) {

        // get Graphics to paint on
        BufferedImage image = gui.getSurface();
        Graphics g = image.getGraphics();
        int sqrPix = 20;

        int canvasX = (gui.myWidth/2) - (sqrPix*2 *gameX)/2;
        int canvasY = (gui.myHeight/5*4) - (sqrPix*2 *gameY)/2;

        // delete old
        g.setColor(Color.darkGray);
        g.drawRect(canvasX-15,canvasY-15, (sqrPix*map[0].length)+10,(sqrPix*map.length)+30);
        g.fillRect(canvasX-15,canvasY-15, (sqrPix*map[0].length)+10,(sqrPix*map.length)+30);

        // draw
        g.setColor(Color.black);
        g.drawRect(canvasX-5,canvasY-5, (sqrPix*map[0].length)+10,(sqrPix*map.length)+10);
        g.fillRect(canvasX-5,canvasY-5, (sqrPix*map[0].length)+10,(sqrPix*map.length)+10);
        for (int i0 = 0; i0 < map.length; i0++) {
            boolean lastRow = false;
            if (i0 == map.length-1) {
                lastRow = true;
            }
            for (int i1 = 0; i1 < map[0].length; i1++) {
                g.setColor(Color.white);	// nothing
                g.drawRect(canvasX+(sqrPix*i1),canvasY+(sqrPix*i0), sqrPix,sqrPix);
                if (map[i0][i1] == -0.5) {
                    g.setColor(Color.lightGray);	// nothing
                } else if (map[i0][i1] == -0.25) {
                    g.setColor(Color.black);	        // wall
                } else if (map[i0][i1] == 0.0) {
                    g.setColor(Color.blue);	    // person
                }
                g.fillRect(canvasX+(sqrPix*i1)+1,canvasY+(sqrPix*i0)+1, sqrPix-1,sqrPix-1);
                if (lastRow && g.getColor()==Color.lightGray) {
                    g.setColor(Color.red);
                    g.fillRect(canvasX+(sqrPix*i1)+1,canvasY+(sqrPix*i0)+(sqrPix/2)+1, sqrPix-1,(sqrPix/2)-1);
                }
            }
        }

        System.out.println(" Your Position:   X: " + actualPosition.getMyPosX() + "    Y: " + actualPosition.getMyPosY() );

    }


}