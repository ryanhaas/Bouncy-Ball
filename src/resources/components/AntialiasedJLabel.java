package resources.components;

import java.awt.*;
import javax.swing.*;

public class AntialiasedJLabel extends JLabel {
	private int x, y;
	private boolean isAntialiased;

	public AntialiasedJLabel(String text, boolean antialiased) {
		super(text);
		x = 0;
		x = 0;
		isAntialiased = antialiased;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (isAntialiased) {
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}

		if (isOpaque()) {
			g2d.setColor(getBackground());
			g2d.fillRect(x, y, getWidth(), getHeight());
		}

		g2d.setFont(getFont());
		FontMetrics fm = g2d.getFontMetrics();

		g2d.setColor(getForeground());
		g2d.drawString(getText(), x, y + fm.getAscent());
	}
}
