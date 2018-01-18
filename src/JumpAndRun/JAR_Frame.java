/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JumpAndRun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *  Manages the visual representation and interaction of the game
 * @author Dominik Onyszkiewicz
 */
public class JAR_Frame extends JFrame {
    
    private Screen screen;
    private boolean bLeftPressed, bRightPressed, bJumpPressed;
    private JAR_Game game;
    
    // a way of "moving aroundv" in the scene by displacing the environment
    private int viewOffsetX, viewOffsetY;
    
    public JAR_Frame(JAR_Game game) {
        super("Jump and Run");
        
        this.game = game;
        
        screen = new Screen();
        screen.setBounds(0, 0, 750, 750);
        add(screen);
        addKeyListener(new InputHandler());
        
        bLeftPressed = false;
        bRightPressed = false;
        bJumpPressed = false;
        
        viewOffsetX = 0;
        
        for(Entity block : game.getBlocks()) {
            screen.add(block.getLabel());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="button state getter">
    public boolean isbLeftPressed() {
        return bLeftPressed;
    }

    public boolean isbRightPressed() {
        return bRightPressed;
    }

    public boolean isbJumpPressed() {
        return bJumpPressed;
    }
//</editor-fold>
    
    /**
     * Repaints the content of the screen
     */
    public void repaintScreen() {   
        this.repaint();
        screen.repaint();
    }
    
    private class Screen extends JLabel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // paint the background in dark grey
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if(game.isRunning()) {
                for(Entity block : game.getBlocks()) {
                    block.getLabel().setVisible(false);
                }
                // paint the environment
                g.setColor(Color.LIGHT_GRAY);
                for(Entity block : game.getVisibleBlocks()) {
                    g.fillRect(block.getPosition().getIntX() - viewOffsetX, 
                            block.getPosition().getIntY() - viewOffsetY, 
                            block.getWidth(), 
                            block.getHeight());
                    
                    // XXX: write labels on visible blocks (for debugging purposes)
                    //block.getLabel().setLocation(block.getPosition().getIntX() - viewOffsetX, block.getPosition().getIntY() - viewOffsetY);
                    //block.getLabel().setVisible(true);
                }

                // paint the player
                g.setColor(Color.RED);
                g.fillRect(game.getPlayer().getPosition().getIntX() - viewOffsetX, 
                        game.getPlayer().getPosition().getIntY() - viewOffsetY, 
                        game.getPlayer().getWidth(), 
                        game.getPlayer().getHeight());
            }
        }
    }
    
    private class InputHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_A) {
                bLeftPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_D) {
                bRightPressed = true;
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                bJumpPressed = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_A) {
                bLeftPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_D) {
                bRightPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                bJumpPressed = false;
            }
            if(e.getKeyCode() == KeyEvent.VK_R) {
                game.restart();
            }
        }
        
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter/setter">
    
    /**
     * Returns the view offset on the x axis
     */
    public int getViewOffsetX() {
        return viewOffsetX;
    }
    
    /**
     * Sets a new view offset on the x axis
     * @param newOffset 
     */
    public void setViewOffsetX(int newOffset) {
        viewOffsetX = newOffset;
    }
    
    /**
     * Displaces the view offset on the x axis with the given amount
     */
    public void changeViewOffsetX(int amount) {
        viewOffsetX += amount;
    }
    
    /**
     * Returns the view offset on the y axis
     */
    public int getViewOffsetY() {
        return viewOffsetY;
    }
    
    /**
     * Sets a new view offset on the y axis
     * @param newOffset 
     */
    public void setViewOffsetY(int newOffset) {
        viewOffsetY = newOffset;
    }
    
    /**
     * Displaces the view offset on the y axis with the given amount
     */
    public void changeViewOffsetY(int amount) {
        viewOffsetY += amount;
    }
    
    /**
     * Returns the screen object with the drawn content of the game
     */
    public Screen getScreen() {
        return screen;
    }
//</editor-fold>
    
}
