/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the neural network and builds its basic structure with neurons
 * and connections
 * @author Dominik Onyszkiewicz
 */
public class NeuralNet {
    private int iSize, oSize;
    private List<Integer> hSizes;  // multiple sizes for each hidden-layer
    // i know its bad to make attributes public, i am sorry :(
    public InputNeuron[] iLayer;
    public WorkingNeuron[] oLayer;
    public List<List<WorkingNeuron>> hLayers;
    private int fitness, id;
    private static int generalId = 0;
    
    /**
     * Creates a network of neurons with three layer with the given sizes
     * @param inputSize size of the input layer
     * @param hiddenSizes sizes of the hidden layers
     * @param outputSize size of the output layer
     */
    public NeuralNet(int inputSize, List<Integer> hiddenSizes, int outputSize) {
        iSize = inputSize;
        hSizes = hiddenSizes;
        oSize = outputSize;
        
        id = generalId;
        generalId++;
        
        fitness = 0;
        
        buildNetwork();
    }
    
    /**
     * Builds the general structure of the network with the given layer sizes
     * without connecting any neurons
     */
    public void buildNetwork() {
        
        iLayer = new InputNeuron[iSize];
        hLayers = new ArrayList<>();
        oLayer = new WorkingNeuron[oSize];
        
        for (int i = 0; i < iLayer.length; i++) {
            iLayer[i] = new InputNeuron(i, -1);
        }
        
        for(int hiddenLayer = 0; hiddenLayer < hSizes.size(); hiddenLayer++) {
            hLayers.add(new ArrayList<>());
            for (int h = 0; h < hSizes.get(hiddenLayer); h++) {
                hLayers.get(hiddenLayer).add(new WorkingNeuron(h, hiddenLayer));
            }
        }
        
        for (int o = 0; o < oSize; o++) {
            oLayer[o] = new WorkingNeuron(o, Integer.MAX_VALUE);
        }
    }
    
    /**
     * Connects the neurons of the different layers with every neuron of the next 
     * layer with random connection weights
     */
    public void connectNeuronsAllToAll() {
        // if there are neurons in the hidden layer
        if(hLayers.size() > 0) {
            // input to hidden layer
            for(Neuron i : iLayer) {
                for(Neuron h : hLayers.get(0)) {
                    i.addConnection(h);
                }
            }

            // every hidden to next hidden layer
            for(int hLayerIndex = 0; hLayerIndex < hLayers.size() - 1; hLayerIndex++) {
                for(WorkingNeuron neuron : hLayers.get(hLayerIndex)) {
                    for(WorkingNeuron target : hLayers.get(hLayerIndex + 1)) {
                        neuron.addConnection(target);
                    }
                }
            }

            // last hidden to output layer
            for(Neuron h : hLayers.get(hLayers.size() - 1)) {
                for(Neuron o : oLayer) {
                    h.addConnection(o);
                }
            }
        }
        // else: just connection all input neurons to all output neurons
        else {
            for(Neuron i : iLayer) {
                for(Neuron o : oLayer) {
                    i.addConnection(o);
                }
            }
        }
    }
    
    /**
     * Computes the output of the complete neural net by computing the outputs
     * of the seperate neurons and using them for the next layer calculation
     * @param input input values for the seperate input neurons
     * @return output of the neural net
     */
    public double[] computeNetOutput(double[] input) {
        if(input.length < iLayer.length) {
            System.out.println("missing inputs");
            return null;
        }
        
        // input layer output to hidden layer
        for (int i = 0; i < iLayer.length; i++) {
            ((InputNeuron) iLayer[i]).setInput(input[i]);
            iLayer[i].pushOutput();
        }
        
        // all hidden layers to the next hidden layer + to output layer
        for (int layer = 0; layer < hLayers.size(); layer++) {
            for(Neuron h : hLayers.get(layer)) {
                h.pushOutput();
            }
        }
        
        // save output layer outputs
        double[] output = new double[oSize];
        for (int o = 0; o < oLayer.length; o++) {
            output[o] = oLayer[o].activate();
        }
        
        return output;
    }
    
    /**
     * Increases the fitness of the neural net by a given amount
     * @param amount how much it should be increased
     */
    public void increaseFitness(int amount) {
        fitness += amount;
    }
    
    /**
     * Sets the neural nets fitness to a given value
     */
    public void setFitness(int value) {
        fitness = value;
    }
    
    /**
     * Returns the fitness of the neural net
     * @return fitness of the neural net
     */
    public int getFitness() {
        return fitness;
    }
    
