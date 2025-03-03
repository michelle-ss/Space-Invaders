/* 2018-04-17
 * Sihan Li, Michelle Song, and Olivia Waller - ICTP 12
 * Sprite.java
 * Stores no state information, this allows the image to be stored only
 * once, but to be used in many different places.  For example, one
 * copy of alien.gif can be used over and over.
 */

package spaceinvaderspackage;
 
 import java.awt.Graphics;
 import java.awt.Image;
 
 public class Sprite {

	 public Image image;  // the image to be drawn for this sprite
   
	 // constructor
	 public Sprite (Image i) {
		 image = i;
	 } // constructor

	 // return width of image in pixels
	 public int getWidth() {
		 return image.getWidth(null);
	 } // getWidth

	 // return height of image in pixels
	 public int getHeight() {
		 return image.getHeight(null);
	 } // getHeight

	 // draw the sprite in the graphics object provided at location (x,y)
	 public void draw(Graphics g, int x, int y) {
		 g.drawImage(image, x, y, null);
	 } // draw
   
	 public void drawBackground(Graphics g, int x, int y, int w, int h) {
		 g.drawImage(image,  x,  y,  w,  h,  null);
	 } // drawBackground

 } // Sprite