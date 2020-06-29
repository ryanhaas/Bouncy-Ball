import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class CloseX implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
	private BouncyBallV3 bb3;
	private int x, y;
	
	private int rectX, rectY, rectD;
	
	private Color xCol = Color.RED;
	
	private JButton cancel = new JButton("Cancel");
	private JButton exit = new JButton("Exit");
	private JPanel cancelPane = new JPanel();
	
	private boolean toggle = false;

	public CloseX(int x, int y, BouncyBallV3 b) {
		this.x = x;
		this.y = y;
		
		bb3 = b;
		
		bb3.addMouseListener(this);
		bb3.addMouseMotionListener(this);
		bb3.frame.addKeyListener(this);
	}

	public void drawClose(Graphics2D g2d) {
		AffineTransform defaultAT = g2d.getTransform();
		
		int width = 5;
		int height = 30;
		
		rectD = height;
		rectX = x - (int)(height*1.5);
		rectY = y - height/2 + height;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(rectX, rectY, rectD, rectD);
		
		g2d.setColor(xCol);

		g2d.rotate(Math.toRadians(45), x - height, y + height);
		g2d.fillRect(x - width/2 - height, y - height/2 + height, width, height);
		
		g2d.setTransform(defaultAT);
		
		g2d.rotate(Math.toRadians(135), x - height, y + height);
		g2d.fillRect(x - width/2 - height, y - height/2 + height, width, height);
		
		g2d.setTransform(defaultAT);
	}
	
	public void mousePressed(MouseEvent e) {
		Rectangle xRec = new Rectangle(rectX, rectY, rectD, rectD);
		Rectangle mouse = new Rectangle(e.getX(), e.getY(), 1, 1);
		
		if(xRec.intersects(mouse)) {
			bb3.ball3.stopTimers();
			confirmExit();
			toggle = true;
		}
		
		bb3.repaint();
	}
	public void mouseReleased(MouseEvent e) {
		
	}
	public void mouseMoved(MouseEvent e) {
		Rectangle xRec = new Rectangle(rectX, rectY, rectD, rectD);
		Rectangle mouse = new Rectangle(e.getX(), e.getY(), 1, 1);
		
		if(xRec.intersects(mouse)) {
			xCol = Color.WHITE;
			bb3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else {
			xCol = Color.RED;
			bb3.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		bb3.repaint();
	}
	
	private void confirmExit() {
		cancelPane.setPreferredSize(new Dimension(300, 150));
		cancelPane.setBounds(bb3.getWidth()/2 - cancelPane.getPreferredSize().width/2, bb3.getHeight()/2 - cancelPane.getPreferredSize().height/2, 
				cancelPane.getPreferredSize().width, cancelPane.getPreferredSize().height);
		Color lg = Color.LIGHT_GRAY;
		cancelPane.setBackground(new Color(lg.getRed(), lg.getGreen(), lg.getBlue(), 200));
		
		JLabel warning = new JLabel("Are you sure you want to exit?");
		warning.setFont(warning.getFont().deriveFont(Font.BOLD, 18f));
		warning.setBounds(10, 10, warning.getPreferredSize().width, warning.getPreferredSize().height);
		
		cancel.setBounds(10, cancelPane.getPreferredSize().height - cancel.getPreferredSize().height - 10,
				cancel.getPreferredSize().width, cancel.getPreferredSize().height);
		cancel.addActionListener(this);
		
		exit.setBounds(cancelPane.getPreferredSize().width - exit.getPreferredSize().width - 10,
				cancelPane.getPreferredSize().height - exit.getPreferredSize().height - 10,
				exit.getPreferredSize().width, exit.getPreferredSize().height);
		exit.addActionListener(this);
		
		cancelPane.add(warning);
		cancelPane.add(cancel);
		cancelPane.add(exit);
		
		bb3.add(cancelPane);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == cancel) {
			bb3.remove(cancelPane);
			bb3.ball3.startTimers();
			bb3.frame.requestFocus();
			toggle = false;
		}
		if(source == exit) {
			System.exit(0);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == KeyEvent.VK_ESCAPE) {
			if(toggle == false) {
				bb3.ball3.stopTimers();
				confirmExit();
				
				toggle = true;
			}
			else {
				bb3.remove(cancelPane);
				bb3.ball3.startTimers();
				bb3.frame.requestFocus();
				
				toggle = false;
			}
		}
		
		bb3.repaint();
	}
	
	public void showClose() {
		bb3.ball3.stopTimers();
		confirmExit();
		
		toggle = true;
	}
	
	public void closeClose() {
		bb3.remove(cancelPane);
		bb3.ball3.startTimers();
		bb3.frame.requestFocus();
		
		toggle = false;
	}
	
	public boolean getShowing() {
		return toggle;
	}
	
	public int getRectX() {return rectX;}
	public int getRectY() {return rectY;}
	public int getRectD() {return rectD;}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
