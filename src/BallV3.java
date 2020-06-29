import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.Timer;

public class BallV3 implements ActionListener, KeyListener {
	private BouncyBallV3 bb3;
	
	private double x, y;
	private int d;
	private Color[] colorArray;
	
	private boolean justStarting = true;
	
	public static final int up = 1;
	public static final int down = 2;
	private int direction = down;
	
	private double initSpeed = .1/2;
	private double downSpeed = initSpeed;
	private double upSpeed;
	
	private int right = 1;
	private int left = 2;
	private int stopped = 3;
	private int rightOrLeft = stopped;
	
	private boolean r = false;
	private boolean l = false;
	
	private double isi = .2;
	private double speedIncrement = isi;
	
	private boolean is2ndBounce = false;
	
	private int rlSpeed;
	
	private int floor;
	int time = 3;
	
	private Timer move = new Timer(1000/60, this);
	private Timer countdown = new Timer(1000, this);
	
	private boolean showCountdown = true;
	
	public BallV3(double x, int d, Color c, BouncyBallV3 b) {
		this.x = x;
		this.d = d;
		y = 150;
		
		Color[] temp = {Color.WHITE, c};
		colorArray = temp;
		
		bb3 = b;
		bb3.frame.addKeyListener(this);
		
		startTimers();
		countdown.start();
	}
	
	public void startTimers() {
		move.start();
		countdown.start();
	}
	
	public void stopTimers() {
		move.stop();
		countdown.stop();
	}
	
	public void drawBall(Graphics2D g2d) {
		Point2D center = new Point2D.Float((int)x, (int)y);
		float radius = d/2;
		float[] dist = {0.0f, 1.0f};
		RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colorArray);
		g2d.setPaint(rgp);
		
		g2d.fillOval((int)x - d/2, (int)y - d/2, d, d);
		
		//g2d.drawLine(0, floor, bb3.getWidth(), floor);
		
		if(showCountdown) {
			FontMetrics fm = g2d.getFontMetrics();
			int stringWidth = 0;
			int stringHeight = 0;
			if(time > 0) {
				stringWidth = fm.stringWidth(Integer.toString(time));
				stringHeight = fm.getHeight();
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
				g2d.drawString(Integer.toString(time), bb3.getWidth()/2 - stringWidth/2
						, bb3.getHeight()/2 - stringHeight/4);
			}
			else {
				stringWidth = fm.stringWidth("Go");
				stringHeight = fm.getHeight();
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
				g2d.drawString("Go", bb3.getWidth()/2 - stringWidth/2
						, bb3.getHeight()/2 - stringHeight/4);
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == move) {
			checkBounce();
			if(direction == down) {
				y += downSpeed;
				if(!justStarting) {
					downSpeed += speedIncrement;
				}
				else downSpeed += speedIncrement;
			}
			else if(direction == up) {
				y -= upSpeed;
				upSpeed -= speedIncrement;
			}
			
			if(rightOrLeft == right || r) x += rlSpeed;
			if(rightOrLeft == left || l) x -= rlSpeed;
		}
		if(source == countdown) {
			time--;
			if(time < 0) {
				countdown.stop();
				showCountdown = false;
			}
		}
		
		bb3.repaint();
	}
	
	private int tempCounter = 0;
	private double defaultUpSpeed = 4.85;
	
	public void checkBounce() {
		if(y + d/2 >= floor) {
			y = floor - d/2;
			direction = up;
			
			//upSpeed = downSpeed;
			upSpeed = defaultUpSpeed;
			speedIncrement = isi;
			downSpeed = initSpeed;
			
			if(justStarting) justStarting = false;
			if(tempCounter < 3) tempCounter++;
		}
		else if (upSpeed <= 0) {
			direction = down;
			
			upSpeed = initSpeed;
			downSpeed = initSpeed;
			speedIncrement = isi;

			if(tempCounter == 2) {				
				rlSpeed = 3;
				is2ndBounce = true;
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(is2ndBounce) {
			/*if(k == KeyEvent.VK_RIGHT) rightOrLeft = right;
			if(k == KeyEvent.VK_LEFT) rightOrLeft = left;*/
			if(k == KeyEvent.VK_RIGHT) r = true;
			if(k == KeyEvent.VK_LEFT) l = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(is2ndBounce) {
			//if(k == KeyEvent.VK_RIGHT) rightOrLeft = stopped;
			//if(k == KeyEvent.VK_LEFT) rightOrLeft = stopped;
			if(k == KeyEvent.VK_RIGHT) r = false;
			if(k == KeyEvent.VK_LEFT) l = false;
		}
	}
	
	public int getX() {return (int)x;}
	public int getY() {return (int)y;}
	public int getD() {return d;}
	
	public void setFloor(int f) {
		floor = f;
	}
	
	public void reset() {
		downSpeed = initSpeed;
		speedIncrement = isi;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void keyTyped(KeyEvent e) {}
}
