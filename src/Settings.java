import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;

public class Settings extends JPanel implements ChangeListener {
	private BallV2 ball;

	public Settings(int x, int y, BallV2 b) {
		ball = b;
		
		setPreferredSize(new Dimension(350, 200));
		int gray = 120;
		setBackground(new Color(gray, gray, gray, 180));
		setBounds(x/2 - getPreferredSize().width/2, y/2 - getPreferredSize().height/2,
				getPreferredSize().width, getPreferredSize().height);
		
		int yLoc = 10;
		
		JLabel fpsLabel = new JLabel("FPS: ");
		fpsLabel.setBounds(10, yLoc, fpsLabel.getPreferredSize().width, fpsLabel.getPreferredSize().height);
		JSlider fpsSlider = new JSlider(JSlider.HORIZONTAL, 0, 60, 0);
		fpsSlider.setPaintTicks(true);
		fpsSlider.setMajorTickSpacing(5);
		fpsSlider.setPaintLabels(true);
		fpsSlider.setBounds(10 + fpsLabel.getPreferredSize().width + 10, yLoc,
				getPreferredSize().width - fpsLabel.getPreferredSize().width - 30,
				fpsSlider.getPreferredSize().height);
		fpsSlider.setValue(ball.getFPS());
		fpsSlider.setFocusable(false);
		fpsSlider.addChangeListener(this);
		yLoc += fpsSlider.getPreferredSize().height;
		
		add(fpsLabel);
		add(fpsSlider);
	}
	
	public void stateChanged(ChangeEvent e) {
		
	}
	
	public void addPane(JFrame f) {f.add(this);}
	public void addPane(JPanel p) {p.add(this);}
	public void removePane(JFrame f) {f.remove(this);}
	public void removePane(JPanel p) {p.remove(this);}
}
