package com.company.AI;




        import java.util.ArrayList;
        import java.util.Hashtable;
        import java.util.LinkedList;
        import java.util.UUID;



public class Gene {

    // is lead by the Genom and the Genom's mutation chances

    // represents a bunch of links

    // - - - Variables - - -
    UUID myID = UUID.randomUUID();

    private LinkedList <Neuron> Incoming;
    private LinkedList <Neuron> Outgoing;

    private ArrayList <Neuron> into;    // Neurons that are somehow influencing one of myNeurons
    private ArrayList <Neuron> out;     // Neurons that are somehow influencing out
    private ArrayList <Neuron> myNeurons;
    private double weight;
    private boolean enabled;
    private int innovation;
    private int layers;
    // - - - - - - - -



    public Gene () {

        // Neuron - (1st from createFirstConnection)
        this.into = new ArrayList<Neuron>();

        // Neuron - (1st from createFirstConnection)
        this.out = new ArrayList<Neuron>();

        // Neuron - all belonging to this gene
        this.myNeurons = new ArrayList<Neuron>();

        // Amount which is send to next.
        this.weight = 0.0;

        // Shows, if it can mutate or not
        this.enabled = true;

        // Times copied & mutated
        this.innovation = 0;

        // layers of connected nodes
        this.layers = -1;

    }



    public Gene getCopy () {
        Gene newG = new Gene();

        newG.setWeight(this.weight);
        newG.setenabled(this.enabled);
        if (this.innovation < Integer.MAX_VALUE-10) this.innovation++;
        newG.setInAndOut(Incoming, Outgoing);
        newG.setInto(this.into);
        newG.setInAndOut(Incoming, Outgoing);
        updateLayers();
        newG.setLayers(this.layers);

        // copy out
        ArrayList <Neuron> newOut = new ArrayList <Neuron> ();
        ArrayList <Neuron> newMyNeurons = new ArrayList <Neuron> ();
        Neuron tempN;
        // Hashtable ( UUIDs (out & myneurons) , new Neurons )
        // out = alle mit (max layer)+1

        // represent all Knots
        Hashtable <UUID, Neuron> ht = new Hashtable <UUID, Neuron> ();
        for (Neuron e : this.out) {
            e.setlayer(this.layers+1);
            tempN = new Neuron();
            newOut.add(tempN);
            ht.put(e.getID(), tempN);
        }
        for (Neuron e : this.myNeurons) {
            ht.put(e.getID(), new Neuron());
            tempN = new Neuron();
            newMyNeurons.add(tempN);
            ht.put(e.getID(), tempN);
        }
        for (Neuron e : this.into) {
            ht.put(e.getID(), e);
        }

        // picture of all Knots (incl. Links)
        for (Neuron e : this.out) {
            e.getCopy(ht);
        }
        for (Neuron e : this.myNeurons) {
            e.getCopy(ht);
        }
        for (Neuron e : this.into) {
            e.getCopy(ht);
        }

        // Finalize
        for (Neuron e : this.myNeurons) {
            if (e.getLayer() == 1) {
                e.computeLayer();
            }
        }

        newG.setInto(this.into);
        newG.setOut(newOut);
        newG.setMyNeurons(newMyNeurons);
        newG.updateLayers();
        return newG;
    }


    public int getInnovation() {
        return innovation;
    }

    public void setWeight (double newWeight) {
        this.weight = newWeight;
    }

    public boolean getenabled() {
        return this.enabled;
    }

    public void setenabled (boolean newEnab) {
        this.enabled = newEnab;
    }

    public void setInnovation(int inno) {
        this.innovation = inno;
    }


    public void adjustInnovation() {
        this.innovation++;
    }


    public int getMySize() {
        return myNeurons.size();
    }


    public void setInto(ArrayList <Neuron> newInto) {
        this.into = newInto;
    }

    public void setOut(ArrayList <Neuron> newOut) {
        this.out = newOut;
    }

    public void setMyNeurons(ArrayList <Neuron> newMyNeurons) {
        this.myNeurons = newMyNeurons;
    }

    public void setLayers (int newLayers) {
        this.layers = newLayers;
    }

    public void updateLayers () {
        int newL = -1;

        for (Neuron e : this.out) {
            if (e.getLayer()>newL) {
                newL = e.getLayer();
            }
        }
        setLayers(newL);

        // All "out" = max layers
        for (Neuron n : this.getOutgoing()) {
            n.setlayer(this.layers);
        }
    }

