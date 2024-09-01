/* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * WaitressEntity.java
 * Represents the waitress
 */

package spaceinvaderspackage;

public class WaitressEntity extends Entity {

    private Game game; // the game in which the Waitress exists

    /* construct the Waitress
     * input: game - the game in which the Waitress is being created
     *        ref - a string with the name of the image associated to
     *              the sprite for the Waitress
     *        x, y - initial location of Waitress
     */
    public WaitressEntity(Game g, String r, int newX, int newY) {
        super(r, newX, newY); // calls the constructor in Entity
        game = g;
    } // constructor


    public void move(long delta) {

    } // move


    /* collidedWith
     * input: other - the entity with which the Waitress has collided
     * purpose: notification that the Waitress has collided
     *          with something
     */
    public void collidedWith(Entity other) {
        if (other instanceof TableEntity) {
            game.notifyDeath();
        } // if
    } // collidedWith    

} // WaitressEntity class