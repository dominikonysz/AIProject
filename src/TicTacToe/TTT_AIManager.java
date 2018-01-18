/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import NeuralNetwork.NeuralNet;
import java.util.ArrayList;

/**
 * Manages games between two AIs
 * @author Dominik Onyszkiewicz
 */
public class TTT_AIManager {
    NeuralNet player1, player2, activePlayer;
    TTT_Game game;
    int winReward, successReward, unsuccessPunishment;
    
    /**
     * Initializes all necessary components of a game betweens two ais.
     * Generates two random neural nets and sets the first player 
     * as the active or beginning player
     */
    public TTT_AIManager() {
        player1 = new NeuralNet(9, new ArrayList<>(), 9);
        player2 = new NeuralNet(9, new ArrayList<>(), 9);
        activePlayer = player1;
        
        game = new TTT_Game();
        
        winReward = 30;
        successReward = 10;
        unsuccessPunishment = -5;
    }
    
    /**
     * Plays a whole game between two neural nets until it is either finished or
     * one of the neural networks does a invalid/unsuccessful turn
     * TODO: Create a fair enemy for all neural nets to measure the fitness
     * without additional randomness
     */
    public void playGame() {
        // Resetting the fitness of both players at the start of every game
        // TODO: Maybe dont reset the fitness so it would be more precise over multiple games
        //player1.resetFitness();
        //player2.resetFitness();
        
        game.startGame();
        
        double[] playerOutput;
        int turnState;
        while(!game.isFinished()) {
            playerOutput = activePlayer.computeNetOutput(game.getDoubleField());
            if((turnState = game.doTurn(mapPlayerOutput(playerOutput, game.getField()))) == 1) {
                // increase fitness as a reward for a valid turn
                activePlayer.increaseFitness(successReward);
                
                // change active player for the next turn
                activePlayer = activePlayer == player1 ? player2 : player1;
            }
            else if(turnState == 2) {
                // increase fitness as a reward for winning the game
                int winner = game.getWinner();
                if(winner == 0) {
                    player1.increaseFitness(winReward);
                }
                else if (winner == 1) {
                    player2.increaseFitness(winReward);
                }
                // break out of while-loop because the game is finished now
                break;
            }
            else if(turnState == -1) {
                // punishment for unsuccessful turn
                activePlayer.increaseFitness(unsuccessPunishment);
                // break out of while-loop because the player did an unsuccessful turn
                break;
            }
        }
    }
    
    /**
     * Plays a game between the two given ais
     * @param p1 player 1
     * @param p2 player 2
     */
    public void playGame(NeuralNet p1, NeuralNet p2) {
        overwriteAIs(p1, p2);
        playGame();
    }
    
    /**
     * Determines the field matching the player output array
     * @param playerOutput output of the players neural network
     * @return associated field on the 3x3 square
     * TODO: Choose the player output out of the possible field, so unsuccessful
     * turns are not possible anymore
     */
    private int mapPlayerOutput(double[] playerOutput, int[] currentField) {
        int max = 0;
        
        for (int i = 1; i < playerOutput.length; i++) {
            if(playerOutput[i] > playerOutput[max] && currentField[i] == TTT_Game.FieldStates.EMPTY.getValue()) {
                max = i;
            }
        }
        return max;
    }
    
    /**
     * Deletes the two ais inside the game manager and replaces them with new ones
     * @param p1 new player 1
     * @param p2 new player 2
     */
    public void overwriteAIs(NeuralNet p1, NeuralNet p2) {
        player1 = p1;
        player2 = p2;
    }
    
    /**
     * Transforms the current state of the field into a suitable input for
     * a neural network
     * @param field current state of the field
     * @return suitable input for the neural network
     */
    public double[] transformFieldToInput(double[] field) {
        double[] output = new double[field.length*2];
        int counter = 0;
        for (int i = 0; i < field.length; i++) {
            // 0;0 for an empty field
            // 1;0 for player 1
            // 0;1 for player 2
            output[counter] = field[i] == 0 ? 1 : 0;
            counter++;
            output[counter] = field[i] == 1 ? 1 : 0;
        }
        return output;
    }
}