    /**
     * Resets the fitness in order to being able to determine a new fitness
     */
    public void resetFitness() {
        fitness = 0;
    }
    
    /**
     * Creates a mutation of the neural network by slightly modifying the
     * connection weights and adding or deleting neurons or connections
     * TODO: Add a way to cross two networks
     * TODO: Test some different probabilities
     * @param maxDifference maximal difference of the new connection weight
     */
    public void mutate(double maxDifference) {
        Random r = new Random();
        
        // Probabilities
        double probAddNeuron = 0.6;
            double probNewLayerNeuron = 0.3;
        double probRemoveNeuron = 0.2;
        double probAddConnection = 0.8;
            double probBiasConnection = 0.15;
        double probRemoveConnection = 0.3;
        double probModifyWeights = 0.7;
        double probRepeatProcess = 0.4;
        
        // Allow multiple changes on network structure
        do {
            
            // Event: Add neuron
            
            if(r.nextDouble() <= probAddNeuron) {
                if(hLayers.size() == 0) {
                    hLayers.add(new ArrayList());
                    hLayers.get(0).add(new WorkingNeuron(0, 0));
                }
                else {
                    // Create a new hidden layer with the neuron
                    if(r.nextDouble() <= probNewLayerNeuron) {
                        hLayers.add(new ArrayList());
                        hLayers.get(hLayers.size() - 1).add(new WorkingNeuron(0, hLayers.size()));
                    }
                    // Add the new neuron to an existing layer
                    else {
                        int layerIndex = r.nextInt(hLayers.size());
                        hLayers.get(layerIndex).add(new WorkingNeuron(hLayers.get(layerIndex).size(), layerIndex));
                    }
                }
            }

            // Event: Remove neuron
            
            if(r.nextDouble() <= probRemoveNeuron) {
                int layerIndex, neuronIndex;
                if(hLayers.size() > 0) {
                    layerIndex = r.nextInt(hLayers.size());
                    neuronIndex = r.nextInt(hLayers.get(layerIndex).size());
                    // check for connections to this neurons in the previous layer
                    // XXX: not very efficient
                    // if it is the first hidden layer then check the input layer neurons
                    if(layerIndex == 0) {
                        for(Neuron n : iLayer) {
                            int conIndex = 0;
                            for(Connection con : n.getConnections()) {
                                if(con.getTarget() == null) {
                                    n.removeConnection(conIndex);
                                }
                                if(con.getTarget().equals(hLayers.get(layerIndex).get(neuronIndex))) {
                                    n.removeConnection(conIndex);
                                    break; // only one connection to target neuron is possible
                                }
                                conIndex++;
                            }
                        }
                    }
                    // otherwise check the previous layers
                    else {
                        for(Neuron n : getHiddenNeuronsUntil(layerIndex)) {
                            int conIndex = 0;
                            for(Connection con : n.getConnections()) {
                                if(con.getTarget().equals(hLayers.get(layerIndex).get(neuronIndex))) {
                                    n.removeConnection(conIndex);
                                    break; // only one connection to target neuron is possible
                                }
                                conIndex++;
                            }
                        }
                    }
                    // delete the whole layer when the only neuron gets deleted
                    if(hLayers.get(layerIndex).size() == 1) {
                        hLayers.remove(layerIndex);
                    }
                    // only remove the single neuron in the layer
                    else {
                        hLayers.get(layerIndex).remove(neuronIndex);
                    }
                }
            }

            // Event: Add connection

            if(r.nextDouble() <= probAddConnection) {
                int startLayerIndex;
                if(hLayers.size() == 0) {
                    startLayerIndex = -1;
                }
                else {
                    startLayerIndex = r.nextInt(hLayers.size() + 2) - 1;
                }
                if(startLayerIndex == -1) {
                    int startNeuronIndex = r.nextInt(iSize);
                    List<WorkingNeuron> neurons = getNeuronsAfter(startLayerIndex);
                    Neuron target = neurons.get(r.nextInt(neurons.size()));
                    iLayer[startNeuronIndex].addConnection(target);
                }
                else if(startLayerIndex < hLayers.size()) {
                    int startNeuronIndex = r.nextInt(hLayers.get(startLayerIndex).size());
                    List<WorkingNeuron> neurons = getNeuronsAfter(startLayerIndex);
                    int targetIndex;
                    targetIndex= r.nextDouble() < probBiasConnection ? -1 : r.nextInt(neurons.size());
                    if(targetIndex >= 0) {
                        Neuron target = neurons.get(targetIndex);
                        hLayers.get(startLayerIndex).get(startNeuronIndex).addConnection(target);
                    }
                    else {
                        hLayers.get(startLayerIndex).get(startNeuronIndex).setBiasConnectionActive(true);
                    }
                }
                else {
                    int startNeuronIndex = r.nextInt(oLayer.length);
                    oLayer[startNeuronIndex].setBiasConnectionActive(true);
                }
            }

            // Event: Remove connection

            if(r.nextDouble() <= probRemoveConnection) {
                int layerIndex;
                if(hLayers.size() == 0) {
                    layerIndex = -1;
                }
                else {
                    layerIndex = r.nextInt(hLayers.size() + 2) - 1;
                }
                // delete a connection of an input neuron
                if(layerIndex == -1) {
                    int neuronIndex = r.nextInt(iSize);
                    // if the random neuron has no connections: try the whole layer
                    if(!iLayer[neuronIndex].hasConnections()) {
                        int index = 0;
                        for(Neuron n : iLayer) {
                            if(n.hasConnections()) {
                                neuronIndex = index;
                                // take the first neuron that has a connection
                                break;
                            }
                            index++;
                        }
                    }
                    // if no neuron in the layer has a connection: do nothing, 
                    // otherwise delete a random connection of the neuron
                    if(iLayer[neuronIndex].hasConnections()) {
                        int connectionIndex = r.nextInt(iLayer[neuronIndex].getConnections().size());
                        iLayer[neuronIndex].removeConnection(connectionIndex);
                    }
                    
                }
                // delete a connection of a hidden neuron
                else if(layerIndex < hLayers.size()){
                    int neuronIndex = r.nextInt(hLayers.get(layerIndex).size());
                    // if the random neuron has no connections: try the whole layer
                    if(!hLayers.get(layerIndex).get(neuronIndex).hasConnections()) {
                        int index = 0;
                        for(Neuron n : hLayers.get(layerIndex)) {
                            if(n.hasConnections()) {
                                neuronIndex = index;
                                // take the first neuron that has a connection
                                break;
                            }
                            index++;
                        }
                    }
                    // if no neuron in the layer has a connection: do nothing, 
                    // otherwise delete a random connection of the neuron
                    if(hLayers.get(layerIndex).get(neuronIndex).hasConnections()) {
                        int connectionIndex = r.nextInt(hLayers.get(layerIndex).get(neuronIndex).getConnections().size());
                        hLayers.get(layerIndex).get(neuronIndex).removeConnection(connectionIndex);
                    }
                }
                // delete a bias connection in the output layer
                else {
                    int startNeuronIndex = r.nextInt(oLayer.length);
                    oLayer[startNeuronIndex].setBiasConnectionActive(false);
                }
            }
        }
        while(r.nextDouble() <= probRepeatProcess);
        
        // Event: Adjust weights

        if(r.nextDouble() <= probModifyWeights) {
            for(Neuron n : iLayer) {
                for(Connection c : n.getConnections()) {
                    c.setWeight(c.getWeight() 
                            + (r.nextDouble() * maxDifference * 2) - maxDifference);
                }
            }
            for(WorkingNeuron n : getNeuronsAfter(-1)) {
                if(n.isBiasConnectionActive()) {
                    n.setBiasWeight(n.getBiasWeight() + (r.nextDouble() * maxDifference * 2) - maxDifference);
                }
                for(Connection c : n.getConnections()) {
                    c.setWeight(c.getWeight() 
                            + (r.nextDouble() * maxDifference * 2) - maxDifference);
                }
            }
        }
    }
    
