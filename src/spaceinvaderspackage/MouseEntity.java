/* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * MouseEntity.java
 * Represents the mouse enemy
 */
package spaceinvaderspackage;

public class MouseEntity extends Entity {

    private Game game; // the game in which the ship exists

    public MouseEntity(Game g, String r, int newX, int newY) {
        super(r, newX, newY); // calls the constructor in Entity
        game = g;
    } // constructor

    /* move
     * input: delta - time elapsed since last move (ms)
     * purpose: move shot
     */

    public void move(long delta) {

        super.move(delta);

        if (x > 1100) {
            game.removeEntity(this);
        } //if

        if (x < -10) {
            game.removeEntity(this);
        } //if
    } // move

    //compatible with Entity
    public void collidedWith(Entity other) {


    } // collidedWith



} // MouseEntity class