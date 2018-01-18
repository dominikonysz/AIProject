/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JAR_GeneticAlgorithm;

import NeuralNetwork.NeuralNet;

/**
 *  Provides the required attributes and functions to be used in a genetic algorithm
 *  -------------------------------------UNUSED---------------------------------------
 * @author Dominik Onyszkiewicz
 */
public abstract class Individual {
    protected NeuralNet nn;
    protected int[] view;
    
    /**
     * The AI looks at the screen an saves what is sees
     */
    public abstract void lookAtScreen();
    
    /**
     * Returns the fitness of the individual
     */
    public int getFitness() {
        return nn.getFitness();
    }
    
    public static Individual getNewObject() {
        return null;
    };
}
