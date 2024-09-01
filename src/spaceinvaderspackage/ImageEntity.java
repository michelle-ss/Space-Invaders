/* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * ImageEntity.java
 * Creates an entity used to add images to the game
 */

package spaceinvaderspackage;

public class ImageEntity extends Entity {

    private Game game; // the game in which the waitress exists

    // construct the Image
    public ImageEntity(Game g, String r, int newX, int newY) {
        super(r, newX, newY); // calls the constructor in Entity
        game = g;
    } // constructor

    //compatible with Entity
    public void collidedWith(Entity other) {

    } // collidedWith    

} // ImageEntity class