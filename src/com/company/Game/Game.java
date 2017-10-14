package com.company.Game;

public class Game {

    // - - - Variablen - - -

    private int dimX;
    private int dimY;

    /**
     *	Size . . . Default = 8x5
     *	@param Empty = -0.5
     *	@param Wall = -0.25
     *	@param Player = 0
     *	@param Enemy = 0.25
     *	@param Misile = 0.5		// later
     */
    private double myGameWindow [] [];

    /**
     * Inputs of Player . . .  activation if ( value > 0.5 ) in Rage ( -1 to 1 )
     *	@param 1 = Right
     *	@param 2 = Up
     */
    private double newInputs [];

    private Player myPlayer;
    private int jump;
    private int noMoveRight;

    private double [][] myMap = {
            { 	-0.5,	-0.5, 	-0.5, 	-0.5	},
            { 	-0.5, 	-0.5, 	-0.5, 	-0.5	},
            { 	-0.5, 	-0.5, 	-0.5, 	-0.5	},
            { 	-0.5, 	-0.5, 	-0.5, 	-0.5	},
            { 	-0.25, 	-0.25, 	-0.25, 	-0.25	} };
    private MapParts mapExtender;
    private boolean isRunning;

    private Viewer myView;

    // - - - - - - - - - - -




    /**
     *
     * @param dimX
     * @param dimY
     */
    public Game (int dimX, int dimY) {

        this.dimX = dimX;	// 8
        this.dimY = dimY;	// 5
        this.myGameWindow = new double [this.dimX] [this.dimY];

        this.myPlayer = new Player (2, this.dimY-2);		// linke untere Ecke (jeweils +1)
        this.jump = 0;
        this.noMoveRight = 0;

        this.mapExtender = new MapParts();
        this.myMap = mapExtender.appendTile(myMap, 12-1);

        /**
         this.myMap = mapExtender.appendTile(myMap, 0);
         this.myMap = mapExtender.appendTile(myMap, 1);
         this.myMap = mapExtender.appendTile(myMap, 2);
         this.myMap = mapExtender.appendTile(myMap, 3);
         this.myMap = mapExtender.appendTile(myMap, 4);
         this.myMap = mapExtender.appendTile(myMap, 5);
         this.myMap = mapExtender.appendTile(myMap, 6);
         this.myMap = mapExtender.appendTile(myMap, 7);
         this.myMap = mapExtender.appendTile(myMap, 8);
         this.myMap = mapExtender.appendTile(myMap, 9);
         this.myMap = mapExtender.appendTile(myMap, 10);
         /**
         *
         */

        System.out.println( "Length :" );
        System.out.println( myMap[0].length );
        System.out.println( "" );

        this.isRunning = false;
    }



    public void giveInput (double [] inputs) {
        this.newInputs = inputs;
        if (this.isRunning) {
            computeInputs(this.newInputs);
        }
    }



