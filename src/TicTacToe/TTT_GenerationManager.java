/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import NeuralNetwork.NeuralNet;
import TicTacToe.TTT_AIManager;
import java.util.ArrayList;

/**
 * Manages the general selection.     public GenerationManager() {

 * Analyzes the success of the neural nets and generates a new with the best
 * and mutations of them.
 * @author Dominik Onyszkiewicz
 */
public class TTT_GenerationManager {
    NeuralNet[] individuals;
    int generationSize, generation;
    TTT_AIManager aigame;
    
    /**
     * Sets up the basic components of the genetic algorithm and the first
     * generation
     */
    public TTT_GenerationManager() {
        generation = 0;
        generationSize = 400;
        individuals = new NeuralNet[generationSize];
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new NeuralNet(18, new ArrayList<>(),9);
        }
        
        aigame = new TTT_AIManager();
    }
    
    /**
     * Generates the next generation with the best 100 neural nets and 300 mutations
     */
    public void generateNextGeneration() {
        sortBest();
        // copy the 100 best three times into the whole array
        for (int i = 0; i < generationSize / 4; i++) {
            for (int j = 1; j < generationSize / 100; j++) {
                individuals[i + j * 100] = individuals[i].getCopy();
            }
        }
        // mutate every copy
        for (int i = generationSize / 4; i < generationSize; i++) {
            individuals[i].mutate(1);
        }
        generation++;
        System.out.println("New generation: " + generation);
    }
    
    /**
     * Every neural net plays two games against every other neural net and get
     * a fitness assigned based on the success in all these games
     * TODO: Determine the fitness with the same enemy AI every time
     */
    public void determineFitnesses() {
        // reset alls fitnesses before determining new fitnesses
        for (NeuralNet individual : individuals) {
            individual.resetFitness();
        }
        for (NeuralNet source : individuals) {
            for (NeuralNet target : individuals) {
                aigame.playGame(source, target);
            }
        }
    }
    
    /**
     * Sorts the array of individuals so that the hundred best neural nets based
     * on their fitness are on the beginning of the array
     */
    public void sortBest() {
        NeuralNet temp;
        int best = 0;
        for (int c = 0; c < generationSize / 4; c++) { 
            best = c;
            for (int i = c; i < individuals.length; i++) {
                if(individuals[i] == null) continue;
                if(individuals[i].getFitness() > individuals[best].getFitness()) {
                    best = i;
                }
            }
            temp = individuals[c];
            individuals[c] = individuals[best];
            individuals[best] = temp;
        }
    }
}
