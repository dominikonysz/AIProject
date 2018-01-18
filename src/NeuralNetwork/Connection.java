/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.Random;

/**
 * Represents a connections between two neurons inside the neural network
 * @author Dominik Onyszkiewicz
 */
public class Connection {
    private Neuron target;
    private double weight;
    private Random r;
    
    /**
     * Creates a new connection to the given neuron with a random initial weight
     * @param target target of the connection
    */
    public Connection(Neuron target, boolean randomWeight) {
        this.target = target;
        if(randomWeight) {
            r = new Random();
            weight = (r.nextDouble() * 4) - 2;
        }
        else {
            weight = 1;
        }
    }
    
    /**
     * Sets the weight to a given new weight
     * @param newWeight new weight of the connection
    */
    public void setWeight(double newWeight) {
        weight = newWeight;
    }
    
    /**
     * Returns the weight of the connection
     * @return weight of the connection
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Return the target of the connection
     * @return target neuron
     */
    public Neuron getTarget() {
        return target;
    }
    
    /**
     * Transfers the output of the source neuron to the target neuron by computing
     * a new input with the weight of the connection
     * @param output output of the source neuron
     */
    public void transferOutput(double output) {
        double newInput = output * weight;
        target.receiveInput(newInput);
    }
}
