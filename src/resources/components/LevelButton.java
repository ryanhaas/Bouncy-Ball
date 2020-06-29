package resources.components;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class LevelButton extends Component {
	private int level;
	private boolean enabled;
	private boolean antialiased;
	public static float fontSize;

	private boolean isRounded;
	private int arcSize = 10;

	private boolean isSelected;

	private Color highlightColor;

	private boolean tempDisable;

	public LevelButton(int level) {
		this.level = level;
		setPreferredSize(new Dimension(100, 100));
		enabled = true;
		antialiased = true;
		isRounded = false;
		isSelected = false;
		tempDisable = false;
		arcSize = 10;
	}

	public LevelButton(int level, boolean enabled) {
		this.level = level;
		this.enabled = enabled;
		setPreferredSize(new Dimension(100, 100));
		antialiased = true;
		isRounded = false;
		isSelected = false;
		tempDisable = false;
		arcSize = 10;
	}

	public LevelButton(int level, boolean enabled, boolean isRounded) {
		this.level = level;
		this.enabled = enabled;
		this.isRounded = isRounded;
		setPreferredSize(new Dimension(100, 100));
		antialiased = true;
		isSelected = false;
		tempDisable = false;
		arcSize = 10;
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		if (antialiased) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		if (!isSelected)
			g2d.setColor(getBackground());
		else
			g2d.setColor(highlightColor);
		if (!isRounded)
			g2d.fillRect(0, 0, getWidth(), getHeight());
		else
			g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize));
		if (!enabled && !tempDisable)
			g2d.setColor(
					new Color(getForeground().getRed(), getForeground().getGreen(), getForeground().getBlue(), 100));
		else
			g2d.setColor(getForeground());
		g2d.setFont(getFont());
		FontMetrics fm = g2d.getFontMetrics();
		fontSize = g2d.getFont().getSize2D();
		String s = Integer.toString(level);
		if (s.length() < 2)
			s = "0" + level;
		while (fm.getHeight() + 10 < getHeight() && fm.stringWidth(s) + 20 < getWidth()) {
			fontSize = fontSize + 2f;
			g2d.setFont(getFont().deriveFont(fontSize));
			fm = g2d.getFontMetrics();
		}
		int x = getWidth() / 2 - fm.stringWidth(Integer.toString(level)) / 2;
		int y = getHeight() / 2 + fm.getHeight() / 3;
		g2d.drawString(Integer.toString(level), x, y);
		if (isSelected)
			g2d.drawLine(x, y + 5, x + fm.stringWidth(Integer.toString(level)), y + 5);
	}

	public void setAntialiased(boolean b) {
		antialiased = b;
	}

	public void setEnabled(boolean b) {
		enabled = b;
		super.setEnabled(b);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getLevel() {
		return level;
	}

	public boolean isRounded() {
		return isRounded;
	}

	public void setRounded(boolean b) {
		isRounded = b;
	}

	public void setArcSize(int arcSize) {
		this.arcSize = arcSize;
	}

	public void setFont(Font f) {
		super.setFont(new Font(f.getFontName(), f.getStyle(), 0));
	}

	public void setBackground(Color c) {
		super.setBackground(c);
		highlightColor = c.brighter();
	}

	public void setHighlightColor(Color c) {
		highlightColor = c;
	}

	public Color getHighlightColor() {
		return highlightColor;
	}

	public boolean contains(Point p) {
		return containsMethod((int) p.getX(), (int) p.getY());
	}

	public boolean contains(int x, int y) {
		return contains(new Point(x, y));
	}

	private boolean containsMethod(int x, int y) {
		if (isRounded) {
			RoundRectangle2D rr2d = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize);
			Rectangle other = new Rectangle(x, y, 1, 1);
			Area aR = new Area(rr2d);
			Area aO = new Area(other);
			aR.intersect(aO);
			return !aR.isEmpty();
		} else
			return super.contains(x, y);
	}

	public void setSelected(boolean b) {
		isSelected = b;
		repaint();
	}
}
