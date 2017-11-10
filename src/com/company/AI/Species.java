package com.company.AI;




        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.UUID;



public class Species {

    // is part of a Pool (many Species)

    // represents a bunch of Genoms (bunch of individuals)


    // - - - Variables
    UUID myID = UUID.randomUUID();
    private LinkedList <Neuron> Incoming;
    private LinkedList <Neuron> Outgoing;
    private ArrayList <Genom> myGenome;
    private int topFitness;
    private int staleness;
    private int averageFitness;
    // - - - - - - - -



    public Species (int outputs) {
        // New List to put Genoms in
        this.myGenome = new ArrayList <Genom> ();

        // Highest Fitness reached
        this.topFitness = 0;

        // Performed trainings (runs) of Species [in cycluses]
        this.staleness = 0;

        // AverageFitness through all trainings (runs) [staleness]
        this.averageFitness = 0;

        this.Incoming = new LinkedList <Neuron> ();

        this.Outgoing = new LinkedList <Neuron> ();
        for (int i = 0; i < outputs; i++) {
            this.Outgoing.add( new Neuron() );
        }

    }



    public int getMySize() {
        // number of Neurons
        int answer = 0;
        for (Genom g: this.myGenome) {
            answer = answer + g.getMySize();
        }
        return answer;
    }

    public int getNumberOfGenomes () {
        return this.myGenome.size();
    }


    public int hasGenome (Genom searched) {
        int myReturn = -1;

        int i = 0;
        for (Genom c : this.myGenome) {
            if (c.myID == searched.myID) {
                myReturn = i;
                break;
            }
            i++;
        }

        return myReturn;
    }


    public void freeGenome (int index) {
        this.myGenome.remove(index);
    }


    public Genom getGenome (int index) {
        return this.myGenome.get(index);
    }


    public void setGenome (Genom newGenome) {
        this.myGenome.add(newGenome);
    }


    public ArrayList <Genom> getAllGenomes () {
        return this.myGenome;
    }


    public int getTopFitness () {
        return this.topFitness;
    }


    public void insertNewFitness (int newFitness) {
        if (newFitness > this.topFitness) {
            setTopFitness(newFitness);
        }
        this.staleness++;
        adjustFitness(newFitness);
    }


    private void setTopFitness (int newFitness) {
        setTopFitness(newFitness);
    }


    public int getStalness() {
        return this.staleness;
    }


    public int getAverageFitness() {
        return this.averageFitness;
    }

    public int size() {
        return this.myGenome.size();
    }




    private void adjustFitness (int newFitness) {
        if (this.staleness == 0) {
            System.out.println("Error - b_Species - staleness == 0 in adjustFitness - newFitness =" + newFitness);
        } else if (this.staleness == 1) {
            this.averageFitness = newFitness;
        } else {
            double oldFitnessAdjusted = (this.averageFitness / (this.staleness)) * (this.staleness-1);
            double newFitnessAdjusted = (newFitness / this.staleness);
            this.averageFitness = (int) ( oldFitnessAdjusted + newFitnessAdjusted );
        }
    }



    public void setInAndOut (LinkedList <Neuron> in) {
        this.Incoming = in;

        Genom firstGenome = new Genom();
        firstGenome.setInAndOut ( Incoming, Outgoing );
        firstGenome.createNewGene();

        myGenome.add( firstGenome );
    }


    public LinkedList <Neuron> getIncoming () {
        return this.Incoming;
    }


    public LinkedList <Neuron> getOutgoing () {
        return this.Outgoing;
    }


    public void setID (UUID newID) {
        this.myID = newID;
    }

    public UUID getID () {
        return this.myID;
    }



    public String SpiecieToString () {
        return ( meToString() + genomesToString());
    }


    public String meToString() {
        String myReturn;
        // i = information
        // d = data

        // general Information
        String Object = "o; Species;";
        String Info = "i; topFitness; staleness; averageFitness; myID;" + "\n";
        String Data = "d;" + topFitness + ";" + staleness + ";" + averageFitness + ";" + ";" + myID + ";" + "\n";
        myReturn = Object + Info + Data;

        // List with Neurons
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.Incoming.size() + ";";
        for (Neuron e : this.Incoming) {
            Data = Data + e.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;


        // List with Neurons (Outgoing) = same as Gene
        // - nothing to store -


        // List with Genome
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.myGenome.size() + ";";
        for (Genom c : this.myGenome) {
            Data = Data + c.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        return myReturn;
    }


    private String genomesToString() {
        String myReturn = "";

        for (Genom c : this.myGenome) {
            myReturn = c.genomToString();
        }

        return myReturn;
    }


    public void mutate () {
        // copy all Genomes
        LinkedList <Genom> newGs = new LinkedList <Genom> ();
        Genom newG;
        for (Genom c : this.myGenome) {
            // copy
            newG = getCopy( c );
            // mutate all copied Genes (newGs)
            newG.mutate();
            // If allowed (not null)
            if (! (newG == null) ) {
                newGs.add( newG );
            }
        }
    }


    public void checkAllowed (int maxNodes, int maxDepth) {
        for (Genom c : this.myGenome) {
            if (! c.getAllowedStructure() ) {
                if (! (c.isAllowed(maxNodes, maxDepth) )) {
                    this.myGenome.remove(c);
                    c = null;
                }
            }
        }
    }




    public Genom getCopy (Genom old) {
        Genom newG = new Genom();
        newG.setMutateConnectionsChance(old.getMutateConnectionsChance());
        newG.setLinkMutationChance(old.getLinkMutationChance());
        newG.setBiasMutationChance(old.getBiasMutationChance());
        newG.setNodeMutationChance(old.getNodeMutationChance());
        newG.setEnableMutationChance(old.getEnableMutationChance());
        newG.setDisableMutationChance(old.getDisableMutationChance());
        newG.setStepSize(old.getStepSize());
        newG.setInAndOut(old.getIncoming() , old.getOutgoing() );
        newG.setGenes(old.CopyGenes());


        return newG;
    }
}
