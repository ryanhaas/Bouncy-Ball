package resources.objects;

import java.awt.Graphics2D;
import java.awt.Shape;

public interface GameObject {
	int getX();
	int getY();
	void setLocation(int x, int y);
	Shape getShape();
	GameObject clone();
	void drawObject(Graphics2D g2d);
	boolean equals(Object o);
	String toString();
}
