import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.*;

import resources.StarPolygon;

//Third Version of Bouncy Ball
//Currently the most succesfull version, made the physics much smoother, and the platforms work
//far better than the 2nd version.

public class BouncyBallV3 extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	private boolean antialias = true;
	private boolean won = false;
	
	private CloseX close;
	public BallV3 ball3;

	JFrame frame = new JFrame("Bouncy Ball: Version 3");
	
	private ArrayList<Platform> platforms = new ArrayList<Platform>();
	private ArrayList<StarPolygon> stars = new ArrayList<StarPolygon>();
	private Timer check = new Timer(1000/60, this);
	
	private int mouseX, mouseY;
	private int mouseLine = 30;
	
	private JLabel name = new JLabel(" " + frame.getTitle());
	
	public BouncyBallV3() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setUndecorated(true);
		
		int border = 1;
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(border, border, border, border, Color.BLACK));
		
		setLayout(null);
		setPreferredSize(new Dimension(500, 350));
		
		int gray = 220;
		setBackground(new Color(gray, gray , gray));
		
		Container canvas = frame.getContentPane();
		add(name);
		canvas.add(this);
		
		frame.pack();

		name.setFont(name.getFont().deriveFont(22f));
		int grayShade = 180;
		name.setBackground(new Color(grayShade, grayShade, grayShade));
		name.setOpaque(true);
		name.addMouseListener(this);
		name.addMouseMotionListener(this);
		
		close = new CloseX(getWidth() + 10, -10, this);
		name.setBounds(0, 0, getWidth() - close.getRectD() - 40, mouseLine);
		ball3 = new BallV3(50, 20, Color.BLUE, this);
		
		platforms.add(new Platform(0, getHeight() - 100, getWidth(), 5));
		platforms.add(new Platform(100, 200, 150, 10));
		platforms.add(new Platform(250, 150, 150, 10));
		
		int size = 15;
		int startAngle = 55;
		int vertexes = 5;
		Platform temp = platforms.get(2);
		stars.add(new StarPolygon(temp.getX() + 100,temp.getY() - size, size, (2 * size)/5, vertexes, startAngle));
		temp = platforms.get(0);
		stars.add(new StarPolygon(temp.getX() + 400, temp.getY() - size, size, (2 * size)/5, vertexes, startAngle));
		
		frame.setVisible(true);
		check.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		if(antialias) {
			g2d.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}
		
		for(Platform p : platforms) p.drawPlatform(g2d);
		
		{
			close.drawClose(g2d);
			ball3.drawBall(g2d);
		}
		
		for(int x = 0; x < stars.size(); x++) {
			g2d.setColor(Color.YELLOW);
			g2d.fillPolygon(stars.get(x));
			g.setColor(Color.BLACK);
			g2d.drawPolygon(stars.get(x));
		}
		
		if(won) {
			String string = "YOU WON";
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
			FontMetrics fm = g2d.getFontMetrics();
			
			int stringWidth = fm.stringWidth(string);
			int stringHeight = fm.getHeight();
			g2d.drawString(string, getWidth()/2 - stringWidth/2
					, getHeight()/2 - stringHeight/4);
		}
		
		g.drawLine(0, name.getHeight(), name.getWidth(), name.getHeight());
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		boolean hasOne = false;
		int index = 0;
		if(source == check) {
			for(int x = 0; x < platforms.size(); x++) {
				if(platforms.get(x).doesCollide(ball3.getX() - ball3.getD()/2
						, ball3.getY() - ball3.getD()/2, ball3.getD())) {
					hasOne = true;
					index = x;
					break;
				}
				else hasOne = false;
			}
			
			if(hasOne && ball3.getDirection() == BallV3.down) ball3.setFloor(platforms.get(index).getY());
			else ball3.setFloor(platforms.get(0).getY());
			
			Ellipse2D userE = new Ellipse2D.Double(ball3.getX() - ball3.getD()/2
					, ball3.getY() - ball3.getD()/2, ball3.getD(), ball3.getD());
			
			for(int x = 0; x < stars.size(); x++) {
				if(userStarCollision(userE, stars.get(x))) {
					stars.remove(x);
				}
			}
			
			if(stars.isEmpty()) {
				won = true;
				//ball3.stopTimers();
			}
			
			if(!frame.hasFocus() && !close.getShowing()) {
				close.showClose();
			}
		}
		
		repaint();
	}
	
	private boolean userStarCollision(Shape user, Shape star) {
		Area userA = new Area(user);
		Area starA = new Area(star);
		
		userA.intersect(starA);
	
		if(!userA.isEmpty())
			return true;
		else
			return false;
	}
	
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseDragged(MouseEvent e) {
		Object source = e.getSource();
		
		if(source == name) {
			if(mouseY < mouseLine && !collidesWithClose(e.getX(), e.getY())) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				
				frame.setLocation(x - mouseX, y - mouseY);
			}
		}
	}
	
	private boolean collidesWithClose(int mouseX, int mouseY) {
		Rectangle mouse = new Rectangle(mouseX, mouseY, 1, 1);
		Rectangle box = new Rectangle(close.getRectX(), close.getRectY(), close.getRectD(), close.getRectD());
		
		if(mouse.intersects(box))
			return true;
		else
			return false;
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {};
	
	public static void main(String[] args) {
		new BouncyBallV3();
	}
}