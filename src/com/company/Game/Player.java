package com.company.Game;

public class Player {

    // - - - Variablen - - -
    private int myPosX;
    private int myPosY;
    private int myHealth;
    // - - - - - - - - - - -

    public Player (int x, int y) {
        this.setMyPosX(x);
        this.setMyPosY(y);
        this.setMyHealth(100);
    }


    public int getMyPosX() {
        return myPosX;
    }


    public void setMyPosX(int newPosX) {
        if ( Math.abs(this.myPosX-newPosX) > 2 ) {
            System.out.println("not allowed movement (more then 1 step)");
        }
        this.myPosX = newPosX;
    }


    public int getMyPosY() {
        return myPosY;
    }


    public void setMyPosY(int newPosY) {
        if ( Math.abs(this.myPosY-newPosY) > 2 ) {
            System.out.println("komisch");
        }
        this.myPosY = newPosY;
    }


    public int getMyHealth() {
        return myHealth;
    }


    public void setMyHealth(int newHealth) {
        this.myHealth = newHealth;
    }



}
