/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package General;

/**
 *  Stores two doubles to manage a point of two decimal numbers
 * @author Dominik Onyszkiewicz
 */
public class Vector2 {
    private double x, y;
    
    public Vector2() {
        x = 0;
        y = 0;
    }
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns the value of the x coordinate
     * @return 
     */
    public double getX() {
        return x;
    }
    
    /**
     * Returns the value of the x coordinate as an integer
     * @return 
     */
    public int getIntX() {
        return (int) Math.round(x);
    }
    
    /**
     * Sets the x coordinate to a new given value
     * @param newX 
     */
    public Vector2 setX(double newX) {
        x = newX;
        return this;
    }
    
    /**
     * Adds a specific value to the x coordinate
     */
    public void addX(double value) {
        x += value;
    } 
    
    /**
     * Returns the value of the y coordinate
     * @return 
     */
    public double getY() {
        return y;
    }
    
    /**
     * Returns the value of the y coordinate as an integer
     * @return 
     */
    public int getIntY() {
        return (int) Math.round(y);
    }
    
    /**
     * Sets the y coordinate to new given value
     * @param newY 
     */
    public Vector2 setY(double newY) {
        y = newY;
        return this;
    }
    
    /**
     * Adds a specific value to the y coordinate
     */
    public void addY(double value) {
        y += value;
    } 
    
    /**
     * Returns the length of the vector
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Multiplicates the vector with the given value and returns the result
     */
    public Vector2 multiply(double value) {
        return new Vector2(x * value, y * value);
    }
    
    /**
     * Returns the scalar product/dot product of this vector and the given Vector
     * @return 
     */
    public double scalarProduct(Vector2 vec) {
        return (x * vec.getX() + y * vec.getY());
    }
    
    /**
     * Returns the angle between this and the given vector
     * @return 
     */
    public double getAngleTo(Vector2 vec) {
        double out = Math.toDegrees(Math.acos(scalarProduct(vec) / (getLength() * vec.getLength())));
        return out;
    }
    
    @Override
    public String toString() {
        return x + " | " + y;
    }
    
}
