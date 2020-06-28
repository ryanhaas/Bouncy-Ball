import java.awt.*;
import java.awt.geom.*;

public class Platform {
	private int x, y, w, h;
	private Color c;
	private boolean canFall = false;

	public Platform(int x, int y, int w, int h, Color c) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.c = c;
	}

	public void drawPlatform(Graphics2D g2d) {
		g2d.setColor(c);
		g2d.fillRect(x, y, w, h);
	}
	
	private void canFall(int userY, int userD) {
		if(userY + userD <= y + 10) canFall = false;
		else canFall = true;
	}
	
	public boolean doesCollide(int userX, int userY, int userD) {
		Rectangle user = new Rectangle(userX - userD/2, userY, userD, userD);
		Rectangle platform = new Rectangle(x, y, w, h);
		/*Ellipse2D ball = new Ellipse2D.Double(userX, userY, userD, userD);
		
		Area pArea = new Area(platform);
		Area ballArea = new Area(ball);
		
		
		pArea.intersect(ballArea);*/
		
		canFall(userY, userD);
		//if(!canFall && pArea.isEmpty()) {
		if(!canFall && user.intersects(platform)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}
}
