/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JumpAndRun;

import java.awt.Dimension;
import General.Vector2;
import javax.swing.JLabel;

/**
 * Manages properties of an enitity (player, enemy etc.) and ckecks collision
 * @author Dominik Onyszkiewicz
 */
public class Entity {
    
    protected String tag;
    protected Vector2 position;
    protected Dimension collider;
    private JLabel label;
    
    public Entity(String tag, Vector2 startingPosition, Dimension size) {
        this.tag = tag;
        this.position = startingPosition;
        collider = size;
        label = new JLabel(tag);
        label.setSize(50, 50);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public void setPosition(Vector2 pos) {
        position = pos;
    }
    
    public Dimension getCollider() {
        return collider;
    }
    
    public void setCollider(Dimension col) {
        collider = col;
    }
    
    public int getWidth() {
        return (int) collider.getWidth();
    }
    
    public int getHeight() {
        return (int) collider.getHeight();
    }
    
    public JLabel getLabel() {
        return label;
    }
    
    public void setLabel(JLabel label) {
        this.label = label;
    }
    //</editor-fold>
}
