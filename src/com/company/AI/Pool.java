package com.company.AI;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.LinkedList;
        import java.util.Scanner;

public class Pool {

    // Save Load
    private String myPath = "data.csv";
    private LoadSave RW = new LoadSave(myPath);

    // - - - Variables
    private ArrayList <Species> mySpecies;
    private int maxSpecies;
    private int maxOutputs;
    private int maxNodes;
    private int maxDepth;
    private int myGeneration;
    private int myInnovation;
    private int actualSpecies;
    private int actualGenome;
    private int actualFrame;
    private int maxFitness;
    // - - - - - - - -
    public Neuron [] input;
    private LinkedList <Neuron> Incoming;

    int output;




    /**
     * @param maxNumberOfSpecies
     * @param numberOfInputs
     * @param numberOfOutputs
     * @param maxNumberOfNodes
     * @param maxDepthInLayers
     */
    public Pool (int maxNumberOfSpecies, int numberOfInputs, int numberOfOutputs, int maxNumberOfNodes, int maxDepthInLayers) {

        boolean createNew = false;	// false = read from file ... true = create new instances
        this.Incoming = new LinkedList <Neuron>();

        // list to give the inputs
        this.input = new Neuron [numberOfInputs] ;
        for (int i=0 ; i<input.length; i++) {
            input [i] = new Neuron();
            input[i].setlayer(0);
            this.Incoming.add(input[i]);
        }
        // list to give the outputs
        // address the output-linked list of actual Specie

        // New List to put Species in
        this.mySpecies = new ArrayList <Species> ();

        // Number of Species in Pool (Maximum)
        this.maxSpecies = maxNumberOfSpecies;

        // Number of Outputs of each Species in Pool (fixed Maximum)
        if (numberOfOutputs > 0  &&  numberOfOutputs < 1000) {
            this.maxOutputs = numberOfOutputs;
        } else {
            this.maxOutputs = 300;
            System.out.println("Warning - maximum Output nodes were set to  ( default = 300 ) ");
        }

        // Maximum Depth of Species = Layers
        if (maxDepthInLayers > 3 || maxDepthInLayers < 30  ) {
            this.maxDepth = maxDepthInLayers;
        } else {
            this.maxDepth = 10;
            System.out.println("Warning - maximum Depth nodes were set to  ( default = 10 ) ");
        }

        // Number of Nodes of a Species (Maximum)
        //	- presume:	tree structure
        //				- many incomming nodes, few outgoing nodes
        //				- halfing nodes by every layer due to reduction of complexity
        //					- in case of not being perfect yet - MaxNumber = NumberIncomming * Layers
        //					- breeding will have a negative rating for many layers and many nodes --> reduction of complexity --> done in other instance
        this.maxNodes = this.Incoming.size() * this.maxDepth;
        if (maxNumberOfNodes > (this.maxNodes*0.1)  &&  maxNumberOfNodes < (this.maxNodes*100)) {
            // in case User gives a good guess of max Nodes
            this.maxNodes = maxNumberOfNodes;
        } else {
            // bad input guess
            System.out.println("Warning - maximum nodes per species is set to  ( default = " + maxNodes + " ) ");
        }

        // Overall Generation of Breeding
        this.myGeneration = 0;

        // Level of Innovation - Rated by calling Code
        this.myInnovation = 0;

        // Species in focus (looking at) --> default = 1
        this.actualSpecies = 1;

        // Genome in focus (looking at) --> default = 1
        this.actualGenome = 1;

        // Best reached overall fitness.
        this.maxFitness = 0;

        while (!createNew) {
            System.out.println(" New (= 'N') or Load (= 'L') ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            createNew = true;

            if (input.equals("N") || input.equals("n")) {
                // New
                createSpecies(this.input, this.output);
            } else if (input.equals("L") || input.equals("l")) {
                // Load
                loadSpecies();
            } else {
                createNew = false;
                System.out.println(" Input is not valid - repeat");
            }
        }

        System.out.println("Pool is created.");

    }



    public int getMaxNumberOfSpecies () {
        return this.maxSpecies;
    }


    public Species getSpecie(int index) {
        return this.mySpecies.get(index);
    }


    public void setSpecie(Species newSpecies) {
        this.mySpecies.add(newSpecies);
    }


    public void killSpecies(Species newSpecies) {
        if (this.mySpecies.contains(newSpecies)) {
            // Delete if existing
            this.mySpecies.remove(newSpecies);
            if (this.actualSpecies >= this.mySpecies.size()) {
                // If deleted was last in Row -> go to first
                this.actualSpecies = 1;
            }
        }
    }


    public int getGeneration () {
        return this.myGeneration;
    }


    public void increaseGeneration() {
        this.myGeneration++;
    }


    public int getInnovation () {
        return this.myInnovation;
    }


    public void increaseInnovation () {
        this.myInnovation++;
    }


    public int getGenomCounter () {
        return this.actualGenome;
    }


    public void increaseGenomCounter () {
        this.actualGenome++;
    }


    public int getActualFrame () {
        return this.actualFrame;
    }


    public void setActualFrame (int newFrame) {
        this.actualFrame = newFrame;
    }


    public int getMaxFitness () {
        return this.maxFitness;
    }


    private void setMaxFitness (int newFitness) {
        this.maxFitness = newFitness;
    }


    public void insertNewFitness (int newFitness) {
        if (newFitness > this.maxFitness) {
            setMaxFitness(newFitness);
        }
    }



    public void timeToMutate () {
        //	loop all species
        LinkedList <Genom> allGenomes = new LinkedList <Genom>();
        for (Species b : this.mySpecies) {
            for (Genom c : b.getAllGenomes()) {
                allGenomes.add(c);
            }
        }

        // sort by adjustedFitness
        Collections.sort(allGenomes);	// descending

        // delete bad performers
        int toDelete = (int) (allGenomes.size() * 0.1);
        if ( allGenomes.size() > this.maxSpecies )	{ toDelete = ( allGenomes.size() - this.maxSpecies ) + (int) (this.maxSpecies * 0.1); }

        Genom actual;
        for (int i = 0; i < (toDelete); i++) {
            actual = allGenomes.pollLast();
            // delete all references
            int index = -2;
            for (Species b : this.mySpecies) {
                index = b.hasGenome(actual);
                if (index > -1 && index < b.getNumberOfGenomes()) {
                    // found
                    b.freeGenome(index);
                    break;
                }
            }
            // Delete object
            actual = null;
        }

        // Mutate
        for (Species b : this.mySpecies) {
            b.mutate();
            // Check if mutated are complying the rules
            b.checkAllowed( this.maxNodes, this.maxDepth);
        }
    }


    public void createSpecies (Neuron [] input, int output) {
        int startNumber = 10;

        if (startNumber <= 0) {
            startNumber = 10;
        } else if (startNumber > this.maxSpecies) {
            // nothing - startpool can be bigger until first measurement is done
        } else if ( (startNumber+10) < this.maxSpecies) {
            startNumber = ( this.maxSpecies - startNumber );
        }

        Species temp_Species;
        for (int i = 0; i < startNumber; i++ ) {
            temp_Species = new Species (output);
            temp_Species.setInAndOut(this.Incoming);
            // add to already created
            this.mySpecies.add(temp_Species);
        }
    }


    public void loadSpecies () {

        // Implement();

        String lastPool = RW.load();
        // bottom up

        // 1st	neurons
        // 2nd	genes		incl. links
        // 3rd	Genom		jooo

    }



    public void savePool() {

        // Debug();

        // Top down

        // get Pool-Data
        String thisPool = meToString();

        // get Species-Data
        String thisSpecies = speciesToString();

        // csv
        RW.save( thisPool + thisSpecies);
    }


    public String meToString() {
        String myReturn;
        // o = Object
        // i = information
        // d = data

        // general Information
        String Object = "o; Pool;";
        String Info = "i; maxSpecies; maxOutputs; maxNodes; maxDepth; myGeneration; myInnovation; actualSpecies; actualGenome;  actualFrame; maxFitness; Output;" + "\n";
        String Data = "d;" + maxSpecies + ";" + maxOutputs + ";" + maxNodes + ";" + maxDepth + ";" + myGeneration + ";" + myInnovation + ";" + actualSpecies + ";" + actualGenome + ";" +  actualFrame + ";" + maxFitness + ";" + output + ";" + ";" + "\n";
        myReturn = Object + Info + Data;

        // List with Species
        Info = "i; SpeciesNumber; Listing" + "\n";
        Data = "d;" + this.mySpecies.size() + ";";
        for (Species b : this.mySpecies) {
            Data += b.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        // Input
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.Incoming.size() + ";";
        for (Neuron b : this.Incoming) {
            Data += b.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        return myReturn;
    }


    private String speciesToString() {
        String myReturn = "";

        for (Species b : this.mySpecies) {
            myReturn = b.meToString();
        }

        return myReturn;
    }


}


