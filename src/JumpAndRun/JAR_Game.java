/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JumpAndRun;

import java.awt.Dimension;
import General.Vector2;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The game logic of a Jump-and-Run game
 * @author Dominik Onyszkiewicz
 */
public class JAR_Game {
    
    private boolean running;
    
    private Player player;
    private List<Entity> blocks;
    
    private JAR_AIPlayer aiplayer;
    private boolean aiactive, left, right, jump;
    
    private long lastFrame;
    // how long has a frame to be to reach 60 frames per second
    private double fps;
    private double millisPerFrame; 
    // how much was the time between the frames bigger than expected
    private double movementPercent;
    
    private Vector2 levelBounds;
    
    private JAR_Frame frame;
    
    private static JAR_Game game;
    
    public static JAR_Game getGame() {
        if(game == null) {
            game = new JAR_Game();
        }
        return game;
    }
    
    private JAR_Game() {
        load();
    }
    
    /**
     * Loads the single components that are needed to run the game
     */
    public void load() {
        
        fps = 60;
        millisPerFrame = 1000/fps;
        
        running = false;
        
        player = new Player("Player", new Vector2(350, 350), new Dimension(50,50));
        /*
        blocks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            blocks.add(new Entity("Block " + i, new Vector2(i * 50, 470), new Dimension(50,50)));
        }
        blocks.get(13).setPosition(new Vector2(blocks.get(13).getPosition().getX(), blocks.get(13).getPosition().getY() - 100));
        blocks.get(14).setPosition(new Vector2(blocks.get(14).getPosition().getX(), blocks.get(14).getPosition().getY() - 100));
        blocks.get(18).setPosition(new Vector2(250, 270));
        blocks.get(19).setPosition(new Vector2(200, 370));*/
        
        // toggle to activate/deactivate the ai player
        aiactive = true;
        left = false;
        right = false;
        jump = false;
        
        levelBounds = new Vector2();
        loadLevel(0);
        
        frame = new JAR_Frame(this);
        
        // Create window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(750, 750));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
    
    /**
     * Runs the game: calculates the single frames and paints them
     */
    public void run() {
        this.running = true;
        
        if(aiplayer != null) {
            aiplayer.startProgressDetection();
        }
        
        long thisFrame;
        lastFrame = System.currentTimeMillis();
        while(true) {
            if(running) {
                thisFrame = System.currentTimeMillis();
                movementPercent = (thisFrame - lastFrame) / millisPerFrame;
                if(!aiactive) {
                    doMovement();
                }
                if(aiplayer != null && aiactive) {
                    aiplayer.doAction();
                }

                frame.repaintScreen();

                lastFrame = thisFrame;
            }
            else {
                lastFrame = System.currentTimeMillis();
            }
            
            try {
                Thread.sleep(Math.round(millisPerFrame - 0.5));
            } catch (InterruptedException ex) {
                
            }
        }
    }
    
    /**
     * Restarts the game by setting all values to default
     */
    public void restart() {
        frame.setViewOffsetX(0);
        player.setPosition(new Vector2(350, 350));
        player.resetVelocity();
    }
    
    /**
     * Does a specific movement based on the pressed button
     */
    private void doMovement() {
        if(frame.isbLeftPressed()) {
            left = true;
        }
        if(frame.isbRightPressed() || right) {
            right = true;
        }
        if(frame.isbJumpPressed() || jump) {
            jump = true;
        }
        
        player.move(left, right, jump, movementPercent);
        correctCamera();
        
        left = false;
        right = false;
        jump = false;
    }
    
    /**
     * Does an action on the player depending on the input value
     * @param input 
     */
    public void doAction(int[] input) {
        player.move(input[0] == 1, input[1] == 1, input[2] == 1, movementPercent);
        correctCamera();
    }
    
    /**
     * Returns the list of blocks in the scene
     */
    public List<Entity> getBlocks() {
        return blocks;
    }
    
    /**
     * Returns the list of blocks in the scene as an entity array
     */
    public Entity[] getBlocksArray() {
        Entity[] array = new Entity[blocks.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = blocks.get(i);
        }
        return array;
    }
    
    /**
     * Returns the player entity
     * @return 
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Returns if the game is running
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Returns the frame of the game
     */
    public JAR_Frame getFrame() {
        return frame;
    }
    
    /**
     * Sets the ai player of the game to the given ai player
     * @param aiplayer 
     */
    public void setAIPlayer(JAR_AIPlayer aiplayer) {
        this.aiplayer = aiplayer;
    }
    
    /**
     * Loads a lavel from an image on the given path
     */
    public void loadLevelFromImage(String path) {
        BufferedImage img = null;
        try {
            File f = new File(path);
            img = ImageIO.read(f);
        }
        catch(IOException e) {
            System.out.println(e);
        }
        blocks = new ArrayList<>();
        int counter = 0;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = new Color(img.getRGB(x, y), true);
                if(color.getBlue() < 10 && color.getGreen() < 10 && color.getRed() < 10) {
                    blocks.add(new Entity("B: " + counter, new Vector2(x * 50, y * 50), new Dimension(50, 50)));
                    counter++;
                }
            }
        }
    }
    
    /**
     * Returns if the AI player is active
     */
    public boolean isAIActive() {
        return aiactive;
    }
    
    /**
     * Corrects the camera offset depending on the player position
     */
    public void correctCamera() {
        frame.setViewOffsetX((int) Math.round(player.getPosition().getX() - player.getStartingPosition().getX()));
        frame.setViewOffsetY(player.position.getIntY() - 350);
    }

    /**
     * Returns the current ai player
     * @return 
     */
    public JAR_AIPlayer getAIPlayer() {
        return aiplayer;
    }
    
    /**
     * Returns a list of visible blocks (standard window)
     */
    public List<Entity> getVisibleBlocks() {
        List<Entity> visibleBlocks = new ArrayList<>();
        
        Entity[] blocks = getBlocksArray();
        int xOffset = getFrame().getViewOffsetX();
        int yOffset = getFrame().getViewOffsetY();
        for (int i = 0; i < blocks.length; i++) {
            Entity block = blocks[i];
            // check if the block is visible for the player
            if((block.position.getIntX() - xOffset > -50)
                    && (block.position.getIntX() - xOffset < 750)
                    && (block.position.getIntY() - yOffset > -50)
                    && (block.position.getIntY() - yOffset < 750))
            {
                // if yes: add it to the visible blocks
                visibleBlocks.add(block);
            }
        }
        
        return visibleBlocks;
    }
    
    /**
     * pauses the game
     */
    public void pause() {
        running = false;
    }
    
    /**
     * Starts the game out of the pause mode
     */
    public void start() {
        running = true;
        aiplayer.startProgressDetection();
    }
    
    /**
     * Returns the boundaries of the current level
     */
    public Vector2 getLevelBounds() {
        return levelBounds;
    }
    
    /**
     * Sets the currently playable level to the level indicated by the given id
     */
    public void loadLevel(int id) {
        String filePath;
        System.out.println("LOADING LEVEL: " + id);
        switch(id)
        {
            case 1:
                filePath = "/src/General/Level1.jpg";
                break;
            case 2:
                filePath = "/src/General/Level2.jpg";
                break;
            case 3:
                filePath = "/src/General/Level3.jpg";
                break;
            default:
                filePath = "/src/General/Level2.jpg";
                break;
        }
        
        String basePath = new File("").getAbsolutePath();
        String path = basePath + filePath;
        loadLevelFromImage(path);
        
        Vector2 blockPos;
        for (Entity block : blocks) {
            blockPos = block.getPosition();
            if(blockPos.getX() > levelBounds.getX()) {
                levelBounds.setX(blockPos.getX() + block.getWidth());
            }
            if(blockPos.getY() > levelBounds.getY()) {
                levelBounds.setY(blockPos.getY() + block.getHeight());
            }
        }
    }
}
