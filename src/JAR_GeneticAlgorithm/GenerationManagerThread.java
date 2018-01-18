/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JAR_GeneticAlgorithm;

import JumpAndRun.JAR_Game;

/**
 *  Runs the generations manager beside the main game
 * @author Dominik Onyszkiewicz
 */
public class GenerationManagerThread extends Thread {
    @Override
    public void run() {
        new GenerationManager().run();
    }
}
