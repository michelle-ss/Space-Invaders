/* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * Game.java
 * The main program of Dining Room. Controls main gameplay.
 */

package spaceinvaderspackage;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.Graphics2D;

public class Game extends Canvas {


    private BufferStrategy strategy; // take advantage of accelerated graphics
    private boolean waitingForKeyPress = true; // true if game held up until
    // a key is pressed
    private boolean leftPressed = false; // true if left arrow key currently pressed
    private boolean rightPressed = false; // true if right arrow key currently pressed
    private boolean firePressed = false; // true if firing
    private boolean stayMenu = true;
    private boolean chooseDifficulty = false;
    private boolean play = false;
    private boolean introduction = false;
    private boolean gameRunning = true;
    private ArrayList entities = new ArrayList(); // list of entities                                                    // in game
    private ArrayList removeEntities = new ArrayList(); // list of entities
    // to remove this loop
    private Entity waitress; // the waitress
    private boolean firingInterval = true; // interval between foods (ms)
    private int tableCount; // # of tables left on screen
    private double differentX = 0;
    private double differentY = 0;
    private double angle = 90;
    private int menu = 0;
    private int useItem = 0;
    private int playerHp = 3;
    private int numOfPotatoTable = 0;
    private int numOfSushiTable = 0;
    private int difficulty = 2;
    private int chanceMiceEnter = 0;
    private int mouseSpeed = 0;
    private int mouseLocation = 0;
    private String tableName;
    private String winLose = "";
    private String message = "Play"; // message to display while waiting
    private String itemName;
    private String mouseName;

    //private Image floor = Toolkit.getDefaultToolkit().createImage("floor.jpg");
    //private Image floor1 = new Sprite(this, "sprites/floor.jpg", 400, 550);

    private boolean logicRequiredThisLoop = false; // true if logic
    // needs to be 
    // applied this loop