    public void computeInputs(double [] inputs) {
        int playerX = this.myPlayer.getMyPosX();
        int playerY = this.myPlayer.getMyPosY();

        // check horizontal movement
        if (inputs[0]> 0.5 && inputs[0] < 1.1) {
            // "right" is pressed
            if (isAccessible( this.myPlayer.getMyPosX()+1 , this.myPlayer.getMyPosY() )) {
                this.myPlayer.setMyPosX(this.myPlayer.getMyPosX()+1);
            }
        }

        // check vertical movement
        if (inputs[1]> 0.5 && inputs[1] < 1.1) {
            // "up" is pressed (active junmp)
            if (isSolid( playerX , playerY+1 )) {
                this.jump = 2;
            }
            if (this.jump > 0) {
                if (isAccessible( this.myPlayer.getMyPosX() , this.myPlayer.getMyPosY()-1 )) {
                    this.myPlayer.setMyPosY(this.myPlayer.getMyPosY()-1);
                    this.jump--;
                } else {
                    this.jump = 0;
                }
            } else {

                //  - - -

                if (!(isSolid( this.myPlayer.getMyPosX() , this.myPlayer.getMyPosY()+1 ))) {
                    // non solid ground - fall down
                    this.myPlayer.setMyPosY(this.myPlayer.getMyPosY()+1);

                    // End of Game
                    if (this.myPlayer.getMyPosY() == 4) {
                        this.isRunning = false;
                        System.out.println(" Game Over ");
                    }
                }

                //  - - -

            }
        } else {
            // rest of jump / stable / falling down
            if (this.jump > 0) {
                // power left to jump
                if (isAccessible( this.myPlayer.getMyPosX() , this.myPlayer.getMyPosY()-1 )) {
                    this.myPlayer.setMyPosY(this.myPlayer.getMyPosY()-1);
                    this.jump--;
                } else {
                    this.jump = 0;
                }
            } else {
                if (!(isSolid( this.myPlayer.getMyPosX() , this.myPlayer.getMyPosY()+1 ))) {
                    // non solid ground - fall down
                    this.myPlayer.setMyPosY(this.myPlayer.getMyPosY()+1);

                    // End of Game
                    if (this.myPlayer.getMyPosY() == 4) {
                        this.isRunning = false;
                        System.out.println(" Game Over ");
                    }
                }
            }
        }

        adjustGameWindow ();

        if (playerX == this.myPlayer.getMyPosX()) {
            this.noMoveRight++;
            if (this.noMoveRight > 10) {
                this.isRunning = false;
            }
        } else {
            this.noMoveRight = 0;
        }

    }



    public boolean isAccessible (int x, int y) {
        boolean myReturn = true;

        if (y > 4 || y < 0) {
            myReturn = false;
        } else {
            if (this.myMap[y][x] < -0.49) {
                myReturn = true;
            } else {
                myReturn = false;
            }
        }

        return myReturn;
    }



    public boolean isSolid (int x, int y) {
        boolean myReturn = true;

        if (this.myMap[y][x] > -0.49) {
            myReturn = true;
        } else {
            myReturn = false;
        }

        return myReturn;
    }



    /**
     * Update the Window for the Player to be seen
     */
    public void adjustGameWindow () {

        int startX = this.myPlayer.getMyPosX()-1;

        if (startX+1 > 1000) {
            System.out.println("Player hat   X > 1000   geschafft");
            this.isRunning = false;
        } else {
            if (this.isRunning) {
                // enlarge map if needed
                if (this.myMap[0].length < startX + 10) {
                    this.myMap = mapExtender.appendTile(myMap, (int) (Math.random() * 10));
                }

                // this.mapExtender.printMap(this.myMap);

                // create new Window
                double[][] newWindow = new double[this.dimY][this.dimX];

                for (int i0 = 0; i0 < this.dimY; i0++) {
                    for (int i1 = 0; i1 < this.dimX; i1++) {
                        newWindow[i0][i1] = this.myMap [i0][i1+startX];
                    }
                }

                // set Player into Map
                newWindow [this.myPlayer.getMyPosY()] [this.myPlayer.getMyPosX()-(startX)] = 0.0;

                this.myGameWindow = newWindow;
            }
        }
    }



    public int playersFitness () {
        return this.myPlayer.getMyPosX();
    }



    public boolean isOpen () {
        return this.isRunning;
    }



    public void start() {
        this.isRunning = true;
        adjustGameWindow();
        this.myView = new Viewer(this.myMap, this.myPlayer);

        System.out.println(" ");
        System.out.println(" - - - ");
        System.out.println("Initialisierung abgeschlossen");
        System.out.println(" - - - ");
        System.out.println(" ");

        this.myView.setNewMap(this.myGameWindow);
        showGameState();
    }



    public void nextGameState () {
        myView.view(this.myPlayer);
        giveInput(myView.getInput());
        myView.setNewMap(myGameWindow);
    }



    public void showGameState () {
        myView.view(this.myPlayer);
    }



    public double[] [] combineWithPlayer (double[] [] map) {
        this.myPlayer.getMyPosX();
        map[this.myPlayer.getMyPosX()] [this.myPlayer.getMyPosY()] = 0.0;
        return map;
    }





}