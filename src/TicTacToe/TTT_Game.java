/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

/**
 * General logic behind the tic tac toe game
 * @author Dominik Onyszkiewicz
 */
public class TTT_Game {
    private int[] fields;
    private int currentPlayer;
    boolean gameRunning;
    
    public enum FieldStates {
        EMPTY(0), PLAYER1(-1), PLAYER2(1);
        
        private int value;
        
        FieldStates(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    /**
     * Creates a game by initializing the field and the players
     */
    public TTT_Game() {
        // the nine fields numbered from top to bottom and from left to right
        fields = new int[9];
        for (int i = 0; i < fields.length; i++) {
            // nobody placed his marker on the field
            fields[i] = -1;
        }
        
        gameRunning = true;
        currentPlayer = 0;
    }
    
    /**
     * Checks every possible way to win and determines if anybody won yet. 
     * If yes the game gets stopped and the winner is set as the currently 
     * active player
     * TODO: Change the acceptance of the new player values in other classes(AIGameManager,...)
     */
    private void checkWinner() {
        
        if(gameRunning) {
        // horizontal
        
            // top
            if(fields[0] == FieldStates.PLAYER1.getValue() && fields[1] == FieldStates.PLAYER1.getValue() && fields[2] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[0] == FieldStates.PLAYER2.getValue() && fields[1] == FieldStates.PLAYER2.getValue() && fields[2] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            // middle
            if(fields[3] == FieldStates.PLAYER1.getValue() && fields[4] == FieldStates.PLAYER1.getValue() && fields[5] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[3] == FieldStates.PLAYER2.getValue() && fields[4] == FieldStates.PLAYER2.getValue() && fields[5] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            // bottom
            if(fields[6] == FieldStates.PLAYER1.getValue() && fields[7] == FieldStates.PLAYER1.getValue() && fields[8] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[6] == FieldStates.PLAYER2.getValue() && fields[7] == FieldStates.PLAYER2.getValue() && fields[8] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            
        // vertical
        
            // left
            if(fields[0] == FieldStates.PLAYER1.getValue() && fields[3] == FieldStates.PLAYER1.getValue() && fields[6] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[0] == FieldStates.PLAYER2.getValue() && fields[3] == FieldStates.PLAYER2.getValue() && fields[6] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            // middle
            if(fields[1] == FieldStates.PLAYER1.getValue() && fields[4] == FieldStates.PLAYER1.getValue() && fields[7] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[1] == FieldStates.PLAYER2.getValue() && fields[4] == FieldStates.PLAYER2.getValue() && fields[7] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            // right
            if(fields[2] == FieldStates.PLAYER1.getValue() && fields[5] == FieldStates.PLAYER1.getValue() && fields[8] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[2] == FieldStates.PLAYER2.getValue() && fields[5] == FieldStates.PLAYER2.getValue() && fields[8] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            
        // diagonal
        
            // left to right
            if(fields[0] == FieldStates.PLAYER1.getValue() && fields[4] == FieldStates.PLAYER1.getValue() && fields[8] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[0] == FieldStates.PLAYER2.getValue() && fields[4] == FieldStates.PLAYER2.getValue() && fields[8] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
            // right to left
            if(fields[2] == 0 && fields[4] == FieldStates.PLAYER1.getValue() && fields[6] == FieldStates.PLAYER1.getValue()) {
                currentPlayer = 0;
                gameRunning = false;
                return;
            }
            else if(fields[2] == FieldStates.PLAYER2.getValue() && fields[4] == FieldStates.PLAYER2.getValue() && fields[6] == FieldStates.PLAYER2.getValue()) {
                currentPlayer = 1;
                gameRunning = false;
                return;
            }
        }
    }
    
    /**
     * Returns the winner of the game if the game is finished
     * @return the winner or -1 if the game is still running
     */
    public int getWinner() {
        if(!gameRunning) {
            return currentPlayer;
        }
        else {
            return -1;
        }
    }
    
    /**
     * Does a turn for the currently active player if it is a valid turn
     * @param field the field that a marker should be placed on
     * @return returns if the turn was a invalid(0), successful(1), unsuccessful(-1)
     * or winning(2) turn
     */
    public int doTurn(int field) {
        if(gameRunning) {
            boolean successfulTurn = false;

            // if the field is empty a marker can be placed
            if(fields[field] == -1) {
                fields[field] = currentPlayer;
                successfulTurn = true;
            }
            
            // give feedback for fitness function
            if(successfulTurn) {
                // checks if the last turn won the game
                checkWinner();
                // if so a special value gets returned
                // game gets stopped when a winner is recognized
                if(!gameRunning) {
                    return 2;
                }
                
                // change active player if the turn was successful
                currentPlayer = currentPlayer == 0 ? 1 : 0;
                return 1;
            }
            else {
                return -1;
            }
        }
        else{
            return 0;
        }
    }
    
    /**
     * Returns the current state of the field
     * @return current field
     */
    public int[] getField() {
        return fields;
    }
    
    public double[] getDoubleField() {
        double[] output = new double[fields.length];
        for (int i = 0; i < fields.length; i++) {
            output[i] = fields[i];
        }
        return output;
    }
    
    /**
     * Returns if the game is still running or already finshed
     * @return game state
     */
    public boolean isFinished() {
        return !gameRunning;
    }
    
    /**
     * Starts a new game by resetting the fields
     */
    public void startGame() {
        for (int i = 0; i < fields.length; i++) {
            fields[i] = -1;
        }
        gameRunning = true;
        currentPlayer = 0;
    }
}
