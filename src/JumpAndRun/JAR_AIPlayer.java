/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JumpAndRun;

import General.Vector2;
import NeuralNetwork.NeuralNet;
import java.util.ArrayList;
import NeuralNetwork.NetworkVisualization;

/**
 *  Connects the neural network with the game a computes actions depending on the 
 * screen state
 * @author Dominik Onyszkiewicz
 */
public class JAR_AIPlayer {
    
    private NeuralNet nn;
    private int[] view, parsedView;
    private JAR_Game game;
    
    private Player player;
    private double bestPosX; // furthest right position so far
    private long startTime; 
    private long lastActionTime; // time stamp of the last action
    private long lastProgressTime;
    private Vector2 lastPlayerPos;
    private int[] lastOut;
    private Vector2 travelledDistance;
    private int timeNeeded;
    private int inputBounds = 7;
    
    public JAR_AIPlayer() {
        nn = new NeuralNet(inputBounds * inputBounds, new ArrayList(), 3);
        this.game = JAR_Game.getGame();
        view = new int[15 * 15];
        parsedView = new int[inputBounds * inputBounds];
        
        player = game.getPlayer();
        bestPosX = player.getPosition().getX();
        startTime = System.currentTimeMillis();
        
        lastActionTime = startTime;
        lastPlayerPos = player.getPosition();
        lastOut = new int[3];
        
        travelledDistance = new Vector2();
        timeNeeded = 0;
    }
    
    public JAR_AIPlayer(JAR_AIPlayer base) {
        this.game = JAR_Game.getGame();
        
        view = new int[15 * 15];
        parsedView = new int[inputBounds * inputBounds];
        
        nn = base.getNeuralNet().getCopy();
        nn.mutate(1);
        
        player = game.getPlayer();
        bestPosX = player.getPosition().getX();
        startTime = System.currentTimeMillis();
        
        lastActionTime = startTime;
        lastProgressTime = startTime;
        lastPlayerPos = new Vector2(player.getPosition().getX(), player.getPosition().getY());
        lastOut = new int[3];
        
        travelledDistance = new Vector2();
        timeNeeded = 0;
    }
    
    /**
     * The ai looks at the screen state and saves its view
     * TODO: decrease the view size
     */
    public void lookAtScreen() {
        for (int i = 0; i < view.length; i++) {
            view[i] = 0;
        }
        Entity[] blocks = game.getBlocksArray();
        int xOffset = game.getFrame().getViewOffsetX();
        int yOffset = game.getFrame().getViewOffsetY();
        for (int i = 0; i < blocks.length; i++) {
            Entity block = blocks[i];
            // check if the block is visible for the player
            if((block.getPosition().getIntX() - xOffset > -25)
                    && (block.getPosition().getIntX() - xOffset < 725)
                    && (block.getPosition().getIntY() - yOffset > -25)
                    && (block.getPosition().getIntY() - yOffset < 725))
            {
                // if yes: write it into the view of the player
                view[((block.getPosition().getIntX() - xOffset + 25) / 50) + ((block.getPosition().getIntY() - yOffset + 25) / 50) * 15] = 1;
            }
        }
    }
    
    /**
     * Returns a decreased version of the players view to ignore unrelevant blocks that are too far away
     * 
     * the new bounds need to be uneven to be able to recognize the player as the center
     */
    public int[] parseView(int newBound) {
        int viewBound = (int) Math.sqrt(view.length); // the view is quadratic
        int[] output = new int[newBound * newBound];
        int start = (viewBound - newBound) / 2;
        for (int y = 0; y < newBound; y++) {
            for (int x = 0; x < newBound; x++) {
                output[x + y * newBound] = view[start + x + (start + y) * viewBound];
            }
        }
        return output;
    }
    
