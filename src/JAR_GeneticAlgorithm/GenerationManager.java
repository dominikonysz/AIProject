/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JAR_GeneticAlgorithm;

import JumpAndRun.JAR_Game;
import java.util.ArrayList;
import java.util.List;

/**
 *  Manages the basic creation and mutation of generations
 * @author Dominik Onyszkiewicz
 */
public class GenerationManager {
    private List<Generation> generations;
    private int generationSize;
    private GenerationAnalysis genAna;
    
    public GenerationManager() {
        generations = new ArrayList<Generation>();
        generationSize = 20;
        generations.add(new Generation(0, generationSize));
        genAna = new GenerationAnalysis(this);
    }
    
    public void run() {
        if(JAR_Game.getGame().isAIActive()) {
            while(true) {
                Generation gen = generations.get(generations.size() - 1);
                gen.computeFitnesses();
                genAna.repaint();
                generations.add(new Generation(gen));
            }
        }
    }
    
    public List<Generation> getGenerations() {
        return generations;
    }
}