    /**
     * Returns a copy of the current neural network
     */
    public NeuralNet getCopy() {
        // update the hidden layer sizes
        hSizes = new ArrayList<>();
        for (int i = 0; i < hLayers.size(); i++) {
            hSizes.add(hLayers.get(i).size());
        }
        NeuralNet copy = new NeuralNet(iSize, hSizes, oSize);
        correctIds();
        
        // copy the output layer
        WorkingNeuron[] oLayerCopy = new WorkingNeuron[oSize];
        for (int i = 0; i < oSize; i++) {
            oLayerCopy[i] = new WorkingNeuron(i, Integer.MAX_VALUE);
        }
        copy.oLayer = oLayerCopy;
        
        // copy the hidden layers (backwards)
        List<List<WorkingNeuron>> hLayersCopy = new ArrayList<>();
        if(hLayers.size() > 0) {
            for (int layerIndex = hLayers.size() - 1; layerIndex >= 0; layerIndex--) {
                ArrayList<WorkingNeuron> layer = new ArrayList<WorkingNeuron>();
                for (int neuronIndex = 0; neuronIndex < hLayers.get(layerIndex).size(); neuronIndex++) {
                    WorkingNeuron neuron = new WorkingNeuron(neuronIndex, layerIndex);
                    for (int connectionIndex = 0; connectionIndex < hLayers.get(layerIndex).get(neuronIndex).connections.size(); connectionIndex++) {
                        Connection con = hLayers.get(layerIndex).get(neuronIndex).getConnections().get(connectionIndex);
                        Connection newCon;
                        if(con.getTarget().getLayerId() == Integer.MAX_VALUE) {
                            newCon = new Connection(oLayerCopy[con.getTarget().getId()], false);
                        }
                        else {
                            newCon = new Connection(hLayersCopy.get(con.getTarget().layerId - layerIndex - 1).get(con.getTarget().getId()), false);
                        }
                        newCon.setWeight(con.getWeight());
                        neuron.addConnection(newCon);
                    }
                    layer.add(neuron);
                }
                hLayersCopy.add(0, layer);
            }
        }
        copy.hLayers = hLayersCopy;
        
        // copy the input layer
        InputNeuron[] iLayerCopy = new InputNeuron[iSize];
        for (int i = 0; i < iSize; i++) {
            iLayerCopy[i] = new InputNeuron(i, -1);
            for(Connection con : iLayer[i].getConnections()) {
                Connection newCon = new Connection(copy.getNeuronByIds(con.getTarget().getId(), con.getTarget().getLayerId()), false);
                newCon.setWeight(con.getWeight());
                iLayerCopy[i].addConnection(newCon);
            }
        }
        copy.iLayer = iLayerCopy;
        
        return copy;
    }
    