    /*
     * Construct our game and set it running.
     */
    public Game() {
        // create a frame to contain game
        JFrame container = new JFrame("Dining Room");

        // get hold the content of the frame
        JPanel panel = (JPanel) container.getContentPane();

        // set up the resolution of the game
        panel.setPreferredSize(new Dimension(1000, 700));
        panel.setLayout(null);

        // set up canvas size (this) and add to frame
        setBounds(0, 0, 1000, 700);
        panel.add(this);

        // Tell AWT not to bother repainting canvas since that will
        // be done using graphics acceleration
        setIgnoreRepaint(true);

        // make the window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);


        // if user closes window, shutdown game and jre
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            } // windowClosing
        });

        // add key listener to this canvas
        addKeyListener(new KeyInputHandler());

        // request focus so key events are handled by this canvas
        requestFocus();

        // create buffer strategy to take advantage of accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialize entities
        initEntities();

        // start the game
        gameLoop();
    } // constructor


    /* initEntities
     * input: none
     * output: none
     * purpose: Initialize the starting state of the waitress and table entities.
     *          Each entity will be added to the array of entities in the game.
     */
    private void initEntities() {

        // create the waitress and put in center of screen
        waitress = new WaitressEntity(this, "sprites/waitress.png", 400, 550);
        entities.add(waitress);

        // create a block of tables (3x4)
        numOfPotatoTable = 0;
        numOfSushiTable = 0;
        tableCount = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                if (Math.random() < 0.5) {
                    tableName = "sprites/potatoTable.png";
                    Entity table = new TableEntity(this, tableName,
                        100 + (col * 230),
                        50 + (row * 130));
                    entities.add(table);
                    table.setTableNum(0);
                    numOfPotatoTable++;
                    table.setId(0);
                } else {
                    tableName = "sprites/sushiTable.png";
                    Entity table = new TableEntity(this, tableName,
                        100 + (col * 230),
                        50 + (row * 130));
                    entities.add(table);
                    table.setTableNum(1);
                    numOfSushiTable++;
                    table.setId(0);
                }

                tableCount++;
            } // for
        } // outer for                         

    } // initEntities

    /* Notification from a game entity that the logic of the game
     * should be run at the next opportunity 
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    } // updateLogic

    /* Remove an entity from the game.  It will no longer be
     * moved or drawn.
     */
    public void removeEntity(Entity entity) {
        removeEntities.add(entity);
    } // removeEntity

    /* Notification that the player has died.
     */
    public void notifyDeath() {
        waitingForKeyPress = true;
        play = false;
        message = "Play Again";
        winLose = "You Lost! Try Again!";
        stayMenu = true;
        startGame();
    } // notifyDeath


    /* Notification that the player has served all tables
     */
    public void notifyWin() {
        waitingForKeyPress = true;
        play = false;
        message = "Play Again";
        winLose = "You Won! Good Job!";
        stayMenu = true;
        startGame();
    } // notifyWin

    /* Notification than a table has been served
     */
    public void notifyTableKilled() {
        tableCount--;
        if (tableCount == 0) {
            notifyWin();
        } // if


    } // notifytableKilled

    /* Attempt to fire.*/
    public void tryToFire() {
        // check that we've waited long enough to fire
        if (!firingInterval) {
            return;
        } // if

        // otherwise add a food

        firingInterval = false;

        if (useItem == 0) {
            itemName = "sprites/potato.png";
        } else if (useItem == 1) {
            itemName = "sprites/sushi.png";
        }

        FoodEntity food = new FoodEntity(this, itemName,
            waitress.getX() + 95, waitress.getY() + 20);
        entities.add(food);

        if (useItem == 0) {
            food.setFoodNum(0);
        } else if (useItem == 1) {
            food.setFoodNum(1);
        }

        if (numOfSushiTable != 0 && numOfPotatoTable != 0) {
            if (Math.random() < 0.5) {
                useItem = 0;
            } else {
                useItem = 1;
            } //else        	   
        } else if (numOfSushiTable == 0) {
            useItem = 0;
        } else if (numOfPotatoTable == 0) {
            useItem = 1;
        } //else

        differentX = 300 * (Math.cos(Math.toRadians(angle)));
        differentY = -300 * (Math.sin(Math.toRadians(angle)));


        food.setHorizontalMovement(differentX);
        food.setVerticalMovement(differentY);
    } // tryToFire


    public void boundSide(Object a) {
        ((Entity) a).setHorizontalMovement(-((Entity) a).getHorizontalMovement());
    } //boundSide

    public void boundTop(Object a) {
        ((Entity) a).setVerticalMovement(-((Entity) a).getVerticalMovement());
    } //boundTop

    public void miceEnter() {

        if (Math.random() < 0.5) {
            mouseName = "sprites/mouse1.png";
            mouseSpeed = 100;
            mouseLocation = 0;
        } else {
            mouseName = "sprites/mouse2.png";
            mouseSpeed = -100;
            mouseLocation = 1000;
        }

        Entity mouse = new MouseEntity(this, mouseName, mouseLocation, 450);
        entities.add(mouse);
        mouse.setId(1);
        mouse.setHorizontalMovement(mouseSpeed);
    } //miceEnter

    public void drawImage(Graphics g, String a, int b, int c) {
        ImageEntity Image = new ImageEntity(this, a, b, c);
        Image.draw(g);
    } //drawImage

    /*
     * gameLoop
     * input: none
     * output: none
     * purpose: Main game loop. Runs throughout game play.
     *          Responsible for the following activities:
     *           - calculates speed of the game loop to update moves
     *           - moves the game entities
     *           - draws the screen contents (entities, text)
     *           - updates game events
     *           - checks input
     */
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // keep loop running until game ends
        while (gameRunning) {

            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();


            // if waiting for "any key press", draw message
            if (stayMenu) {
                drawImage(g, "sprites/floor.png", 0, 0);

                g.setColor(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                drawImage(g, "sprites/TitleScreen.png", 0, 0);
                g.setColor(Color.pink);
                g.drawString(winLose, (430 - g.getFontMetrics().stringWidth(message)) / 2, 580);
                g.setColor(Color.white);
                g.drawString(message, (530 - g.getFontMetrics().stringWidth(message)) / 2, 630);
                g.drawString("Instructions", (1000 - g.getFontMetrics().stringWidth("Instructions")) / 2, 630);
                g.drawString("Quit", (1450 - g.getFontMetrics().stringWidth("Quit")) / 2, 630);

                if (menu > 2) {
                    menu = 0;
                } else if (menu < 0) {
                    menu = 2;
                } //else

                if (menu == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString(message, (530 - g.getFontMetrics().stringWidth(message)) / 2, 630);
                    if (!waitingForKeyPress) {
                        chooseDifficulty = true;
                        stayMenu = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } else if (menu == 1) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Instructions", (1000 - g.getFontMetrics().stringWidth("Instructions")) / 2, 630);
                    if (!waitingForKeyPress) {
                        introduction = true;
                        stayMenu = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } else if (menu == 2) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Quit", (1450 - g.getFontMetrics().stringWidth("Quit")) / 2, 630);
                    if (!waitingForKeyPress) {
                        System.exit(0);
                    }
                }


            } else if (chooseDifficulty) {
                drawImage(g, "sprites/floor.png", 0, 0);
                drawImage(g, "sprites/Difficulty.png", 0, 0);
                drawImage(g, "sprites/OpenSign.png", 200, 275);

                g.setColor(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                g.drawString("Easy", (400 - g.getFontMetrics().stringWidth("Easy")) / 2, 300);
                g.drawString("Medium", (800 - g.getFontMetrics().stringWidth("Medium")) / 2, 300);
                g.drawString("Hard", (1200 - g.getFontMetrics().stringWidth("Hard")) / 2, 300);
                g.drawString("Back", (1600 - g.getFontMetrics().stringWidth("Back")) / 2, 300);

                if (menu > 3) {
                    menu = 0;
                } else if (menu < 0) {
                    menu = 3;
                } //else

                if (menu == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Easy", (400 - g.getFontMetrics().stringWidth("Easy")) / 2, 300);
                    if (!waitingForKeyPress) {
                        difficulty = 0;
                        play = true;
                        chooseDifficulty = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } else if (menu == 1) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Medium", (800 - g.getFontMetrics().stringWidth("Medium")) / 2, 300);
                    if (!waitingForKeyPress) {
                        difficulty = 1;
                        play = true;
                        chooseDifficulty = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } else if (menu == 2) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Hard", (1200 - g.getFontMetrics().stringWidth("Hard")) / 2, 300);
                    if (!waitingForKeyPress) {
                        difficulty = 2;
                        play = true;
                        chooseDifficulty = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } else if (menu == 3) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString("Back", (1600 - g.getFontMetrics().stringWidth("Back")) / 2, 300);
                    if (!waitingForKeyPress) {
                        stayMenu = true;
                        chooseDifficulty = false;
                        waitingForKeyPress = true;
                        menu = 0;
                    }
                } //else

            } else if (play) {

                drawImage(g, "sprites/floor.png", 0, 0);

                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.move(delta);
                } // for


                // draw all entities
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.draw(g);
                } // for

                //draw line
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(5));

                if (angle < 50) {
                    g.drawLine(waitress.getX() + 103, waitress.getY() + 25, 1000, 575 - (int)(497 * Math.tan(Math.toRadians(angle))));
                } else if (angle > 130) {
                    g.drawLine(waitress.getX() + 103, waitress.getY() + 25, 0, 575 + (int)(497 * Math.tan(Math.toRadians(angle))));
                } else if (angle <= 130 && angle > 90) {
                    g.drawLine(waitress.getX() + 103, waitress.getY() + 25, 503 - (int)(575 * Math.tan(Math.toRadians(angle - 90))), 0);
                } else if (angle >= 50 && angle < 90) {
                    g.drawLine(waitress.getX() + 103, waitress.getY() + 25, 503 + (int)(575 * Math.tan(Math.toRadians(90 - angle))), 0);
                } else {
                    g.drawLine(waitress.getX() + 103, waitress.getY() + 25, 503, 0);
                } //else

                drawImage(g, "sprites/playerHp.png", 50, 660);
                g.setColor(Color.red);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                g.drawString("" + playerHp, 198, 682);

                if (difficulty != 0) {
                    drawImage(g, "sprites/wall.png", 350, 410);
                } //if

                if (difficulty == 2) {
                    chanceMiceEnter = (int)(100 * Math.random());

                    if (chanceMiceEnter < 2) {
                        miceEnter();
                    } //if
                } //if

                if (useItem == 0) {
                    itemName = "sprites/potato.png";
                } else if (useItem == 1) {
                    itemName = "sprites/sushi.png";
                } //else

                FoodEntity food = new FoodEntity(this, itemName,
                    455, 665);
                entities.add(food);
                food.setHorizontalMovement(0);
                food.setVerticalMovement(0);

                // change angle
                if ((leftPressed) && (!rightPressed)) {
                    angle += 3;
                } else if ((rightPressed) && (!leftPressed)) {
                    angle -= 3;
                } //else

                if (angle > 165) {
                    angle = 165;
                } else if (angle < 15) {
                    angle = 15;
                } //else

                //if spacebar pressed, try to fire
                if (firePressed) {
                    tryToFire();
                } //if          	  	

            } else if (introduction) {
                drawImage(g, "sprites/floor.png", 0, 0);
                drawImage(g, "sprites/Instuctions.png", 300, 2);
                g.setColor(Color.white);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
                g.drawString("Meet Sheila. Due to her crippling student loan debt, she had to get a job at this",
                    100, 230);
                g.drawString("restaurant. This restaurant is a bit... unconventional. It only serves sushi and",
                    100, 270);
                g.drawString("potatoes, and it has quite the rat problem - yikes! It is your job to help Sheila",
                    100, 310);
                g.drawString("serve everyone the right food, and watch out for the rats! Make sure you deliver",
                    100, 350);
                g.drawString("the right food to a table, or you will lose a life! Oh, and if food touches a rat,",
                    100, 390);
                g.drawString("you lose IMMEDIATELY! Have fun, and good luck!",
                    100, 430);
                g.drawString("Use the arrow keys to aim, and the space bar to shoot your dishes to the correct",
                    100, 470);
                g.drawString("table. You can bounce your food off the sides of the screen as well!",
                    100, 510);
                g.setColor(Color.LIGHT_GRAY);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                g.drawString("Back", (950 - g.getFontMetrics().stringWidth("Back")) / 2, 600);
                if (!waitingForKeyPress) {
                    waitingForKeyPress = true;
                    stayMenu = true;
                    introduction = false;
                }
            } //else

            // brute force collisions, compare every entity
            // against every other entity.  If any collisions
            // are detected notify both entities that it has
            // occurred
            for (int i = 0; i < entities.size(); i++) {
                for (int j = i + 1; j < entities.size(); j++) {
                    Entity me = (Entity) entities.get(i);
                    Entity him = (Entity) entities.get(j);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    } // if
                } // inner for
            } // outer for


            // run logic if required
            if (logicRequiredThisLoop) {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                } // for
                logicRequiredThisLoop = false;
            } // if            	

            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // remove dead entities
            entities.removeAll(removeEntities);
            removeEntities.clear();

            try {
                Thread.sleep(100);
            } catch (Exception e) {}

        } // while

    } // gameLoop


    /* startGame
     * input: none
     * output: none
     * purpose: start a fresh game, clear old data
     */
    private void startGame() {
        // clear out any existing entities and initalize a new set
        entities.clear();

        initEntities();

        // blank out any keyboard settings that might exist
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
        menu = 0;
        play = false;
        firingInterval = true;
        //upPressed = false;
        angle = 90;
        playerHp = 3;

    } // startGame


    /* inner class KeyInputHandler
     * handles keyboard input from the user
     */
    private class KeyInputHandler extends KeyAdapter {

        /* The following methods are required
         * for any class that extends the abstract
         * class KeyAdapter.  They handle keyPressed,
         * keyReleased and keyTyped events.
         */
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                waitingForKeyPress = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            } // if


            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                menu--;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                menu++;
            }


        } // keyPressed

        public void keyReleased(KeyEvent e) {

            // respond to move left, right or fire
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            } // if
        } // keyReleased

    } // class KeyInputHandler

    public void setFiringInterval() {
        firingInterval = true;
    }

    public void setPlayerHp() {
        playerHp--;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    public void setPotatoNum() {
        numOfPotatoTable--;
    }

    public int getPotatoNum() {
        return numOfPotatoTable;
    }

    public void setSushiNum() {
        numOfSushiTable--;
    }

    public int getSushiNum() {
        return numOfSushiTable;
    }

    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Main Program
     */
    public static void main(String[] args) {
        // instantiate this object
        new Game();
    } // main
} // Game