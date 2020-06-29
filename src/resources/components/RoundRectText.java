package resources.components;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.text.AttributedString;

public class RoundRectText extends RoundRectangle2D.Double {
	private int alignment;
	public static final int RIGHT = 1;
	public static final int CENTER = 2;
	public static final int LEFT = 3;

	private Font font;

	private Color bg;
	private Color fg;

	private String text;

	private boolean underline = false;

	public RoundRectText(int x, int y, int width, int height, String text, int arc) {
		super((double) x, (double) y, (double) width, (double) height, (double) (arc), (double) (arc));
		font = new Font("", Font.PLAIN, 16);
		alignment = CENTER;
		this.text = text;
	}

	public RoundRectText(int x, int y, int width, int height, String text, int arc, int align) {
		super((double) x, (double) y, (double) width, (double) height, (double) (arc), (double) (arc));
		font = new Font("", Font.PLAIN, 16);
		this.text = text;
		alignment = align;
	}

	public void drawRoundRectText(Graphics2D g2d) {
		g2d.setColor(bg);
		g2d.fill(this);

		g2d.setFont(font);
		g2d.setColor(fg);
		FontMetrics fm = g2d.getFontMetrics(font);
		String textToPaint = text;
		boolean removed = false;
		while (fm.stringWidth(textToPaint) > this.getWidth()) {
			textToPaint = textToPaint.substring(0, textToPaint.length() - 2);
			removed = true;
		}

		if (removed) {
			textToPaint = textToPaint.substring(0, textToPaint.length() - 3) + "...";
		}

		if (alignment == LEFT) {
			if (underline) {
				AttributedString as = new AttributedString(textToPaint);
				as.addAttribute(TextAttribute.FONT, font);
				as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, 0,
						textToPaint.length());
				int stringX = (int) (this.getX() + this.getArcWidth() / 2);
				int stringY = (int) (getCenterY() - fm.getHeight() / 2 + fm.getAscent());
				g2d.drawString(as.getIterator(), stringX, stringY);
			} else {
				int stringX = (int) (this.getX() + this.getArcWidth() / 2);
				int stringY = (int) (getCenterY() - fm.getHeight() / 2 + fm.getAscent());
				g2d.drawString(textToPaint, stringX, stringY);
				// ( ( getHeight() - fm.getHeight() ) / 2 ) + fm.getAscent();
			}
		} else if (alignment == CENTER) {
			if (underline) {
				AttributedString as = new AttributedString(textToPaint);
				as.addAttribute(TextAttribute.FONT, font);
				as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, 0,
						textToPaint.length());
				int stringX = (int) ((getCenterX() - fm.stringWidth(textToPaint) / 2) + this.getArcWidth() / 2);
				int stringY = (int) ((getCenterY()) - fm.getHeight() / 2 + fm.getAscent());
				g2d.drawString(as.getIterator(), stringX, stringY);
			} else {
				int stringX = (int) ((getCenterX() - fm.stringWidth(textToPaint) / 2
						- fm.getLeading() * font.getSize() / 4) + this.getArcWidth() / 2);
				int stringY = (int) (getCenterY() - fm.getHeight() / 2 + fm.getAscent());
				g2d.drawString(textToPaint, stringX, stringY);
			}
		}
	}

	public void setFont(Font f) {
		font = f;
	}

	public void setBackground(Color bg) {
		this.bg = bg;
	}

	public void setForeground(Color fg) {
		this.fg = fg;
	}

	public void setText(String newText) {
		text = newText;
	}

	public void setUnderline(boolean u) {
		underline = u;
	}

	public String getText() {
		return text;
	}
}