    public int getlayerDepth () {
        return this.layers;
    }


    public int getNeuronsOnLayerCount (int layer) {
        if (layer > getlayerDepth()) {
            return 0;
        } else {
            int sum = 0;
            for (Neuron n : myNeurons) {
                if (n.getLayer() == layer) {
                    sum++;
                }
            }
            return sum;
        }
    }

    public LinkedList<Neuron> getNeuronsOnLayer(int layer) {
        LinkedList<Neuron> myReturn = new LinkedList<>();
        if (!(layer > getlayerDepth() )) {
            for (Neuron n : myNeurons) {
                if (n.getLayer() == layer && !(myReturn.contains(n)) ) {
                    myReturn.add(n);
                }
            }
        }
        return myReturn;
    }



    public void createFirstConnection () {
        // set link
        // input matrix --> pick random
        // output matrix -->  pick random

        // pick input
        Neuron start = pickRandom(this.Incoming);
        this.into.add(start);
        // pick output
        Neuron end = pickRandom(this.Outgoing);
        this.out.add(end);

        // create neuron in Gene
        Neuron firstLink = new Neuron ();
        this.myNeurons.add(firstLink);

        // Neuron listens on input (=start)
        connectNodes(start, firstLink);

        // Output(=end) listens on Neuron
        connectNodes(firstLink, end);

        // Finalize
        reComputeLayers();

        // calculate Depth
        updateLayers();
    }


    public Neuron pickRandom (LinkedList <Neuron> source) { return source.get( (int)( Math.random() * 1000 ) % source.size() ); }



    public void setInAndOut (LinkedList <Neuron> in, LinkedList <Neuron> out) {
        this.Incoming = in;
        this.Outgoing = out;
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




    public void newLink () {
        // mutate on mutateConnectionsChance (new Link appears)
        for (Neuron e : this.out) {
            e.setlayer(this.layers+1);
        }

        // prepare startpoints
        ArrayList <Neuron> neurons = new ArrayList <Neuron> ();



        // wtf; // less copy paste - as one list	// extracting out of list is not possible  // null ?!


        for (Neuron e : this.Incoming) {
            if ( this.into.indexOf(e) < 0 ) {
                neurons.add(e);
            }
        }
        for (Neuron e : this.myNeurons) {
            neurons.add(e);
        }
        // pick one start
        Neuron newStart = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );


        // prepare endpoints
        for (Neuron e : neurons) {
            if ( e.getLayer() < newStart.getLayer()) {
                neurons.remove(e);
            }
        }
        for (Neuron e : this.out) {
            neurons.add(e);
        }
        // pick one end
        Neuron newEnd = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );


        // Link the Nodes
        connectNodes(newStart,newEnd);

