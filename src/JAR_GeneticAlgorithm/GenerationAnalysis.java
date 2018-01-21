/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JAR_GeneticAlgorithm;

import General.Vector2;
import JumpAndRun.JAR_Game;
import NeuralNetwork.Connection;
import NeuralNetwork.Neuron;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *  Displays diagramms to analyse the development of generations
 * @author Dominik Onyszkiewicz
 */
public class GenerationAnalysis extends JFrame {
    GenerationManager genMan;
    Vector2 diaSize;
    
    public GenerationAnalysis(GenerationManager gm) {
        super("Generation Analysis");
        
        this.setLayout(null);
        
        genMan = gm;
        diaSize = new Vector2(400, 400);
        
        FitnessDiagramm fDia = new FitnessDiagramm();
        fDia.setLocation(20, 20);
        fDia.setSize(new Dimension(diaSize.getIntX() + 1, diaSize.getIntY() + 1));
        this.add(fDia);
        
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // position the window on the upper right corner of the screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX() - this.getWidth();
        this.setLocation(x, 0);
        this.setVisible(true);
    }
    
    private class FitnessDiagramm extends JPanel {
        public FitnessDiagramm() {
            super();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.out.println("REPAINT");
            int maxFitness = 1;
            List<Generation> generations = genMan.getGenerations();
            for(Generation gen : generations) {
                if(gen.getMaxFitness() > maxFitness) {
                    maxFitness = gen.getMaxFitness();
                }
            }
            g.drawLine(0, 0, 0, diaSize.getIntY());
            g.drawLine(0, diaSize.getIntY(), diaSize.getIntX(), diaSize.getIntY());
            
            int genAmount = generations.size();
            Vector2 scale = new Vector2(diaSize.getX() / genAmount, diaSize.getY() / maxFitness);

            int[] xPoint = new int[genAmount];
            for (int i = 0; i < xPoint.length; i++) {
                xPoint[i] = (i+1) * scale.getIntX();
            }
            if(genAmount > 0) {
                g.setColor(Color.GREEN);
                g.drawLine(0, yCoordinate(0), scale.getIntX(), yCoordinate((int) Math.round(generations.get(0).getMaxFitness() * scale.getY())));
                g.setColor(Color.BLUE);
                g.drawLine(0, yCoordinate(0), scale.getIntX(), yCoordinate((int) Math.round(generations.get(0).getMaxFitness() / generations.get(0).getSize() * scale.getY())));
            }
            for (int i = 0; i < genAmount - 2; i++) {
                g.setColor(Color.GREEN);
                g.drawLine((i + 1) * scale.getIntX(), yCoordinate((int) Math.round(generations.get(i).getMaxFitness() * scale.getY())), 
                        (i + 2) * scale.getIntX(), yCoordinate((int) Math.round(generations.get(i + 1).getMaxFitness() * scale.getY())));
                g.setColor(Color.BLUE);
                g.drawLine((i + 1) * scale.getIntX(), yCoordinate((int) Math.round(generations.get(i).getAverageFitness() * scale.getY())), 
                        (i + 2) * scale.getIntX(), yCoordinate((int) Math.round(generations.get(i + 1).getAverageFitness() * scale.getY())));
            }
        }
        
        private int yCoordinate(int coord) {
            return diaSize.getIntY() - coord;
       }
    }
}
