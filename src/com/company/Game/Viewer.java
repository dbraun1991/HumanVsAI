package com.company.Game;

import java.util.Scanner;

public class Viewer {


    // - - - Variablen - - -
    private double map [] [];
    public Player gPosition;
    // - - - - - - - - - - -


    public Viewer (double newMap [] [], Player position) {
        this.map = newMap;
        this.gPosition = position;
        view(this.gPosition);
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

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(" ||||||||||||||||||||||||||| ");

        for (int i0 = 0; i0 < map.length; i0++) {
            System.out.print(" ||| ");
            for (int i1 = 0; i1 < map[0].length; i1++) {
                if (map[i0][i1] == -0.5) {
                    System.out.print(" " + " ");	// nothing
                } else if (map[i0][i1] == -0.25) {
                    System.out.print("X" + " ");	// wall
                } else if (map[i0][i1] == 0.0) {
                    System.out.print("O" + " ");	// wall
                }
            }
            System.out.println();
        }
        System.out.println(" ||||||||||||||||||||||||||| ");
        System.out.println(" Your Position:   X: " + actualPosition.getMyPosX() + "    Y: " + actualPosition.getMyPosY() );
    }


}