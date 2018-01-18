/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package General;

import JAR_GeneticAlgorithm.GenerationManagerThread;
import JumpAndRun.JAR_Game;

/**
 *  A general test class to test single elements of the project
 * @author Dominik Onyszkiewicz
 */
public class TestClass {
    public static void main(String[] args){
        
        JAR_Game game = JAR_Game.getGame();
        GenerationManagerThread genMan = new GenerationManagerThread();
        genMan.start();
        //GenerationManagerThread genMan2 = new GenerationManagerThread();
        //genMan2.start();
        game.run();
        
    }
}
