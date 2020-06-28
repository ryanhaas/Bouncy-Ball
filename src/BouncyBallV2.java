import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

//Second Version of Bouncy Ball
//Added 'physics' to the ball, so it wasn't static bouncing
//Incorporated platforms, but had flaws, but made significant progress from the first version

public class BouncyBallV2 extends JPanel implements KeyListener, ActionListener {
	public static int frameHeight, frameWidth;
	
	public BallV2 ball;
	public Settings settings;
	
	private boolean antialias = true;
	private boolean showPause = false;
	
	private JFrame frame;
	
	private ArrayList<Platform> platforms = new ArrayList<Platform>();
	
	private Timer check = new Timer(1000/60, this);
	
	public BouncyBallV2() {
		frame = new JFrame("Bouncy Ball V2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.addKeyListener(this);
		setPreferredSize(new Dimension(500, 500));
		setLayout(null);
		
		Container canvas = frame.getContentPane();
		canvas.add(this);
		
		frame.pack();
		
		frameHeight = getHeight();
		frameWidth = getWidth();
		
		Color[] colors = {Color.WHITE, Color.BLUE};
		ball = new BallV2(getWidth()/2, 20, colors, this);
		settings = new Settings(getWidth(), getHeight(), ball);
		
		//Floor
		platforms.add(new Platform(0, getHeight(), getWidth(), getHeight(), Color.BLACK));
		
		platforms.add(new Platform(20, 450, 200, 10, Color.BLACK));
		platforms.add(new Platform(240, 380, 100, 10, Color.BLACK));
		
		frame.addKeyListener(ball);
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
		}

		for(Platform p : platforms) p.drawPlatform(g2d);
		ball.drawBall(g2d);
		
		if(showPause) {
			g2d.setColor(new Color(222, 222, 222, 200));
			g2d.fillRect(0, 0, getWidth(), getHeight()); 
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_ESCAPE) {
			if(showPause) {
				settings.removePane(this);
				ball.startTimer();
				showPause = false;
			}
			else {
				settings.addPane(this);
				showPause = true;
				ball.stopTimer();
			}
			
			repaint();
		}
		if(k == KeyEvent.VK_R) {
			frame.dispose();
			new BouncyBallV2();
		}
	}
	
	private boolean hasOne = false;
	private int index;
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == check) {
			for(int x = 0; x < platforms.size(); x++) {
				if(platforms.get(x).doesCollide(ball.getX(), ball.getY(), ball.getD()) && ball.isGoingDown()) {
					//ball.setFloor(p.getY());
					//ball.updateCeiling();
					hasOne = true;
					index = x;
					break;
				}
				else {
					hasOne = false;
				}
			}
			
			if(hasOne) {
				ball.setFloor(platforms.get(index).getY());
				ball.updateCeiling();
			}
			else {
				ball.setFloor(platforms.get(0).getY());
			}
			
			if(ball.getY() + ball.getD() >= getHeight()) {
				ball.setFloor(platforms.get(0).getY());
				ball.updateCeiling();
			}
		}
		
		repaint();
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
 
	public static void main(String[] args) {
		new BouncyBallV2();
	}
}
