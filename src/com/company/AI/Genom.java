package com.company.AI;




        import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.UUID;



public class Genom implements Comparable <Genom> {

    // is lead by the Species (Group = bunch of Genoms (individuals) )

    // represents an individual (bunch Genes)


    // - - - Variables - - -
    UUID myID = UUID.randomUUID();

    private LinkedList <Neuron> Incoming;
    private LinkedList <Neuron> Outgoing;
    private ArrayList <Gene> myGenes;
    private int myFitness;						// plain Fitness in game
    private int adjustedFitness;				// Fitness after judgeing parameters ( too many Neurons? too many Genes? too many steps? all outputs covered?)
    private int globalRank;						// Rank due to adjusted Fitness

    // private ArrayList <d_Gene> mutationRates;	// ???
    private double mutateConnectionsChance;
    private double linkMutationChance;
    private double biasMutationChance;
    private double nodeMutationChance;
    private double enableMutationChance;
    private double disableMutationChance;
    private double stepSize;
    private boolean allowedStructure;
    // - - - - - - - -



    public Genom () {
        // New List to put Genes in
        this.myGenes = new ArrayList <Gene> ();

        // actual Fitness
        this.setMyFitness(0);

        // actual adjusted Fitness
        this.setAdjustedFitness(0);

        // Rank over ALL Genoms
        this.setGlobalRank(0);


        // - - - mutationRates - - -

        // Chance for a Connection to mutate (Link disappears / new Link appears )
        this.mutateConnectionsChance = 0.25;

        // Chance for a Connection to mutate (in START or in END Node)
        this.linkMutationChance		= 2.0;

        // Chance for a Bias to mutate (rise or sink)
        this.biasMutationChance		= 0.4;

        // Chance for a Node to mutate (???)
        this.nodeMutationChance		= 0.5;

        // Chance to enable mutation
        this.enableMutationChance	= 0.2;

        // Chance to disable mutation
        this.disableMutationChance	= 0.4;

        // Step size to change weights
        this.stepSize = 0.1;

        // - - - mutationRates - - -

        this.allowedStructure = false;

    }



    public void setGenes (ArrayList <Gene> newGenes) {
        this.myGenes = newGenes;
    }

    public ArrayList <Gene> CopyGenes () {
        ArrayList <Gene> newGs = new ArrayList <Gene> ();

        for (Gene d : this.myGenes) {
            newGs.add(d.getCopy());
        }

        return newGs;
    }


    public double getMutateConnectionsChance () {
        return this.mutateConnectionsChance;
    }

    public void setMutateConnectionsChance (double newValue) {
        this.mutateConnectionsChance = newValue;
    }

    public void raiseMutateConnectionsChance () {
        this.mutateConnectionsChance = this.mutateConnectionsChance + this.stepSize;
        if (this.mutateConnectionsChance > 1) {
            this.mutateConnectionsChance = 1.0;
        }
    }

    public void sinkMutateConnectionsChance () {
        this.mutateConnectionsChance = this.mutateConnectionsChance - this.stepSize;
        if (this.mutateConnectionsChance < 0) {
            this.mutateConnectionsChance = 0.0;
        }
    }



    public double getLinkMutationChance () {
        return this.linkMutationChance;
    }

    public void setLinkMutationChance (double newValue) {
        this.linkMutationChance = newValue;
    }

    public void raiseLinkMutationChance () {
        this.linkMutationChance = this.linkMutationChance + this.stepSize;
        if (this.linkMutationChance > 1) {
            this.linkMutationChance = 1.0;
        }
    }

    public void sinkLinkMutationChance () {
        this.linkMutationChance = this.linkMutationChance - this.stepSize;
        if (this.linkMutationChance < 0) {
            this.linkMutationChance = 0.0;
        }
    }



    public double getBiasMutationChance () {
        return this.biasMutationChance;
    }

    public void setBiasMutationChance (double newValue) {
        this.biasMutationChance = newValue;
    }

    public void raiseBiasMutationChance () {
        this.biasMutationChance = this.biasMutationChance + this.stepSize;
        if (this.biasMutationChance > 1) {
            this.biasMutationChance = 1.0;
        }
    }

    public void sinkBiasMutationChance () {
        this.biasMutationChance = this.biasMutationChance - this.stepSize;
        if (this.biasMutationChance < 0) {
            this.biasMutationChance = 0.0;
        }
    }



    public double getNodeMutationChance () {
        return this.nodeMutationChance;
    }

    public void setNodeMutationChance (double newValue) {
        this.nodeMutationChance = newValue;
    }

