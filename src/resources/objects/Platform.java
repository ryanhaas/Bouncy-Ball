package resources.objects;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Platform implements Serializable, GameObject {
	private static final long serialVersionUID = 1L;

	// Location and Dimension Variables
	private int x, y, w, h;

	// Type Variables
	private int type = -1;
	public static final int STANDARD = 0;
	public static final int BOOST = 1;
	public static final int MOVING_VERTICAL = 2;
	public static final int MOVING_HORIZONTAL = 3;

	// Boost Variable if type == BOOST
	private double boost;

	// Variables for other coord if type == MOVING_X and distance between 2
	// coords
	private int otherCoord, distance;
	private int subBox;
	private int startX;
	private int startY;

	// Platform Colors
	private final Color standardColor = new Color(60, 60, 60);
	private final Color boostColor = new Color(4, 179, 255);
	private final Color movingColor = Color.WHITE;

	private int direction;
	private final int left = 1;
	private final int up = 2;
	private final int right = 3;
	private final int down = 4;

	// Side Variables
	public static final int TOP = 10;
	public static final int BOTTOM = 11;
	public static final int LEFT = 12;
	public static final int RIGHT = 13;

	public Platform(int x, int y, int w, int h, int type) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
	}

	public Platform(int x, int y, int w, int h, int type, double boost) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
		this.boost = boost;
	}

	public Platform(int x, int y, int w, int h, int type, int otherCoord) {
		this.w = w;
		this.h = h;
		this.type = type;
		this.otherCoord = otherCoord;

		if (this.type == MOVING_HORIZONTAL) {
			subBox = this.h;
			this.startX = x;
			this.x = startX + subBox;
			this.y = y;

			if (otherCoord < x) {
				this.x = otherCoord;
				this.otherCoord = x;

				this.startX = this.x;
				this.x = this.startX + subBox;
			}

			this.direction = right;
			this.distance = this.otherCoord - this.x;
		} else if (this.type == MOVING_VERTICAL) {
			subBox = this.w;
			this.startY = y;
			this.x = x;
			this.y = startY + h;
			if (otherCoord < y) {
				this.y = otherCoord;
				this.otherCoord = y;

				this.startY = this.y;
				this.y = startY + h;
			}

			this.direction = down;
			this.distance = this.otherCoord - this.y;
		}
	}

	public void movePlatform() {
		int speed = 1;
		if (type == MOVING_HORIZONTAL) {
			if (direction == right) {
				if (x + speed + w < otherCoord)
					x += speed;
				else
					direction = left;
			} else if (direction == left) {
				if (x - speed - subBox > startX)
					x -= speed;
				else
					direction = right;
			}
		} else if (type == MOVING_VERTICAL) {
			if (direction == down) {
				if (y + speed + h < otherCoord)
					y += speed;
				else
					direction = up;
			} else if (direction == up) {
				if (y - speed - h > startY)
					y -= speed;
				else
					direction = down;
			}
		}
	}

	public void drawObject(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, w, h);

		if (type == STANDARD)
			g2d.setColor(standardColor);
		else if (type == BOOST)
			g2d.setColor(boostColor);
		else if (type == MOVING_HORIZONTAL) {
			Stroke defaultStroke = g2d.getStroke();
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
			g2d.setStroke(dashed);
			g2d.drawRect(startX, y, subBox, h - 1);
			g2d.drawRect(otherCoord, y, subBox, h - 1);

			g2d.setColor(movingColor);
			g2d.setStroke(defaultStroke);
		} else if (type == MOVING_VERTICAL) {
			Stroke defaultStroke = g2d.getStroke();
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 4 }, 0);
			g2d.setStroke(dashed);
			g2d.drawRect(x, startY, subBox, h - 1);
			g2d.drawRect(x, otherCoord, subBox, h - 1);

			g2d.setColor(movingColor);
			g2d.setStroke(defaultStroke);
		}

		g2d.fillRect(x + 2, y + 2, w - 4, h - 4);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public double getBoost() {
		return boost;
	}

	public int getType() {
		return type;
	}

	public int getSubBox() {
		return subBox;
	}

	public String getTypeString() {
		if (type == STANDARD)
			return "Standard";
		else if (type == BOOST)
			return "Boost";
		else if (type == MOVING_HORIZONTAL)
			return "Moving Horizontal";
		else if (type == MOVING_VERTICAL)
			return "Moving Vertical";
		else
			return "Unknown";
	}

	public Shape getShape() {
		return new Rectangle2D.Double(x, y, w, h);
	}

	public int getOtherCoord() {
		return otherCoord;
	}

	public void setLocation(int x, int y) {
		if (type != MOVING_HORIZONTAL && type != MOVING_VERTICAL) {
			this.x = x;
			this.y = y;
		} else if (type == MOVING_HORIZONTAL) {
			this.startX = x - subBox;
			this.x = x;
			this.otherCoord = x + distance;
			this.y = y;
		} else if (type == MOVING_VERTICAL) {
			this.startY = y - h;
			this.x = x;
			this.otherCoord = y + distance;
			this.y = y;
		}
	}

	public Platform clone() {
		if (this.type == STANDARD)
			return new Platform(this.x, this.y, this.w, this.h, STANDARD);
		else if (type == BOOST)
			return new Platform(this.x, this.y, this.w, this.h, BOOST, this.boost);
		else if (type == MOVING_HORIZONTAL)
			return new Platform(this.x, this.y, this.w, this.h, MOVING_HORIZONTAL, this.otherCoord);
		else if (type == MOVING_VERTICAL)
			return new Platform(this.x, this.y, this.w, this.h, MOVING_VERTICAL, this.otherCoord);
		else
			return null;
	}

	public String toString() {
		return "Platform[type=" + typeText() + ",x=" + x + ",y=" + y + ",width=" + w + ",height=" + h + ",boost="
				+ boost + ",other coord=" + otherCoord + "]";
	}

	public boolean equals(Object o) {
		if (o instanceof Platform) {
			boolean sameObject = true;
			Platform oP = (Platform) o;
			if (oP.getX() != getX())
				sameObject = false;
			else if (oP.getY() != getY())
				sameObject = false;
			else if (oP.getW() != getW())
				sameObject = false;
			else if (oP.getH() != getH())
				sameObject = false;
			else if (oP.getType() != getType())
				sameObject = false;
			else if (oP.getType() == getType()) {
				if (getType() == BOOST)
					if (oP.getBoost() != getBoost())
						sameObject = false;
					else
						;
				else if (getType() == MOVING_VERTICAL || getType() == MOVING_HORIZONTAL)
					if (oP.getOtherCoord() != getOtherCoord())
						sameObject = false;
			}
			return sameObject;
		} else {
			return false;
		}
	}

	private String typeText() {
		if (type == STANDARD)
			return "Platform.STANDARD";
		else if (type == BOOST)
			return "Platform.BOOST";
		else if (type == MOVING_HORIZONTAL)
			return "Platform.MOVING_HORIZONTAL";
		else if (type == MOVING_VERTICAL)
			return "Platform.MOVING_VERTICAL";
		else
			return "null";
	}

	public int getMaxY() {
		return y + h;
	}

	public int getMaxX() {
		return x + w;
	}

	public int checkSide(BouncyBall b) {
		final Rectangle topRect = new Rectangle(x, y, w, 1);
		final Rectangle bottomRect = new Rectangle(x, getMaxY() - 1, w, 1);
		final Rectangle leftRect = new Rectangle(x, y, 1, h);
		final Rectangle rightRect = new Rectangle(getMaxX() - 1, y, 1, h);
		if (ballPlatCol(topRect, b))
			return TOP;
		else if (ballPlatCol(bottomRect, b))
			return BOTTOM;
		else if (ballPlatCol(leftRect, b))
			return LEFT;
		else if (ballPlatCol(rightRect, b))
			return RIGHT;
		return -1;
	}

	private boolean ballPlatCol(Rectangle r, BouncyBall b) {
		Area pA = new Area(r);
		Area bA = new Area(b.getShape());
		pA.intersect(bA);
		return !pA.isEmpty();
	}
}
