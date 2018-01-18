/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import JumpAndRun.JAR_Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *  Displays a given neural network in a new window
 * @author Dominik Onyszkiewicz
 */
public class NetworkVisualization extends JFrame {
    
    private NeuralNet nn;
    private static NetworkVisualization visual;
    private JAR_Game game;
    private NetworkPanel panel;
    
    private NetworkVisualization(NeuralNet neuralNet) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 550);
        this.setLocation(0, 0);
        this.setVisible(true);
        
        nn = neuralNet;
        panel = new NetworkPanel();
        panel.setBounds(0, 0, getWidth(), getHeight());
        add(panel);
        
        game = JAR_Game.getGame();
        
        
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "un-pause");
        panel.getActionMap().put("un-pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(game.isRunning()) {
                    game.pause();
                }
                else {
                    game.start();
                }
            }
        });
    }
    
    public static NetworkVisualization getFrame() {
        return visual;
    }
    
    public static NetworkVisualization getFrame(NeuralNet neuralNet) {
        if(visual == null) {
            visual = new NetworkVisualization(neuralNet);
        }
        return visual;
    }
    
    /**
     * Receives a new neural net and changes the displayed neural net to the given one
     * @param neuralNet 
     */
    public void updateNetwork(NeuralNet neuralNet) {
        nn = neuralNet;
        repaint();
    }
    
    private class NetworkPanel extends JPanel {
        JLabel idLabel;
        
        public NetworkPanel() {
            super();
            idLabel = new JLabel();
            idLabel.setBounds(5, 5, 50, 30);
            this.add(idLabel);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            idLabel.setText("Network ID: " + nn.getNetworkId() + " | Fitness: " + nn.getFitness());
            
            int inputNeuronXOffset = 30, xOffset = 60, yOffset = 30, neuronSize = 14;
            this.setSize(visual.getWidth(), (nn.iLayer.length + 2) * yOffset);
            
            int neuronX, neuronY;
            
            nn.correctIds();
            
            // background
            g.setColor(new Color(0.5f, 0.5f, 0.5f));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            // input layer
            for (int i = 0; i < nn.iLayer.length; i++) {
                neuronX = inputNeuronXOffset * (i%15 + 1);
                neuronY = yOffset * (i/15 + 1);
                // neuron
                if(i == 112) {
                    g.setColor(Color.RED);
                }
                else {
                    int[] view = JAR_Game.getGame().getAIPlayer().getView();
                    if(view[i] == 0) {
                        g.setColor(Color.WHITE);
                    }
                    else {
                        g.setColor(Color.BLACK);
                    }
                }
                g.fillRect(neuronX, neuronY, neuronSize, neuronSize);
                
                // connections
                for (int c = 0; c < nn.iLayer[i].getConnections().size(); c++) {
                    Connection con = nn.iLayer[i].getConnections().get(c);
                    if(con.getWeight() > 0) {
                        g.setColor(Color.GREEN);
                    }
                    else if(con.getWeight() < 0) {
                        g.setColor(Color.RED);
                    }
                    Neuron target = con.getTarget();
                    if(target != null) {
                        int targetX = target.getLayerId() == Integer.MAX_VALUE ? nn.hLayers.size() : target.getLayerId();
                        targetX = (16 * inputNeuronXOffset) + (targetX+1) * xOffset;
                        int targetY = (target.getId()+1) * yOffset;
                        if(target.getLayerId() < nn.hLayers.size()) {
                            targetY += (int) Math.round((15 - nn.hLayers.get(target.getLayerId()).size()) / 2 * yOffset);
                        }
                        else {
                            targetY += 6 * yOffset;
                        }
                        g.drawLine(neuronX + (neuronSize/2), neuronY + (neuronSize/2), targetX + (neuronSize/2), targetY + (neuronSize/2));
                    }
                }
            }
            
            // hidden layers
            for (int i = 0; i < nn.hLayers.size(); i++) {
                for(int j = 0; j < nn.hLayers.get(i).size(); j++) {
                    // TODO: change the layout of the displayed neural net
                    
                    int layerSize = nn.hLayers.get(i).size();
                    double diff = (15 - layerSize) / 2;
                    int pixelDiff = (int) Math.round(diff * yOffset);
                    
                    neuronX = (16 * inputNeuronXOffset) + (i+1) * xOffset;
                    neuronY = (j+1) * yOffset + pixelDiff;
                    // neuron
                    double neuronOut = nn.hLayers.get(i).get(j).getNeuronOutput();
                    g.setColor(new Color(0.53f, 0.53f, 0.53f));
                    g.drawRect(neuronX, neuronY, neuronSize, neuronSize);
                    g.setColor(new Color((float) (1 - neuronOut), (float) (1 - neuronOut), (float) (1 - neuronOut)));
                    g.fillRect(neuronX + 1, neuronY + 1, neuronSize - 1, neuronSize - 1);
                    
                    // connections
                    for (int c = 0; c < nn.hLayers.get(i).get(j).getConnections().size(); c++) {
                        Connection con = nn.hLayers.get(i).get(j).getConnections().get(c);
                        if(con.getWeight() > 0) {
                            g.setColor(Color.GREEN);
                        }
                        else if(con.getWeight() < 0) {
                            g.setColor(Color.RED);
                        }
                        Neuron target = con.getTarget();
                        int targetX = target.getLayerId() == Integer.MAX_VALUE ? nn.hLayers.size() : target.getLayerId();
                        targetX = (16 * inputNeuronXOffset) + ((targetX+1) * xOffset);
                        int targetY = ((target.getId()+1) * yOffset);
                        if(target.getLayerId() < nn.hLayers.size()) {
                            targetY += (int) Math.round((15 - nn.hLayers.get(target.getLayerId()).size()) / 2 * yOffset);
                        }
                        else {
                            targetY += 6 * yOffset;
                        }
                        g.drawLine(neuronX + (neuronSize/2), neuronY + (neuronSize/2), targetX + (neuronSize/2), targetY + (neuronSize/2));
                    }
                }
            }
            
            // output layer
            int[] lastOut = game.getAIPlayer().getLastOut();
            for (int i = 0; i < nn.oLayer.length; i++) {
                
                
                
                // neuron
                if(lastOut[i] == 0) {
                    g.setColor(Color.WHITE);
                }
                else if(lastOut[i] == 1) {
                    g.setColor(Color.BLACK);
                }
                g.fillRect((16 * inputNeuronXOffset) + (nn.hLayers.size()+1) * xOffset, (i+7) * yOffset, neuronSize, neuronSize);
            }
        }
    }
}