    public void raiseNodeMutationChance () {
        this.nodeMutationChance = this.nodeMutationChance + this.stepSize;
        if (this.nodeMutationChance > 1) {
            this.nodeMutationChance = 1.0;
        }
    }

    public void sinkNodeMutationChance () {
        this.nodeMutationChance = this.nodeMutationChance - this.stepSize;
        if (this.nodeMutationChance < 0) {
            this.nodeMutationChance = 0.0;
        }
    }



    public double getEnableMutationChance () {
        return this.enableMutationChance;
    }

    public void setEnableMutationChance (double newValue) {
        this.enableMutationChance = newValue;
    }

    public void raiseEnableMutationChance () {
        this.enableMutationChance = this.enableMutationChance + this.stepSize;
        if (this.enableMutationChance > 1) {
            this.enableMutationChance = 1.0;
        }
    }

    public void sinkEnableMutationChance () {
        this.enableMutationChance = this.enableMutationChance - this.stepSize;
        if (this.enableMutationChance < 0) {
            this.enableMutationChance = 0.0;
        }
    }



    public double getDisableMutationChance () {
        return this.disableMutationChance;
    }

    public void setDisableMutationChance (double newValue) {
        this.disableMutationChance = newValue;
    }

    public void raiseDisableMutationChance () {
        this.disableMutationChance = this.disableMutationChance + this.stepSize;
        if (this.disableMutationChance > 1) {
            this.disableMutationChance = 1.0;
        }
    }

    public void sinkDisableMutationChance () {
        this.disableMutationChance = this.disableMutationChance - this.stepSize;
        if (this.disableMutationChance < 0) {
            this.disableMutationChance = 0.0;
        }
    }



    public double getStepSize () {
        return this.stepSize;
    }

    public void setStepSize (double newValue) {
        this.stepSize = newValue;
    }

    public void raiseStepSize () {
        this.stepSize = this.stepSize * (1.1);
        if (this.stepSize > 0.4) {
            this.stepSize = 0.4;
        }
    }

    public void sinkStepSize () {
        this.stepSize = this.stepSize * 0.9;
        if (this.stepSize < 0) {
            this.stepSize = 0.0;
        }
    }


    public int size() {
        return this.myGenes.size();
    }

    public Gene getGene (int index) {
        return this.myGenes.get(index);
    }

    public int getlayerDepth () {
        int max = 0;
        for (Gene g : myGenes) {
            if (g.getlayerDepth() > max) {
                max = g.getlayerDepth();
            }
        }
        return max;
    }



    public Genom getCopy () {
        Genom myCopy = new Genom();

        myCopy.setMutateConnectionsChance(this.mutateConnectionsChance);
        myCopy.setLinkMutationChance(this.linkMutationChance);
        myCopy.setBiasMutationChance(this.biasMutationChance);
        myCopy.setNodeMutationChance(this.nodeMutationChance);
        myCopy.setEnableMutationChance(this.enableMutationChance);
        myCopy.setDisableMutationChance(this.disableMutationChance);
        myCopy.setStepSize(this.stepSize);
        myCopy.setGenes(CopyGenes());

        return myCopy;
    }



    public int getMyFitness() {
        return myFitness;
    }



    public void setMyFitness(int newFitness) {
        this.myFitness = newFitness;
    }



    public int getGlobalRank() {
        return globalRank;
    }



    public void setGlobalRank(int newGlobalRank) {
        this.globalRank = newGlobalRank;
    }



    public int getAdjustedFitness() {
        return adjustedFitness;
    }



    public void setAdjustedFitness(int newAdjustedFitness) {
        this.adjustedFitness = newAdjustedFitness;
    }



    public int getMySize() {
        int answer = 0;
        for (Gene d : this.myGenes) {
            answer = answer + d.getMySize();
        }
        return answer;
    }



    public void setInAndOut (LinkedList <Neuron> in, LinkedList <Neuron> out) {
        this.Incoming = in;
        this.Outgoing = out;
    }



