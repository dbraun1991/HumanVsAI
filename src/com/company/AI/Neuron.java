package com.company.AI;




        import sun.awt.image.ImageWatched;

        import java.util.Hashtable;
        import java.util.LinkedList;
        import java.util.UUID;



public class Neuron {

    // is lead by the Gene and the Genes mutation chances

    // represents a Neuron with its links to superior cells

    // - - - Variables - - -
    UUID myID = UUID.randomUUID();

    LinkedList <Neuron> incoming;
    LinkedList <Neuron> outgoing;

    private double myValue;
    private boolean computed;
    private int myLayer;

    private double myBias;
    private double biasChange;
    // - - - - - - - -



    public Neuron () {
        this.setValue(0.0);
        this.computed = false;
        this.myLayer = -1;
        this.myBias = 0.1;
        this.biasChange = 0.0;
        this.incoming = new LinkedList <Neuron>();
        this.outgoing = new LinkedList <Neuron>();
    }


    public double getValue() {
        return myValue;
    }


    public double getComputedValue() {
        compute();
        return myValue;
    }

    public void setValue(double newValue) {
        // represents value between		-1  and  1
        this.myValue = newValue;
    }


    public int getLayer() {
        return this.myLayer;
    }


    public void setlayer(int layer) {
        this.myLayer = layer;
    }


    public double getBias() {
        return this.myBias;
    }


    public void setBias(double Bias) {
        this.myBias = Bias;
    }


    public double getBiasChange() {
        return this.biasChange;
    }


    public void setBiasChange(double BiasChange) {
        this.biasChange = BiasChange;
    }


    public LinkedList<Neuron> getIncoming() { return this.incoming; }


    public void raiseBias() {
        if ( (this.myBias + this.biasChange) > 0.7) {
            this.setBias(0.7);
        } else {
            this.setBias(this.myBias + this.biasChange);
        }
    }


    public void sinkBias() {
        if ( (this.myBias - this.biasChange) < -0.7) {
            this.setBias(-0.7);
        } else {
            this.setBias(this.myBias - this.biasChange);
        }
    }


    public void raiseBiasChange() {
        if ( (this.biasChange * 1.1) > 0.3) {
            this.setBias(0.3);
        } else {
            this.setBias(this.biasChange * 1.1);
        }
    }


    public void sinkBiasChange() {
        if ( (this.myBias * 0.9) < 0.0001) {
            this.setBias(0.0001);
        } else {
            this.setBias(this.myBias * 0.9);
        }
    }



    public void computeLayer () {
        int newL = -1;

        // search for Layer
        if (this.incoming.size()<1) {
            this.setlayer(-1);
        } else {
            for (Neuron e : this.incoming) {
                if (e.getLayer()+1 > newL) {
                    newL = e.getLayer()+1;
                }
            }
            // compare with existing entry
            if (!(this.getLayer() == newL)) {
                // change entry
                this.setlayer(newL);
                // update following
                for (Neuron e : this.outgoing) {
                    e.computeLayer();
                }
            }
        }
    }


    public void compute () {
        double newValue= 0.0;

        int i = 0;

        for (Neuron n : incoming) {
            if (! n.computed) {
                n.compute();
            }
            newValue = newValue + n.getValue();
            i++;
        }
        if ( i == 0 ) {
            newValue = 0;
        } else {
            newValue = newValue / i;
            newValue = ( Math.tanh(newValue) * 1.31303 ) ;
        }

        // set the new computed Value
        this.setValue(newValue);
        this.computed = true;
    }


    public void updateIncoming (Neuron update, boolean addDelete) {
        if (addDelete) {
            // add
            this.incoming.add(update);
        } else {
            // delete;
            this.incoming.remove(update);
        }
    }


    public void updateOutgoing (Neuron update, boolean addDelete) {
        if (addDelete) {
            // add
            this.outgoing.add(update);
        } else {
            // delete;
            this.outgoing.remove(update);
        }
    }


    public void setUncomputed () {
        this.computed = false;
    }



    public void setID (UUID newID) {
        this.myID = newID;
    }


    public UUID getID () {
        return this.myID;
    }



    public String neuronToString () {
        return meToString();
    }


    public String meToString() {
        String myReturn;
        // i = information
        // d = data

        // general Information
        String Object = "o; Pool;";
        String Info = "i; myValue; computed; myID;" + "\n";
        String Data = "d;" + myValue + ";" + computed + ";" + myID + ";" + "\n";
        myReturn = Object + Info + Data;

        // List with Neurons (Outgoing)
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.incoming.size() + ";";
        for (Neuron e : this.incoming) {
            Data = Data + e.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        // List with Neurons (inside)
        Info = "i; InputNumber; Listing" + "\n";
        Data = "d;" + this.outgoing.size() + ";";
        for (Neuron e : this.outgoing) {
            Data = Data + e.getID() + ";";
        }
        Data = Data  + "\n" ;
        myReturn = myReturn + Info + Data;

        return myReturn;
    }


    public Neuron getCopy (Hashtable <UUID, Neuron> ht) {
        Neuron newN = ht.get(this.myID);
        // values
        newN.setValue(this.myValue);
        newN.setUncomputed();
        newN.setlayer(this.myLayer);
        // lists
        for (Neuron e : this.incoming) {
            newN.updateIncoming(ht.get(e.getID()), true);
        }
        for (Neuron e : this.outgoing) {
            newN.updateOutgoing(ht.get(e.getID()), true);
        }

        return newN;
    }




}

