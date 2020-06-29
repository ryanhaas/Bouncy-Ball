import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Platform {
	private int x, y, w, h;
	private boolean canFall = false;
	
	public Platform(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void drawPlatform(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, w, h);
	}
	
	public void canFall(int userY, int userD) {
		if(userY  <= y) canFall = false;
		else canFall = true;
	}
	
	public boolean doesCollide(int userX, int userY, int userD) {
		//NOT GOOD
		//ONLY TEMPORARY, WILL EVENTUALLY USE AREA
		//Rectangle user = new Rectangle(userX, userY, userD, userD);
		//Rectangle platform = new Rectangle(x, y, w, h);
		Rectangle platform = new Rectangle(x, y, w, h);
		Ellipse2D user = new Ellipse2D.Double(userX, userY, userD, userD);
		
		Area pA = new Area(platform);
		Area uA = new Area(user);
		uA.intersect(pA);
		
		canFall(userY, userD);
		if(!canFall && !uA.isEmpty()) return true;
		else return false;
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}
	public boolean getCanFall() {return canFall;}
}