    public void createNewGene () {
        Gene firstGene = new Gene();
        firstGene.setInAndOut ( Incoming, Outgoing );
        firstGene.createFirstConnection();
        myGenes.add( firstGene );
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


    public boolean isAllowed (int maxNodes, int maxDepth) {
        boolean allowed = true;
        int myNodes = getMySize();
        if (! (myNodes > maxNodes) ) {

            // collect data
            for (Gene d : this.myGenes) {

                if (d.getlayerDepth() > maxDepth) {
                    allowed = false;

                    break;
                }
            }
        } else {
            allowed = false;
        }

        if (! (allowed)) buildDown();

        return allowed;
    }


    public void setAllowedStructure (boolean allowed) {
        this.allowedStructure = allowed;
    }


    public boolean getAllowedStructure () {
        return this.allowedStructure;
    }



    public String genomToString () {
        return ( meToString() + genesToString());
    }


    public String meToString() {
        String myReturn;
        // i = information
        // d = data

        // general Information
        String Object = "o; Genom;";
        String Info = "i; myFitness; adjustedFitness; globalRank; mutateConnectionsChance; linkMutationChance; biasMutationChance; nodeMutationChance; enableMutationChance; disableMutationChance; stepSize; myID;" + "\n";
        String Data = "d;" + myFitness + ";" + adjustedFitness + ";" + globalRank + ";" + mutateConnectionsChance + ";" + linkMutationChance + ";" + biasMutationChance + ";" + nodeMutationChance + ";" + enableMutationChance + ";" + disableMutationChance + ";" + stepSize + ";" + myID + ";" + "\n";
        myReturn = Object + Info + Data;

        // List with Neurons (Incoming) = same as Specie
        // - nothing to store -

        // List with Neurons (Outgoing) = same as Gene
        // - nothing to store -


        // List with Genes
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.myGenes.size() + ";";
        for (Gene d : this.myGenes) {
            Data = Data + d.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        return myReturn;
    }


    private String genesToString() {
        String myReturn = "";

        for (Gene d : this.myGenes) {
            myReturn = d.geneToString();
        }

        return myReturn;
    }


    public int compareTo(Genom b) {
        // descending
        return (b.getAdjustedFitness() - this.adjustedFitness);
    }



    public Genom mutate () {
        this.myFitness = -1;
        this.adjustedFitness = -1;

        // one out of 6 should mutate anyway

        for (Gene d : this.myGenes) {

            if (d.getenabled()) {
                // mutate on mutateConnectionsChance (new Link appears)
                if ( Math.random() * this.mutateConnectionsChance > 0.5 ) {
                    if ( Math.random() > 0.3) {
                        d.newLink();
                    } else {
                        d.deleteLink();
                        if (d.isBrokenToEnd()) {
                            // linkChange broke structure
                            this.myGenes.remove(d);
                            d=null;
                        }
                    }
                } else {
                    if ( Math.random() > 0.5) {
                        this.raiseMutateConnectionsChance();
                    } else {
                        this.sinkMutateConnectionsChance();
                    }
                }

                if (! (d == null)) {


                    // mutate on linkMutationChance (existing link changes)
                    if ( Math.random() * this.linkMutationChance > 0.5 ) {
                        if (! (d.linkChange()) ) {
                            // linkChange broke structure
                            d.deleteLink();
                            if (d.isBrokenToEnd()) {
                                // linkChange broke structure
                                this.myGenes.remove(d);
                                d=null;
                            }
                        }
                    } else {
                        if ( Math.random() > 0.5) {
                            this.raiseLinkMutationChance();
                        } else {
                            this.sinkLinkMutationChance();
                        }
                    }


                    if (! (d == null)) {

                        // mutate on biasMutationChance (bias gets changed)
                        if ( Math.random() * this.biasMutationChance > 0.5 ) {
                            d.biasChange();
                        } else {
                            if ( Math.random() > 0.5) {
                                this.raiseBiasMutationChance();
                            } else {
                                this.sinkBiasMutationChance();
                            }
                        }

                        // mutate on nodeMutationChance (Node appears / disappears)
                        if ( Math.random() * this.nodeMutationChance > 0.5 ) {
                            d.nodeChange();
                        } else {
                            if ( Math.random() > 0.5) {
                                this.raiseNodeMutationChance();
                            } else {
                                this.sinkNodeMutationChance();
                            }
                        }

                        // mutate on enableMutationChance
                        if ( Math.random()  > 0.5 ) {
                            if ( Math.random() > 0.5) {
                                this.raiseEnableMutationChance();
                            } else {
                                this.sinkEnableMutationChance();
                            }
                        }

                        // mutate on disableMutationChance
                        if ( Math.random()  > 0.5 ) {
                            if ( Math.random() > 0.5) {
                                this.raiseDisableMutationChance();
                            } else {
                                this.sinkDisableMutationChance();
                            }
                        }

                    }

                }
                // mutate on stepSize
            }

            if (! (d == null)) {
                // disable mutation
                if (Math.random() * this.disableMutationChance > 0.5 ) {
                    d.setenabled(true);
                } else {
                    if (Math.random() * this.enableMutationChance > 0.5 ) {
                        d.setenabled(true);
                    }
                }
            }
        }
        return this;
    }


    public void buildDown(){
        for (Gene d : this.myGenes) {
            d.buildDown();
            this.myGenes.remove(d);
            d = null;
        }
    }






}