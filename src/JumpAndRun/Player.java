/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JumpAndRun;

import General.Gauss;
import java.awt.Dimension;
import General.Vector2;
import java.util.List;

/**
 *  Represents a player with appropriate attributes/methods such as gravity
 * @author Dominik Onyszkiewicz
 */
public class Player extends Entity {
    
    private enum CollisionType {
        NONE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
    
    private boolean grounded;
    private Vector2 startPos, bestPos, velocity, maxVelocity, acceleration;
    CollisionType[] colType;
    
    public Player(String tag, Vector2 startingPosition, Dimension size) {
        super(tag, startingPosition, size);
        velocity = new Vector2(0, 0);
        maxVelocity = new Vector2(8, 10);
        acceleration = new Vector2(2, 0.15);
        startPos = new Vector2(startingPosition.getX(), startingPosition.getY());
        
        colType = new CollisionType[] { CollisionType.NONE, CollisionType.NONE };
        grounded = false;
        
        bestPos = new Vector2(startPos.getX(), startPos.getY());
    }
    
    /**
     * Returns the max velocity on the x axis of the player object
     * @return 
     */
    public double getXVelocity() {
        return velocity.getX();
    }
    
    /**
     * Returns the max velocity on the y axis of the player object
     * @return 
     */
    public double getYVelocity() {
        return velocity.getY();
    }
        
    public void move(boolean moveLeft, boolean moveRight, boolean jump, double movementPercent) {
        defineMovement(moveLeft, moveRight, jump);
        
        velocity = velocity.multiply(movementPercent);
        grounded = false;
        
        position.addX(velocity.getX());
        position.addY(velocity.getY());
        
        handleCollision();
    }
    
    /**
     * Handles the player input by defining different values depending on the button states
     */
    private void defineMovement(boolean moveLeft, boolean moveRight, boolean jump) {
        if(moveLeft && !moveRight) {
            if(velocity.getX() > -maxVelocity.getX()) {
                velocity.setX(velocity.getX() - acceleration.getX());
            }
            else {
                velocity.setX(-maxVelocity.getX());
            }
        }
        else if(moveRight && !moveLeft) {
            if(velocity.getX() < maxVelocity.getX()) {
                velocity.setX(velocity.getX() + acceleration.getX());
            }
            else {
                velocity.setX(maxVelocity.getX());
            }
        }
        else {
            velocity.setX(0);
        }
        if(jump && grounded) {
            velocity.setY(-9);
        }
        else if(grounded) {
            velocity.setY(0);
        }
        else if(!grounded && velocity.getY() < maxVelocity.getY()) {
            if(velocity.getY() < maxVelocity.getY()) {
                velocity.setY(velocity.getY() + acceleration.getY());
            }
            else {
                velocity.setY(maxVelocity.getY());
            }
        }
    }
    
    /**
     * Handles the player collision
     */
    private void handleCollision() {
        double xOverlap, yOverlap;
        // collision detection for all visible entities
        for(Entity entity : JAR_Game.getGame().getVisibleBlocks()) {
            
            if(collidesWith(position, entity)) {
                xOverlap = velocity.getX() > 0 ? entity.getPosition().getX() - (position.getX() + getWidth()) : entity.getPosition().getX() + entity.getWidth() - position.getX();
                yOverlap = velocity.getY() > 0 ? entity.getPosition().getY() - (position.getY() + getHeight()) : entity.getPosition().getY() + entity.getHeight() - position.getY();
                if(Math.abs(yOverlap) < Math.abs(xOverlap)) {
                    position.addY(yOverlap);
                    if(yOverlap < 0) {
                        grounded = true;
                    }
                    velocity.setY(0);
                } 
                else { 
                    position.addX(xOverlap); 
                }
            }
            
        }
    }
    
    /**
     * Returns if the player will hit the given entity in the next move and if yes, when this is going to happen
     */
    private Vector2 collidesInMovement(Entity entity) {
        return null;
    }
    
    
    /**
     * Returns if the player object is colliding with the given entity
     */
    private boolean collidesWith(Vector2 pos, Entity entity) {
        return (pos.getX() > entity.getPosition().getX() - getWidth()
                            && pos.getX() < entity.getPosition().getX() + entity.getWidth())
                            && (pos.getY() > entity.getPosition().getY() - getHeight()
                            && pos.getY() < entity.getPosition().getY() + entity.getHeight());
    }
    
    /**
     * Checks if the given point inside of the entity
     */
    private boolean isPointInside(Vector2 point, Entity entity) {
        return (point.getX() >= entity.getPosition().getX()
                            && point.getX() <= entity.getPosition().getX() + entity.getWidth())
                            && (point.getY() >= entity.getPosition().getY()
                            && point.getY() <= entity.getPosition().getY() + entity.getHeight());
    }
    
    /**
     * Sets the velocity to its default value
     */
    public void resetVelocity() {
        velocity.setX(0)
                .setY(0);
        grounded = false;
    }
    
    /**
     * Returns the starting position of the player object
     */
    public Vector2 getStartingPosition() {
        return startPos;
    }
    
    /**
     * Returns the players best achieved position
     */
    public Vector2 getBestPosition() {
        return bestPos;
    }
    
    /**
     * Resets the players values, which are used to determine the fitness
     */
    public void resetValues() {
        bestPos.setX(startPos.getX())
                .setY(startPos.getY());
    }
}
