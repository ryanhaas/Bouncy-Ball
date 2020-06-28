import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer;

public class Ball implements ActionListener, KeyListener {
	private BouncyBall bb;
	private Timer mainTimer;
	private Timer speedTimer;
	
	private int locX, locY, diameter;
	private int floor = 300;
	private int ceiling = 100;
	
	int upSpeed = 12;
	int downSpeed = 2;
	
	private boolean firstInitialized = true;
	private boolean down = false;
	private boolean up = false;
	private boolean right = false;
	private boolean left = false;
	
	private Color color;
	
	public Ball(int x, int y, int d, Color c, BouncyBall b) {
		locX = x;
		locY = y;
		diameter = d;
		color = c;
		bb = b;
		
		defaultStuff();
	}
	
	public Ball(int x, int y, Color c, BouncyBall b) {
		locX = x;
		locY = y;
		diameter = 50;
		color = c;
		bb = b;
		
		defaultStuff();
	}
	
	private void defaultStuff() {
		mainTimer = new Timer(1000/60, this);
		mainTimer.start();
		
		speedTimer = new Timer(1000/60, this);
		speedTimer.start();
		
		bb.frame.addKeyListener(this);
	}
	
	public void drawBall(Graphics2D g2) {
		g2.setColor(color);
		g2.fillOval(locX - diameter/2, locY - diameter/2, diameter, diameter);
		
		g2.setColor(Color.BLACK);
		g2.drawLine(0, floor, bb.getWidth(), floor);
		//g2.drawLine(0, ceiling, bb.getWidth(), ceiling);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == mainTimer) {
			if(firstInitialized) {
				if(locY + diameter/2 < floor) {
					locY += downSpeed;
					down = true;
				}
				else {
					firstInitialized = false;
					locY = floor - diameter/2;
					up = true;
					downSpeed = 2;
				}
			}
			else {
				if(up) {
					if(locY - diameter/2 > ceiling) {
						locY -= upSpeed;
					}
					else {
						locY = ceiling + diameter/2;
						downSpeed = upSpeed;
						upSpeed = 10;
						down = true;
						up = false;
					}
				}
				else if(down) {
					if(locY + diameter/2 < floor) {
						locY += downSpeed;
					}
					else {
						locY = floor - diameter/2;
						down = false;
						up = true;
						downSpeed = 5;
					}
					
				}
				
				int rlSpeed = 2;
				if(right) {
					locX += rlSpeed;
				}
				else if(left) {
					locX -= rlSpeed;
				}
			}
		}
		else if(source == speedTimer) {
			if(up) {
				if(upSpeed > 5) {
					upSpeed--;
				}
			}
			else if(down) {
				if(downSpeed < 10) {
					downSpeed++;
				}
			}
		}
		
		bb.repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if(k == KeyEvent.VK_LEFT) {
			left = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if(k == KeyEvent.VK_LEFT) {
			left = false;
		}
	}
	
	public void keyTyped(KeyEvent e) {}
}
