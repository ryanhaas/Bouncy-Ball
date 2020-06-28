import java.awt.*;

import javax.swing.*;

//First Verison of Bouncy Ball
//Not at all good, but a start,
//got the basic idea of bouncing a ball

public class BouncyBall extends JPanel {
	private Ball mainBall;
	
	public JFrame frame;

	public BouncyBall() {
		frame = new JFrame("Bouncy Ball");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		setPreferredSize(new Dimension(500, 500));
		setLayout(null);
		
		Container canvas = frame.getContentPane();
		canvas.add(this);
		frame.pack();
		
		mainBall = new Ball(250, 100, 30, Color.BLUE, this);
		
		frame.setVisible(true);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		antiAlias(g2);
		
		mainBall.drawBall(g2);
	}
	
	public void antiAlias(Graphics2D g2d) {
		g2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);
	}

	public static void main(String[] args) {
		new BouncyBall();
	}
}
