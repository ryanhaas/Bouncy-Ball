package resources.objects;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;

import bbsource.Console;

public class BouncyBall implements Serializable, GameObject {
	private static final long serialVersionUID = 1L;

	public static final double STANDARD_RLSPEED = 1.75;
	public static final double STANDARD_DEFALT_UPSPEED = 3.25;

	// Location and Ball Color Variables
	private double x, y, initX, initY;
	private int diameter;
	private Color[] color;

	// Speed Variables
	private double defaultUS = STANDARD_DEFALT_UPSPEED;
	private double downSpeed, upSpeed;
	private double inc = .125;

	private double maxRLSpeed = STANDARD_RLSPEED;
	private double rightLeftVelocity = 0;
	private double rlRate = .09;
	private double decRate = .02;

	// Direction Variables
	public static final int UP = 0;
	public static final int DOWN = 1;
	private int udDirect = DOWN;

	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean stopping = false;

	private boolean pushLeft = false;
	private boolean pushRight = false;
	private int pixelCounter = 0;

	public BouncyBall(double x, double y, int d, Color c) {
		this.x = x;
		this.y = y;
		initX = x;
		initY = y;
		this.diameter = d;
		this.color = new Color[] { Color.WHITE, c };
		downSpeed = 0;
		upSpeed = defaultUS;
	}

	public BouncyBall(double x, double y, int d, Color c, double rlSpeed, double defaultUpSpeed) {
		this.x = x;
		this.y = y;
		initX = x;
		initY = y;
		this.diameter = d;
		this.color = new Color[] { Color.WHITE, c };
		maxRLSpeed = rlSpeed;
		defaultUS = defaultUpSpeed;
		upSpeed = defaultUS;
	}

	public void drawObject(Graphics2D g2d) {
		Point2D center = new Point2D.Float((int) x, (int) y);
		float radius;
		if (diameter > 1)
			radius = diameter / 2;
		else
			radius = 1;
		float[] dist = { 0.0f, 1.0f };
		RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, color);
		g2d.setPaint(rgp);

