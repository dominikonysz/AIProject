/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

/**
 * A neuron that just gives its input value to the connected neurons of the hidden 
 * layer through connections
 * @author Dominik Onyszkiewicz
 */
public class InputNeuron extends Neuron {

    /**
     * Creates a new neuron for the input layer with space for a fixed amount of
     * connections
     * @param conAmount amount of possible connections
     */
    public InputNeuron(int id, int layerId) {
        super(id, layerId);
    }
    
    /**
     * Sets the input of the neuron to the given value
     * @param newInput new input value
     */
    public void setInput(double newInput) {
        input = newInput;
    }

    /**
     * Returns the input of the neuron without changing anything
     * @return input of the neuron
     */
    @Override
    double activate() {
        return input;
    }
    
}
