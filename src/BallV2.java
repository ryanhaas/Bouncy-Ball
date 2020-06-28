import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.Timer;

public class BallV2 implements ActionListener, KeyListener {
	private BouncyBallV2 bb2;
	
	private int fps = 60;
	private Timer move = new Timer(1000/60, this);
	private Timer frameRate = new Timer(1000/fps, this);
	
	private double x, y;
	private int d;
	private Color c;
	private Color[] gradientColors;

	private final int up = 1;
	private final int down = 2;
	private int direction = down;
	
	private final int right = 1;
	private final int left = 2;
	private final int stopped = 0;
	private int rightOrLeft = stopped;
	
	private double initSpeed = .1/2;
	private double downSpeed = initSpeed;
	private double upSpeed;
	
	private double initSpeedIncrement = .1;
	private double speedIncrement = initSpeedIncrement;
	
	private int floor = 500;
	private int ceiling = 00;
	private int rlSpeed;
	
	private boolean isGradient = false;
	
	public BallV2(double x, int d, Color[] gc, BouncyBallV2 b) {
		//THIS ONE
		this.x = x;
		ceiling = (floor - d) - (d * 5);
		this.y = ceiling;
		this.d = d;
		this.gradientColors = gc;
		isGradient = true;
		
		rlSpeed = (floor - ceiling)/33;
		
		bb2 = b;
		startTimer();
	}

	public void drawBall(Graphics2D g2d) {
		if(isGradient) {
			Point2D center = new Point2D.Float((int)x, d/2 + (int)y);
			float radius = d/2;
			float[] dist = {0.0f, 1.0f};
			RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, gradientColors);
			
			g2d.setPaint(rgp);
		}
		else {
			g2d.setColor(c);
		}
		
		g2d.fillOval((int)x - d/2, (int)y, d, d);
		
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, floor, BouncyBallV2.frameWidth, floor);
		
		float[] dash1 = {10.0f};
		Stroke defaultStroke = g2d.getStroke();
		BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		g2d.setStroke(dashed);
		g2d.drawLine(0, ceiling, BouncyBallV2.frameWidth, ceiling);
		
		g2d.setStroke(new BasicStroke(10));
		g2d.setColor(Color.RED);
		g2d.drawLine(bb2.frameWidth - 20, ceiling, bb2.frameWidth - 20, floor);
	}
	
	public void startTimer() {
		move.start();
		frameRate.start();
	}
	public void stopTimer() {
		move.stop();
		frameRate.stop();
	}
	
	public void setFloor(int f) {floor = f;}
	
	public void setFPS(int f) {
		frameRate.setDelay(1000/f);
		fps = f;
	}
	
	public int getFPS() {return fps;}
	public int getX() {return (int)x;}
	public int getY() {return (int)y;}
	public int getD() {return d;}
	
	private void checkBounce() {
		if(y + d + 1 >= floor) {
			y = floor - d;
			direction = up;
			
			upSpeed = downSpeed + 2;
			speedIncrement = initSpeedIncrement;
			downSpeed = initSpeed;
		}
		else if(y < ceiling) {
			y = ceiling;
			direction = down;

			upSpeed = initSpeed;
			downSpeed = upSpeed;
			speedIncrement = initSpeedIncrement;
		}
	}
	
	public void updateCeiling() {
		ceiling = (floor - d) - (d * 5);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == move) {
			checkBounce();
			if(direction == down) {
				y += downSpeed;
				downSpeed += speedIncrement;
				//if(speedIncrement < 1.0) speedIncrement += increaseIncrement;
			}
			else if(direction == up) {
				y -= upSpeed;
				upSpeed -= speedIncrement;
				//if(speedIncrement < .5) speedIncrement += increaseIncrement;
			}
			
			//int rlSpeed = (floor - ceiling)/100;
			if(rightOrLeft == right) x += rlSpeed;
			else if(rightOrLeft == left) x -= rlSpeed;
		}
		if(source == frameRate) {
			bb2.repaint();
		}
	}
	
	public void drawFPS(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.setFont(g.getFont().deriveFont(20f));
		g.drawString(Integer.toString(getFPS()), 10, 20);
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		
		if(k == KeyEvent.VK_RIGHT) rightOrLeft = right;
		if(k == KeyEvent.VK_LEFT) rightOrLeft = left;;
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		
		
		if(k == KeyEvent.VK_RIGHT) rightOrLeft = stopped;
		else if (k == KeyEvent.VK_LEFT) rightOrLeft = stopped;
	}
	
	public boolean isGoingDown() {
		if(direction == down) return true;
		else return false;
	}
	
	public void keyTyped(KeyEvent e) {}
}