		g2d.fillOval((int) x - diameter / 2, (int) y - diameter / 2, diameter, diameter);
		g2d.setColor(Color.BLACK);
		g2d.drawOval((int) x - diameter / 2, (int) y - diameter / 2, diameter, diameter);
	}

	public void moveBall() {
		checkChange();

		// Up and Down Movement
		if (udDirect == DOWN) {
			downSpeed += inc;
			y += downSpeed;
		}
		else if (udDirect == UP) {
			upSpeed -= inc;
			y -= upSpeed;
		}

		// Right and Left Movement
		if (moveRight)
			if (rightLeftVelocity < maxRLSpeed)
				rightLeftVelocity += rlRate;
			else
				rightLeftVelocity = maxRLSpeed;
		else if (moveLeft)
			if (rightLeftVelocity > -maxRLSpeed)
				rightLeftVelocity -= rlRate;
			else
				rightLeftVelocity = -maxRLSpeed;
		else if (!moveRight && !moveLeft && stopping) {
			if (rightLeftVelocity > decRate) {
				rightLeftVelocity -= decRate;
				decRate += .002;
			}
			else if (rightLeftVelocity < -decRate) {
				rightLeftVelocity += decRate;
				decRate += .002;
			}
			else {
				rightLeftVelocity = 0;
				stopping = false;
				decRate = .02;
			}
		}

		x += rightLeftVelocity;

		int speed = (int) (maxRLSpeed + 1.5);
		if (pushLeft) {
			if (pixelCounter < diameter) {
				x -= speed;
				pixelCounter += speed;
			}
			else {
				pushLeft = false;
				pixelCounter = 0;
				rightLeftVelocity = 0;
			}
		}
		else if (pushRight) {
			if (pixelCounter < diameter) {
				x += speed;
				pixelCounter += speed;
			}
			else {
				pushRight = false;
				pixelCounter = 0;
				rightLeftVelocity = 0;
			}
		}
	}

	private void checkChange() {
		if (udDirect == UP) {
			if (upSpeed <= 0) {
				upSpeed = defaultUS;
				udDirect = DOWN;
			}
		}
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDirectUp() {
		if (udDirect != UP) {
			udDirect = UP;
			upSpeed = defaultUS;
			downSpeed = 0;
		}
	}

	public void setDirectDown() {
		if (udDirect != DOWN) {
			udDirect = DOWN;
			upSpeed = defaultUS;
			downSpeed = 0;
		}
	}

	public void setUpSpeed(double UpSpeed) {
		this.upSpeed = UpSpeed;
	}

	public void resetUpSpeed() {
		upSpeed = defaultUS;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Shape getShape() {
		return new Ellipse2D.Double(x - diameter / 2, y - diameter / 2 + downSpeed, diameter, diameter);
	}

	public double getDefaultUpSpeed() {
		return defaultUS;
	}

	public double getRLSpeed() {
		return maxRLSpeed;
	}

	public String toString() {
		return "BouncyBall[x=" + (int) x + ", y=" + (int) y + ", d=" + diameter + ", maxRLSpeed=" + maxRLSpeed + ","
				+ " upSpeed=" + defaultUS + ",direction=" + udDirect + "]";
	}

	public int getDirection() {
		return udDirect;
	}

	public double getTop() {
		return x - diameter / 2;
	}

	public double getDownSpeed() {
		return downSpeed;
	}

	public double getUpSpeed() {
		return upSpeed;
	}

	public BouncyBall clone() {
		return new BouncyBall(this.x, this.y, this.diameter, this.color[1], this.maxRLSpeed, this.defaultUS);
	}

	public void setColor(Color c) {
		this.color = new Color[] { Color.WHITE, c };
	}

	public boolean equals(Object o) {
		if (o instanceof BouncyBall) {
			BouncyBall bb = (BouncyBall) o;
			return x == bb.getX() && y == bb.getY() && diameter == bb.getDiameter() && maxRLSpeed == bb.getRLSpeed()
					&& defaultUS == bb.getDefaultUpSpeed();
		}
		else
			return false;
	}

	public Color getColor() {
		return color[1];
	}

	public void setInnerColor(Color c) {
		this.color = new Color[] { c, getColor() };
	}

	public void reset() {
		x = initX;
		y = initY;
		udDirect = DOWN;
		downSpeed = 0;
		rightLeftVelocity = 0;
		pushRight = false;
		pushLeft = false;
		moveRight = false;
		moveLeft = false;
		stopping = false;
	}

	public void setRight(boolean b) {
		if (!moveRight) rightLeftVelocity = 0;
		moveRight = b;
		checkStopping();
	}

	public void setLeft(boolean b) {
		if (!moveLeft) rightLeftVelocity = 0;
		moveLeft = b;
		checkStopping();
	}

	private void checkStopping() {
		if (!moveRight && !moveLeft) stopping = true;
	}

	public void resetToStandard() {
		maxRLSpeed = STANDARD_RLSPEED;
		defaultUS = STANDARD_DEFALT_UPSPEED;
		upSpeed = defaultUS;
	}

	public void pushRight() {
		pushRight = true;
	}

	public void pushLeft() {
		pushLeft = true;
	}

	public double getMaxX() {
		return x + diameter / 2;
	}

	public double getMaxY() {
		return y + diameter / 2;
	}

	public double getMixX() {
		return x - diameter / 2;
	}

	public double getMinY() {
		return y - diameter / 2;
	}
	
	public String getProperty(String property) {
		if(property.equals("maxRLSpeed")) return Double.toString(maxRLSpeed);
		else if(property.equals("x")) return Double.toString(x);
		else if(property.equals("y")) return Double.toString(y);
		else if(property.equals("diameter")) return Integer.toString(diameter);
		else if(property.equals("color")) return getColor().toString();
		else return null;
	}
	
	public boolean setProperty(String property, Object value) {
		try {
			if(property.equals("maxRLSpeed")) maxRLSpeed = (Double)value;
			else if(property.equals("x")) x = (Double)value;
			else if(property.equals("y")) y = (Double)value;
			else if(property.equals("diameter")) diameter = (Integer)value;
			else if(property.equals("color") && Console.parseColor((String)value) != null) setColor(Console.parseColor((String)value));
			return true;
		} catch(Exception e) {
			System.err.println("whops");
			return false;
		}
	}
}