    /**
     * Corrects the ids of the hidden neurons
     */
    public void correctIds() {
        for (int i = 0; i < hLayers.size(); i++) {
            for (int j = 0; j < hLayers.get(i).size(); j++) {
                hLayers.get(i).get(j).setId(j);
                hLayers.get(i).get(j).setLayerId(i);
            }
        }
    }
    
    /**
     * Returns a working neuron indicated by the id and layer id
     */
    public Neuron getNeuronByIds(int id, int layerId) {
        for (int i = 0; i < iLayer.length; i++) {
            if(iLayer[i].getId() == id && iLayer[i].getLayerId() == layerId)
                return iLayer[i];
        }
        
        for (int i = 0; i < hLayers.size(); i++) {
            for (int j = 0; j < hLayers.get(i).size(); j++) {
                if(hLayers.get(i).get(j).getId() == id && hLayers.get(i).get(j).getLayerId() == layerId)
                    return hLayers.get(i).get(j);
            }
        }
        
        for (int i = 0; i < oLayer.length; i++) {
            if(oLayer[i].getId() == id && oLayer[i].getLayerId() == layerId)
                return oLayer[i];
        }
        // no such neuron found
        return null;
    }
    
    /**
     * Write the construction of the network into the console
     */
    public void printConstruction() {
        String output = iLayer.length + "";
        for (int i = 0; i < hLayers.size(); i++) {
            output += " | " + hLayers.get(i).size();
        }
        output += " | " + oLayer.length;
        System.out.println(output);
    }
    
    /**
     * Returns the neurons of the hidden layers until the border (exclusive)
     */
    public List<WorkingNeuron> getHiddenNeuronsUntil(int border) {
        List<WorkingNeuron> output = new ArrayList<WorkingNeuron>();
        
        border = border > hLayers.size() ? hLayers.size() : border;
        for (int i = 0; i < border; i++) {
            for (int j = 0; j < hLayers.get(i).size(); j++) {
                output.add(hLayers.get(i).get(j));
            }
        }
        
        return output;
    }
    
    /**
     * Returns the neurons of the hidden and output layer located after the border (exclusive)
     */
    public List<WorkingNeuron> getNeuronsAfter(int border) {
        List<WorkingNeuron> output = new ArrayList<WorkingNeuron>();
        
        border = border > hLayers.size() ? hLayers.size() : border;
        border++;
        for (int i = border; i < hLayers.size(); i++) {
            for (int j = 0; j < hLayers.get(i).size(); j++) {
                output.add(hLayers.get(i).get(j));
            }
        }
        for (int i = 0; i < oLayer.length; i++) {
            output.add((WorkingNeuron) oLayer[i]);
        }
        
        return output;
    }
    
    /**
     * Returns the networks id
     */
    public int getNetworkId() {
        return id;
    }
}