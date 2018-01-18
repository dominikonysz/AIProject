/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the base of a single Neuron inside a neural network
 * @author Dominik Onyszkiewicz
 */
public abstract class Neuron {
    protected List<Connection> connections;
    protected double input;    
    protected int id, layerId;
    
    /**
     * Creates a new neuron with space for the given amount of connections
     */
    public Neuron(int id, int layerId) {
        connections = new ArrayList<Connection>();
        this.id = id;
        this.layerId = layerId;
    }
    
    /**
     * Returns the value of the attribute id
     */
    public int getId() {
        return id;
    }
    
    
    /**
     * Sets the neuron id to the given value
     */
    public void setId(int value) {
        id = value;
    }
    
    /**
     * Returns the value of the attribute id
     */
    public int getLayerId() {
        return layerId;
    }
    
    
    /**
     * Sets the neuron id to the given value
     */
    public void setLayerId(int value) {
        layerId = value;
    }
    
    /**
     * Pushes the output of the current neuron to all neurons this neuron
     * is connected to
     */
    public void pushOutput() {
        double output = activate();
        for(Connection c : connections) {
            c.transferOutput(output);
        }
        input = 0;
    }
    
    /**
     * Adds the received input onto the previous input
     * @param newInput 
     */
    public void receiveInput(double newInput) {
        input += newInput;
    }
    
    /**
     * Adds a new connection to the given neuron with a random weight and
     * increments the amount of existing connections
     * @param target connection target neuron
     */
    public void addConnection(Neuron target) {
        connections.add(new Connection(target, true));
    }
    
    /**
     * Adds the given connection to the connections of the neuron
     * @param con new connection
     */
    public void addConnection(Connection con) {
        connections.add(con);
    }
    
    
    /**
     * Removes the connection which is identified by the given index
     * (Used for deleting random connections)
     * @param index index of neuron that should get deleted
     */
    public void removeConnection(int index) {
        try {
            connections.remove(index); 
        }
        catch(IndexOutOfBoundsException e) {
            // Just don't remove anything
        }
    }
    
    /**
     * Returns all connections going out from this neuron
     * @return connections array
     */
    public List<Connection> getConnections() {
        return connections;
    }
    
    /**
     * Return if the neuron has one or more connections or not
     * @return 
     */
    public boolean hasConnections() {
        return connections.size() > 0;
    }
    
    
    /**
     * Computes the output of the neuron with an associated method
     * @return output value of the neuron
     */
    abstract double activate();
    
}
