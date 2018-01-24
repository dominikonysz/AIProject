/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JAR_GeneticAlgorithm;

import JumpAndRun.JAR_AIPlayer;
import JumpAndRun.JAR_Game;
import NeuralNetwork.NetworkVisualization;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Represent a generation for a genetic algorithm to manage the different 
 *  individuals. 
 * --------------------------------ONLY FOR JAR--------------------------------
 * @author Dominik Onyszkiewicz
 */
public class Generation {
    private int id, size;
    JAR_AIPlayer[] individuals;
    JAR_Game game;
    int maxFitness, averageFitness;
    
    /**
     * Creates a completely new generation with given parameters
     * @param id
     * @param size 
     */
    public Generation(int id, int size) {
        this.id = id;
        // the size should be dividable by 4
        if(size % 4 != 0) {
            size = size - (size % 4);
        }
        this.size = size;
        game = JAR_Game.getGame();
        individuals = new JAR_AIPlayer[size];
        for (int i = 0; i < size; i++) {
            individuals[i] = new JAR_AIPlayer();
            // mutate the new neurals nets 5 times to begin with something
            for (int j = 0; j < 5; j++) {
                individuals[i].getNeuralNet().mutate(1);
            }
        }
        
        maxFitness = 0;
        averageFitness = 0;
        System.out.println("New Generation [" + id + "|" + size + "]");
    }
    
    /**
     * Creates a new generation based on a previous generation
     * @param lastGen previous generation
     */
    public Generation(Generation lastGen) {
        game = JAR_Game.getGame();
        createGeneration(lastGen);
        
        System.out.println("New Generation [" + id + "|" + size + "]");
    }
    
    /**
     * Creates a new generation by mutating a previous generation
     */
    private void createGeneration(Generation lastGen) {
        id = lastGen.id + 1;
        size = lastGen.size;
        JAR_AIPlayer[] topQuart = lastGen.getTopQuarter();
        individuals = new JAR_AIPlayer[size];
        for (int i = 0; i < topQuart.length ; i++) {
            individuals[i] = topQuart[i];
            individuals[i].getNeuralNet().resetFitness();
        }
        for (int i = topQuart.length; i < individuals.length; i++) {
            individuals[i] = new JAR_AIPlayer(topQuart[i%topQuart.length]);
        }
        maxFitness = 0;
        averageFitness = 0;
    }
    
    /**
     * Returns the top quarter of the individuals
     */
    public JAR_AIPlayer[] getTopQuarter() {
        JAR_AIPlayer[] top = new JAR_AIPlayer[size / 4];
        
        sortIndividuals();
        for (int i = 0; i < top.length; i++) {
            top[i] = individuals[individuals.length - i - 1];
        }
        
        return top;
    }
    
    /**
     * Sorts the individuals based of their fitness value
     */
    private void sortIndividuals() {
        quickSort(0, size-1);
        maxFitness = individuals[individuals.length - 1].getFitness();
        calculateAverageFitness();
        System.out.println("GENERATION BEST: " + individuals[individuals.length - 1].getFitness()
                            + " by ID: " + individuals[individuals.length - 1].getNeuralNet().getNetworkId());
    }
    
    /**
     * Basic quicksort algorithm
     * @param lowerIndex
     * @param higherIndex 
     */
    private void quickSort(int lowerIndex, int higherIndex) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        JAR_AIPlayer pivot = individuals[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (individuals[i].getFitness() < pivot.getFitness()) {
                i++;
            }
            while (individuals[j].getFitness() > pivot.getFitness()) {
                j--;
            }
            if (i <= j) {
                exchangeIndividuals(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
    
    /**
     * Swaps the two individuals on the given indexes
     * @param i first index
     * @param j second index
     */
    private void exchangeIndividuals(int i, int j) {
        JAR_AIPlayer temp = individuals[i];
        individuals[i] = individuals[j];
        individuals[j] = temp;
    }

    /**
     * Returns the fitness of the 'fittest' individual in the generation
     * @return 
     */
    public int getMaxFitness() {
        return maxFitness;
    }
    
    /**
     * Calculates the average fitness of the generations individuals
     */
    private void calculateAverageFitness() {
        int cumulated = 0;
        for(JAR_AIPlayer player : individuals) {
            cumulated += player.getFitness();
        }
        averageFitness = cumulated / size;
    }
    
    /**
     * Returns the average fitness of the generations individuals
     */
    public int getAverageFitness() {
        return averageFitness;
    }

    /**
     * Returns the generations size
     * @return 
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Plays the game with every AI player to determine all different fitnesses of the neural nets
     */
    public void computeFitnesses() {
        int counter = 0;
        NetworkVisualization netVis = NetworkVisualization.getFrame();
        for (JAR_AIPlayer individual : individuals) {
            System.out.println("Player: " + counter++ + " | ID: " + individual.getNeuralNet().getNetworkId());
            // show the network
            if(netVis == null) {
                netVis = NetworkVisualization.getFrame(individual.getNeuralNet());
            }
            else {
                netVis.updateNetwork(individual.getNeuralNet());
            }
            // print it into the console
            individual.getNeuralNet().printConstruction();
            game.setAIPlayer(individual);
            game.restart();
            individual.startProgressDetection();
            while(true) {
                if(!game.isRunning()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Generation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    if(System.currentTimeMillis() - individual.getLastActionTime() > 500) {
                        individual.endTurn();
                        break;
                    }
                    if(System.currentTimeMillis() - individual.getLastProgressTime() > 3000) {
                        individual.endTurn();
                        break;
                    }
                    if(game.getPlayer().getPosition().getX() > game.getLevelBounds().getX()) {
                        individual.endTurn();
                        break;
                    }
                    if(game.getPlayer().getPosition().getY() > game.getLevelBounds().getY()) {
                        individual.endTurn();
                        break;
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Generation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
