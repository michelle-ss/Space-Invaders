 /* 2018-04-17
  * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
  * FoodEntity.java
  * Represents the food the waitress serves
  */

 package spaceinvaderspackage;

 public class FoodEntity extends Entity {

     private Game game; // the game in which the ship exists

     /* construct the food
      * input: game - the game in which the food is being created
      *        ref - a string with the name of the image associated to
      *              the sprite for the food
      *        x, y - initial location of food
      */
     public FoodEntity(Game g, String r, int newX, int newY) {
         super(r, newX, newY); // calls the constructor in Entity
         game = g;
     } // constructor

     /* move
      * input: delta - time elapsed since last move (ms)
      * purpose: move Food
      */
     public void move(long delta) {
         super.move(delta); // calls the move method in Entity

         if (y > 700) {
             game.removeEntity(this);
             game.setFiringInterval();
         } //if

         if (x > 960) {
             x = 960;
             game.boundSide(this);
         } // if

         if (y < 5) {
             y = 5;
             game.boundTop(this);
         } // if

         if (x < 10) {
             x = 10;
             game.boundSide(this);
         } // if

         if (game.getDifficulty() != 0) {
             if (y > 405 && y < 445 && x > 350 && x < 650) {
                 if (this.getVerticalMovement() < 0) {
                     y = 446;
                     game.boundTop(this);
                 } else if (this.getVerticalMovement() > 0) {
                     y = 406;
                     game.boundTop(this);
                 } //if
             } //if 	
         } //if

     } // move


     /* collidedWith
      * input: other - the entity with which the Food has collided
      * purpose: notification that the Food has collided
      *          with something
      */
     public void collidedWith(Entity other) {


         // if it has hit an table, make it disappear!
         if (other instanceof TableEntity) {
             // remove affect entities from the Entity list
             game.removeEntity(this);
             game.setFiringInterval();


             if (other.getTableNum() == this.getFoodNum() && other.getTableNum() == 0) {
                 other.setHp();
                 game.setPotatoNum();
             } else if (other.getTableNum() == this.getFoodNum() && other.getTableNum() == 1) {
                 other.setHp();
                 game.setSushiNum();
             } else {
                 game.setPlayerHp();
             } //else

             if (other.getHp() == 0) {
                 game.removeEntity(other);
                 game.notifyTableKilled();
             } //if	

             if (game.getPlayerHp() == 0) {
                 game.notifyDeath();
             }



         } else if (other instanceof MouseEntity) {
             game.notifyDeath();
         }

     } // collidedWith



 } //FoodEntity class