        // Finalize
        for (Neuron e : this.myNeurons) {
            if (e.getLayer() == 1) {
                e.computeLayer();
            }
        }
    }


    public boolean linkChange () {
        boolean success = true;
        // mutate on linkMutationChance (existing link changes)
        ArrayList <Neuron> neurons = new ArrayList <Neuron> ();
        for (Neuron e : this.into) {
            neurons.add(e);
        }
        for (Neuron e : this.myNeurons) {
            neurons.add(e);
        }
        for (Neuron e : this.out) {
            neurons.add(e);
        }
        // pick one
        Neuron Point1 = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );
        Neuron oldPoint2;
        Neuron newPoint2;

        if (Math.random() > 0.5) {
            // to incoming

            // incoming
            if (Point1.incoming.size() == 0) {
                // nothing happens
            } else {
                // choose random incoming node = change link there
                oldPoint2 = Point1.incoming.get( (int)( (Math.random()*100)%neurons.size()) );

                // reduce linkable
                for (Neuron e : neurons) {
                    if ( ( e.getLayer() > Point1.getLayer() ) ) {
                        neurons.remove(e);
                    }
                }

                // extend linkable
                for (Neuron e : this.Incoming) {
                    if ( ( e.getLayer() < Point1.getLayer() ) && ( neurons.indexOf(e)<0 ) ) {
                        neurons.add(e);
                    }
                }
                newPoint2 = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );

                // break old Link
                disconnectNodes(oldPoint2,Point1);
                this.checkDeletionToFront(oldPoint2);

                // form new Link
                connectNodes(newPoint2,Point1);

                // check if broken
                success = ! ( this.isBrokenToFront() );
            }

        } else {
            // outgoing

            // incoming
            if (Point1.outgoing.size() == 0) {
                // nothing happens
            } else {
                // choose random outgoing node = change link there
                oldPoint2 = Point1.outgoing.get( (int)( (Math.random()*100)%neurons.size()) );

                // reduce linkable
                for (Neuron e : neurons) {
                    if ( ( e.getLayer() > Point1.getLayer() ) ) {
                        neurons.remove(e);
                    }
                }

                // extend linkable
                for (Neuron e : this.Outgoing) {
                    if ( ( e.getLayer() > Point1.getLayer() ) && ( neurons.indexOf(e)<0 ) ) {
                        neurons.add(e);
                    }
                }
                newPoint2 = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );

                // break old Link
                disconnectNodes(Point1, oldPoint2);
                this.checkDeletionToEnd(oldPoint2);

                // form new Link
                connectNodes(Point1, newPoint2);

                // check if broken
                success = ! ( this.isBrokenToEnd() );
            }

        }
        return success;
    }


    public void checkDeletionToFront (Neuron end) {

        if (end.outgoing.size() == 0) {
            // no followers = not used --> delete

            // delete links to predecessors
            for (Neuron e : end.incoming) {
                disconnectNodes(e, end);
                this.checkDeletionToFront(e);
            }

            if (this.Incoming.indexOf(end) < 0) {
                // not in List Incoming
                this.myNeurons.remove(end);
                end = null;
            }
        }
    }


    public void checkDeletionToEnd (Neuron start) {

        if (start.incoming.size() == 0) {
            // no predecessors = not used --> delete
            // delete links to followers
            for (Neuron e : start.outgoing) {
                disconnectNodes(start, e);
                this.checkDeletionToEnd(e);
            }

            if (this.Outgoing.indexOf(start) < 0) {
                // not in List: Outgoing
                this.myNeurons.remove(start);
                start = null;
            }
        }
    }


    public boolean isBrokenToFront () {
        boolean broken = true;

        for (Neuron e : this.into) {
            broken = this.searchOutToEnd(e);
            if (!broken) break;
        }

        return broken;
    }


    public boolean isBrokenToEnd () {
        boolean broken = true;

        for (Neuron e : this.out) {
            broken = this.searchOutToFront(e);
            if (!broken) break;
        }

        return broken;
    }


    public boolean searchOutToEnd (Neuron start) {
        boolean foundOut = false;

        for (Neuron e : start.outgoing) {
            if (! (this.out.indexOf(e) < 0) ) {
                // linked node is in list "out"
                foundOut = true;
                break;
            } else {
                // search on
                foundOut = this.searchOutToEnd(e);
                break;
            }
        }
        return foundOut;
    }


    public boolean searchOutToFront (Neuron end) {
        boolean foundOut = false;

        for (Neuron e : end.incoming) {
            if (! (this.into.indexOf(e) < 0) ) {
                // linked node is in list "out"
                foundOut = true;
                break;
            } else {
                // search on
                foundOut = this.searchOutToFront(e);
                break;
            }
        }
        return foundOut;
    }


    public boolean deleteLink () {
        boolean success = true;
        // mutate on linkMutationChance (existing link changes)

        ArrayList <Neuron> neurons = new ArrayList <Neuron> ();
        for (Neuron e : this.into) {
            neurons.add(e);
        }
        for (Neuron e : this.myNeurons) {
            neurons.add(e);
        }
        for (Neuron e : this.out) {
            neurons.add(e);
        }
        // pick one
        Neuron Point1 = neurons.remove( (int)( (Math.random()*100)%neurons.size()) );
        Neuron Point2;

        if (Math.random() > 0.5) {
            // incoming
            if ( Point1.incoming.size() > 0) {
                Point2 = Point1.incoming.get ( (int)( (Math.random()*100)%Point1.incoming.size()) );
                disconnectNodes(Point1, Point2);
                this.checkDeletionToEnd(Point1);
                this.checkDeletionToFront(Point2);
            }
        } else {
            // outgoing
            if ( Point1.outgoing.size() > 0) {
                Point2 = Point1.outgoing.get ( (int)( (Math.random()*100)%Point1.outgoing.size()) );
                disconnectNodes(Point1, Point2);
                this.checkDeletionToEnd(Point2);
                this.checkDeletionToFront(Point1);
            }
        }

        return success;
    }


    public void biasChange () {
        for (Neuron e : this.myNeurons) {
            if ( Math.random() > (1-(1/(( this.myNeurons.size() +140)/100))) ) {
                // Function:	1-(1/((x+140)/100))
                //		x = 0 (not possible = skipped loop)
                //		x = 1		-->		0.29078
                //		x = 25		-->		0.39394
                // 		x = 50		-->		0.47368
                // 		x = 100		-->		0.58333
                // 		x = 1000	-->		0.91228
                // The more nodes, the less likely to adjust bias.
                if (Math.random() > 0.5) {
                    e.raiseBias();
                } else {
                    e.sinkBias();
                }
            } else {
                if (Math.random() > 0.5) {
                    e.raiseBiasChange();
                } else {
                    e.sinkBiasChange();
                }
            }
        }
    }


    public void nodeChange() {
        // mutate on nodeMutationChance (Node appears / disappears)
        Neuron middlePoint;
        Neuron Point2;
        double tmpRandom = 0.0;
        int tmpInt;

        int ins;
        int outs;

        for (Neuron e : this.out) {
            e.setlayer(this.layers+1);
        }

        if ( Math.random() > 0.5 ) {
            //	appear - just link
            middlePoint = new Neuron();

            // target layer - between ( 1 ) and ( max.layer-1 )
            middlePoint.setlayer( (int) ( Math.random() * ( this.layers-1 ) ) + 1 );

            // possible ins & outs
            ArrayList <Neuron> pIns = new ArrayList <Neuron> ();
            ArrayList <Neuron> pOuts = new ArrayList <Neuron> ();
            for (Neuron e : this.into) {
                pIns.add(e);
            }
            for (Neuron e : this.myNeurons) {
                if (e.getLayer() < middlePoint.getLayer()) {
                    pIns.add(e);
                } else if (e.getLayer() > middlePoint.getLayer()) {
                    pOuts.add(e);
                }
            }
            for (Neuron e : this.out) {
                pOuts.add(e);
            }

            if (Math.random() > 0.5) {
                // ins  -  dependent on number of neurons	- between (1) and (3)
                if ( pIns.size() < 5 ) {
                    ins = 1;
                } else if (pIns.size() < 10) {
                    if (Math.random() < 0.7) {
                        ins = 1;
                    } else {
                        ins = 2;
                    }
                } else {
                    //   50 / 30 / 20 share
                    tmpRandom = Math.random();
                    if (tmpRandom < 0.5) {
                        ins = 1;
                    } else if (tmpRandom < 0.8) {
                        ins = 2;
                    } else {
                        ins = 3;
                    }
                }
                // ins out incoming & pIns
                for ( int i = 0; i < ins; i++) {
                    tmpInt = ( (int) Math.random() * (1000) ) % ( this.Incoming.size() + pIns.size()) ;
                    if (tmpInt < this.Incoming.size()) {
                        Point2 = this.Incoming.get(tmpInt);
                    } else {
                        Point2 = pIns.remove(tmpInt-this.Incoming.size());
                    }
                    if (!( Point2.outgoing.contains(middlePoint) )) {
                        connectNodes(Point2, middlePoint);
                    }
                }
            } else {
                // outs  -  dependent on number of neurons	- between (1) and (3)
                if ( pOuts.size() < 5 ) {
                    outs = 1;
                } else if (pOuts.size() < 10) {
                    if (Math.random() < 0.7) {
                        outs = 1;
                    } else {
                        outs = 2;
                    }
                } else {
                    //   50 / 30 / 20 share
                    tmpRandom = Math.random();
                    if (tmpRandom < 0.5) {
                        outs = 1;
                    } else if (tmpRandom < 0.8) {
                        outs = 2;
                    } else {
                        outs = 3;
                    }
                }
                for ( int i = 0; i < outs; i++) {
                    tmpInt = ( (int) Math.random() * (1000) ) % ( this.Outgoing.size() + pOuts.size()) ;
                    if (tmpInt < this.Outgoing.size()) {
                        Point2 = this.Outgoing.get(tmpInt);
                    } else {
                        Point2 = pOuts.remove(tmpInt-this.Outgoing.size());
                    }
                    if (!( Point2.outgoing.contains(middlePoint) )) {
                        connectNodes(Point2, middlePoint);
                    }
                }
            }
        } else {
            //	disappear - randomly link incomingS and outgoingS
            middlePoint = this.myNeurons.get( ((int) Math.random()*1000) % this.myNeurons.size() );
            LinkedList <Neuron> Front = middlePoint.incoming;
            LinkedList <Neuron> End = middlePoint.outgoing;
            Neuron F;
            Neuron E;

            // case more Front than End
            while ( Front.size() > End.size() ) {
                F = Front.remove( (int)(Math.random()*1000) % Front.size() );	// lower "Front"-Nodes
                E = End.get( (int)(Math.random()*1000) % End.size() );			// keep "End"-Nodes
                // delete Links
                disconnectNodes(F, middlePoint);
                disconnectNodes(middlePoint, E);

                // create new Link
                connectNodes(F,E);
            }

            // case more End than Front
            while ( Front.size() < End.size() ) {
                F = Front.get( (int)(Math.random()*1000) % Front.size() );		// keep "Front"-Nodes
                E = End.remove( (int)(Math.random()*1000) % End.size() );		// lower "End"-Nodes
                // delete Links
                disconnectNodes(F, middlePoint);
                disconnectNodes(middlePoint, E);

                // create new Link
                connectNodes(F,E);
            }

            // case same number
            while ( ( Front.size() > 0 ) && ( Front.size() == End.size() ) ) {
                F = Front.remove( (int)(Math.random()*1000) % Front.size() );		// keep "Front"-Nodes
                E = End.remove( (int)(Math.random()*1000) % End.size() );		// lower "End"-Nodes
                // delete Links
                disconnectNodes(F, middlePoint);
                disconnectNodes(middlePoint, E);

                // create new Link
                connectNodes(F,E);
            }

            middlePoint = null;

        }
        // Finalize
        reComputeLayers();
    }


    public void connectNodes(Neuron In, Neuron Out) {
        // test "belongs to Incoming"
        if ( this.Incoming.contains(In) && ( ! (this.into.contains(In)) ) ) {
            // another Neuron is already connected to this Incoming-Neuron
            this.into.add(In);
        }
        // test "belongs to Outgoing"
        if (this.Outgoing.contains(Out) && ( ! (this.out.contains(Out)) ) ) {
            // another Neuron is already connected to this Incoming-Neuron
            this.out.add(Out);
        }
        // test if already connected
        if (! (In.outgoing.contains(Out)) && (! (Out.incoming.contains(In)) ) ) {
            // connect
            In.updateOutgoing(Out, true);
            Out.updateIncoming(In, true);
        }
    }


    public void disconnectNodes(Neuron In, Neuron Out) {
        // test if connected
        if (In.outgoing.contains(Out) && (Out.incoming.contains(In)) ) {
            // connect
            In.updateOutgoing(Out, false);
            Out.updateIncoming(In, false);
        }
    }


    public void buildDown() {
        for (Neuron eM : this.myNeurons) {
            // eF => Front
            for (Neuron eF : eM.incoming) {
                disconnectNodes(eF, eM);
            }
            // eO => End
            for (Neuron eE : eM.outgoing) {
                disconnectNodes(eM, eE);
            }
            this.myNeurons.remove(eM);
            eM = null;
        }

        for (Neuron eO : this.out) {
            this.out.remove(eO);
            eO = null;
        }
    }



    public void reComputeLayers () {
        for (Neuron n : myNeurons) {
            n.computeLayer();
        }
    }



    public String geneToString () {
        return ( meToString() + neuronsToString());
    }


    public String meToString() {
        String myReturn;
        // i = information
        // d = data

        // general Information
        String Object = "o; Gene;";
        String Info = "i; weight; enabled; innovation; layers; myID;" + "\n";
        String Data = "d;" + weight + ";" + enabled + ";" + innovation + ";" + layers + ";" + myID + ";" + "\n";
        myReturn = Object + Info + Data;

        // List with Neurons (Incoming) = same as Species
        // - nothing to store -

        // List with Neurons (Outgoing)
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.out.size() + ";";
        for (Neuron e : this.out) {
            Data = Data + e.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        // List with Neurons (inside)
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.myNeurons.size() + ";";
        for (Neuron e : this.myNeurons) {
            Data = Data + e.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        return myReturn;
    }


    private String neuronsToString() {
        String myReturn = "";

        for (Neuron n : this.myNeurons) {
            myReturn = n.neuronToString();
        }

        return myReturn;
    }







}