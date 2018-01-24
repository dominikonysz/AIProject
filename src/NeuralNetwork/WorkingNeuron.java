/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

/**
 * A neuron that computes an output that can be used by further neuron or as a
 * result with a associated function (sigmoid)
 * @author Dominik Onyszkiewicz
 */
public class WorkingNeuron extends Neuron {

    private double bias;
    private double biasWeight;
    private boolean biasConnectionActive;
    private double output;
    
    public WorkingNeuron(int id, int layerId) {
        super(id, layerId);
        bias = 1;
        biasWeight = 0.1;
        biasConnectionActive = false;
    }
    
    /**
     * Returns the current weight of the bias connection
     * @return 
     */
    public double getBiasWeight() {
        return biasWeight;
    }
    
    /**
     * Sets the weight of the bias connection to the given value
     * @param newWeight 
     */
    public void setBiasWeight(double newWeight) {
        biasWeight = newWeight;
    }
    
    /**
     * Returns if the bias connection is active or not
     */
    public boolean isBiasConnectionActive() {
        return biasConnectionActive;
    }
    
    /**
     * Activates/Deactivates the bias connection depending on the given value
     */
    public void setBiasConnectionActive(boolean value) {
        biasConnectionActive = value;
    }
    
    /**
     * Returns the output of the neuron after computing it with the specitific function (sigmoid)
     * @return neuron output
     */
    @Override
    double activate() {
        double x = input;
        if(biasConnectionActive) {
            x += bias * biasWeight;
        }
        // no special result if the input is smaller than -10 or bigger than
        // 10
        if(x < -10) {
            output = 0;
        }
        else if(x > 10) {
            
            output = 1;
        }
        else {
            // sigmoid function
            output = (1 / (1 + Math.exp(-x)));
        }
        input = 0;
        return output;
    }
    
    /**
     * Returns the output of the single neuron
     */
    public double getNeuronOutput() {
        return output;
    }
    
}
