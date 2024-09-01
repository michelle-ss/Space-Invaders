 /* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * TableEntity.java
 * Represents one of the Tables
 */

package spaceinvaderspackage;

/* TableEntity.java
 * March 27, 2006
 * Represents one of the Tables
 */
public class TableEntity extends Entity {

	private Game game; // the game in which the Table exists

	/* construct a new Table
	 * input: game - the game in which the Table is being created
	 *        r - the image representing the Table
	 *        x, y - initial location of Table
	 */
	public TableEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY);  // calls the constructor in Entity
		game = g;
		//dx = -moveSpeed;  // start off moving left
	} // constructor
  
	public int getTableNum() {
		return tableNum;
	}

	//compatible with Entity
	public void collidedWith(Entity other) {   
	} // collidedWith

} // TableEntity class