    /**
     * Computes what actions the player should do by computing an output from the
     * players neural net with the player view as input
     */
    public void doAction() {
        lookAtScreen();
        NetworkVisualization.getFrame().repaint();
        
        Vector2 playerPos = player.getPosition();
        
        travelledDistance.setX(playerPos.getX() - player.getStartingPosition().getX());
        
        calculateFitness();
        
        // register last progress to eliminate standing players
        if(playerPos.getX() != lastPlayerPos.getX()) {
            lastPlayerPos = new Vector2(playerPos.getX(), playerPos.getY());
            lastActionTime = System.currentTimeMillis();
        }
        else if(playerPos.getY() != lastPlayerPos.getY()) {
            lastPlayerPos = new Vector2(playerPos.getX(), playerPos.getY());
            lastActionTime = System.currentTimeMillis();
        }
        if(playerPos.getX() > bestPosX) {
            lastProgressTime = System.currentTimeMillis();
            bestPosX = playerPos.getX();
        }
        
        double[] netInput = new double[inputBounds * inputBounds];
        parsedView = parseView(inputBounds);
        for (int i = 0; i < parsedView.length; i++) {
            netInput[i] = parsedView[i];
        }
        double[] output = nn.computeNetOutput(netInput);
        int[] intOutput = new int[output.length];
        /*for (int i = 0; i < output.length; i++) {
            intOutput[i] = (int) Math.round(output[i]);
        }*/
        if(output[0] > output[1] && output[0] > 0.65) {
            intOutput[0] = 1;
            intOutput[1] = 0;
        }
        else if(output[1] > output[0] && output[1] > 0.65) {
            intOutput[1] = 1;
            intOutput[0] = 0;
        }
        else {
            intOutput[0] = 0;
            intOutput[1] = 0;
        }
        if(output[2] > 0.65) {
            intOutput[2] = 1;
        }
        //System.out.println(output[0] + " | " + output[1] + " | " + output[2]);
        game.doAction(intOutput);
        lastOut = intOutput;
    }
    
    /**
     * Gives the neural net a specified amount of fitness depending of the progress in the level
     * TODO: proper fitness calculation
     */
    public void calculateFitness() {
        
        double fitness = travelledDistance.getX();
        
        nn.setFitness((int) Math.round(fitness));
    }
    
    /**
     * Returns the distance that the player has moved on the x axis
     */
    public Vector2 getTravelledDistance() {
        return travelledDistance;
    }
    
    /**
     * Ends the players turn by setting certain values
     */
    public void endTurn() {
        timeNeeded = (int) (System.currentTimeMillis() - startTime);
        System.out.println("Fitness: " + nn.getFitness());
    }
    
    /**
     * Writes the view of the ai into the console
     */
    public void printView() {
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                System.out.print(view[x + y * 15] + " ");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------");
    }
    
    /**
     * Returns the fitness of the individual
     */
    public int getFitness() {
        return nn.getFitness();
    }
    
    /**
     * Returns the neural network of the AI player
     */
    public NeuralNet getNeuralNet() {
        return nn;
    }
    
    /**
     * Returns the time stamp of start time
     */
    public long getStartTime() {
        return startTime;
    }
    
    
    /**
     * Resets the time stamp of the last progress to define a start time
     */
    public void startProgressDetection() {
        player.resetValues();
        startTime = System.currentTimeMillis();
        lastActionTime = System.currentTimeMillis();
        lastProgressTime = System.currentTimeMillis();
        bestPosX = player.getPosition().getX();
        travelledDistance.setX(0);
        travelledDistance.setY(0);
        timeNeeded = 0;
    }

    /**
     * Returns the time stamp of the last action
     * @return 
     */
    public long getLastActionTime() {
        return lastActionTime;
    }
    
    /**
     * Returns the time stamp of the last progress
     * @return 
     */
    public long getLastProgressTime() {
        return lastProgressTime;
    }

    /**
     * Returns the players view
     * @return 
     */
    public int[] getView() {
        return view;
    }
    
    /**
     * Returns the last moves of the player
     */
    public int[] getLastOut() {
        return lastOut;
    }
    
    /**
     * Returns the time the player was active
     */
    public int getNeededTime() {
        return timeNeeded;
    }
    
    /**
     * Returns the reduced view field of the ai player
     */
    public int[] getParsedView() {
        return parsedView;
    }
}
