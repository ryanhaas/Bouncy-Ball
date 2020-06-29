package screens;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedString;

import bbsource.BouncyBallV5;
import resources.components.RoundRectText;

public class GameMenu {
	private BouncyBallV5 bb5;
	private Sample sample;

	private RoundRectText play, settings, about, help, editor;
	private RoundRectText[] options;

	private static final int playOption = 0;
	private static final int settingsOption = 1;
	private static final int aboutOption = 2;
	private static final int helpOption = 3;
	private static final int editorOption = 4;
	private int selectedOption = playOption;

	public GameMenu(BouncyBallV5 bb5, Color ballColor) {
		this.bb5 = bb5;
		sample = new Sample(125, 135, ballColor);
		// preferredSize = bb5.getPreferredSize();

		int x = 275;
		int y = 15;
		int width = 250;
		int height = 45;
		int arcSize = 15;
		Font buttonFont = bb5.blackTuesday.deriveFont(30f);
		// buttonFont = new Font("Segoe UI", Font.BOLD, 30);
		int gap = 5;

		play = new RoundRectText(x, y, width, height, "Play", arcSize, RoundRectText.LEFT);
		play.setBackground(Color.BLUE);
		play.setForeground(Color.WHITE);
		play.setFont(buttonFont);
		y += play.getHeight() + gap;

		settings = new RoundRectText(x, y, width, height, "Settings", arcSize, RoundRectText.LEFT);
		settings.setBackground(Color.BLUE);
		settings.setForeground(Color.WHITE);
		settings.setFont(buttonFont);
		y += settings.getHeight() + gap;

		about = new RoundRectText(x, y, width, height, "About", arcSize, RoundRectText.LEFT);
		about.setBackground(Color.BLUE);
		about.setForeground(Color.WHITE);
		about.setFont(buttonFont);
		y += about.getHeight() + gap;

		help = new RoundRectText(x, y, width, height, "Help", arcSize, RoundRectText.LEFT);
		help.setBackground(Color.BLUE);
		help.setForeground(Color.WHITE);
		help.setFont(buttonFont);
		y += help.getHeight() + gap;

		editor = new RoundRectText(x, y, width, height, "Level Editor", arcSize, RoundRectText.LEFT);
		editor.setBackground(Color.BLUE);
		editor.setForeground(Color.WHITE);
		editor.setFont(buttonFont);
		y += editor.getHeight() + gap;

		RoundRectText[] temp = { play, settings, about, help, editor };
		options = temp;

		checkSelected();
	}

	public void drawGameMenu(Graphics2D g2d) {
		drawTitle(g2d);
		sample.drawSample(g2d);

		for (RoundRectText rrt : options)
			rrt.drawRoundRectText(g2d);
		
		drawSignature(g2d);
	}

	private void drawTitle(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setFont(bb5.comfortaa.deriveFont(Font.BOLD, 50f));
		FontMetrics fm = g2d.getFontMetrics();
		int x = 25;
		int y = bb5.getHeight() / 2 - fm.getHeight() / 2;

		String topString = "Bouncy";
		AttributedString as = new AttributedString(topString);
		as.addAttribute(TextAttribute.FONT, g2d.getFont());
		as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, 0, topString.length());
		g2d.drawString(as.getIterator(), x, y);

		y += fm.getHeight() + 10;

		String bottomString = "Ball";
		as = new AttributedString(bottomString);
		as.addAttribute(TextAttribute.FONT, g2d.getFont());
		as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, 0, bottomString.length());
		g2d.drawString(as.getIterator(), x, y);
	}

	public void mouseButtons(int mouseX, int mouseY) {
		Rectangle mouseRect = new Rectangle(mouseX, mouseY, 1, 1);
		boolean collided = false;

		for (int i = 0; i < options.length; i++) {
			if (mouseCollision(mouseRect, options[i])) {
				for (int y = 0; y < options.length; y++)
					options[y].setUnderline(false);
				options[i].setUnderline(true);
				selectedOption = i;
				collided = true;
				break;
			}
		}

		if (collided)
			bb5.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			bb5.setCursor(Cursor.getDefaultCursor());
	}

	public void clickedButtons(int mouseX, int mouseY) {
		Rectangle mouseRect = new Rectangle(mouseX, mouseY, 1, 1);
		if (mouseCollision(mouseRect, about)) {
			bb5.setScreen(BouncyBallV5.ABOUT);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (mouseCollision(mouseRect, help)) {
			bb5.setScreen(BouncyBallV5.HELP);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (mouseCollision(mouseRect, editor)) {
			bb5.setScreen(BouncyBallV5.EDITOR_MENU);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (mouseCollision(mouseRect, settings)) {
			bb5.setScreen(BouncyBallV5.SETTINGS);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (mouseCollision(mouseRect, play)) {
			bb5.setScreen(BouncyBallV5.PLAY_MENU);
			bb5.setCursor(Cursor.getDefaultCursor());
		}
	}

	private boolean mouseCollision(Shape a, Shape b) {
		Area aA = new Area(a);
		Area aB = new Area(b);
		aA.intersect(aB);
		return !aA.isEmpty();
	}

	public void moveSample() {
		sample.sb.moveBall();
	}

	private void drawSignature(Graphics2D g2d) {
		// g2d.setFont(bb5.samsung1.deriveFont(Font.BOLD, 12f));
		g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		g2d.setColor(Color.BLACK);
		FontMetrics fm = g2d.getFontMetrics();
		g2d.drawString("by Ryan Haas", 5, bb5.getHeight() - fm.getHeight() / 2 + 2);
	}

	public void setBallColor(Color c) {
		sample.sb.setColor(c);
	}

	public void setInnerColor(Color c) {
		sample.sb.setInnerColor(c);
	}

	private void checkSelected() {
		for (RoundRectText rrt : options)
			rrt.setUnderline(false);

		if (selectedOption == playOption)
			play.setUnderline(true);
		else if (selectedOption == settingsOption)
			settings.setUnderline(true);
		else if (selectedOption == aboutOption)
			about.setUnderline(true);
		else if (selectedOption == helpOption)
			help.setUnderline(true);
		else if (selectedOption == editorOption)
			editor.setUnderline(true);
	}

	public void enterFunc() {
		if (selectedOption == aboutOption) {
			bb5.setScreenManually(BouncyBallV5.ABOUT);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (selectedOption == helpOption) {
			bb5.setScreenManually(BouncyBallV5.HELP);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (selectedOption == editorOption) {
			bb5.setScreenManually(BouncyBallV5.EDITOR_MENU);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (selectedOption == settingsOption) {
			bb5.setScreenManually(BouncyBallV5.SETTINGS);
			bb5.setCursor(Cursor.getDefaultCursor());

		} else if (selectedOption == playOption) {
			bb5.setScreenManually(BouncyBallV5.PLAY_MENU);
			bb5.setCursor(Cursor.getDefaultCursor());

		}
	}

	public void moveSelectedDown() {
		selectedOption++;
		if (selectedOption > editorOption)
			selectedOption = playOption;
		checkSelected();
	}

	public void moveSelectedUp() {
		selectedOption--;
		if (selectedOption < playOption)
			selectedOption = editorOption;
		checkSelected();
	}

	private class Sample {
		private int width = 100;
		private int height = 50;
		private int sampleX, sampleY;

		private SampleBall sb;

		public Sample(int locX, int locY, Color ballColor) {
			sampleX = locX;
			sampleY = locY;
			sb = new SampleBall((sampleX + (sampleX + width)) / 2, ballColor);

		}

		public void drawSample(Graphics2D g2d) {
			g2d.setColor(new Color(230, 230, 230));
			// g2d.fillRect(sampleX, sampleY, width, height);

			sb.drawBall(g2d);
		}

		private class SampleBall {
			private double x, y;
			private int d;
			private double defaultUpSpeed = 4.85 / 2;

			private Color[] colorArr;

			private final int up = 1;
			private final int down = 2;
			private int direction = down;

			private double initSpeed = .1 / 2;
			private double downSpeed = initSpeed;
			private double upSpeed;

			private double isi = .1;
			private double speedIncrement = isi;

			private int floor;

			public SampleBall(double x, Color c) {
				d = 12;
				Color[] temp = { Color.WHITE, c };
				colorArr = temp;

				this.x = x;
				this.y = sampleY + d;

				floor = sampleY + height;
			}

			public void drawBall(Graphics2D g2d) {
				g2d.setStroke(new BasicStroke(2));
				g2d.setColor(Color.BLACK);
				g2d.drawLine(sampleX + 20, floor - 3, sampleX + width - 20, floor - 3);
				g2d.setStroke(new BasicStroke(1));

				Point2D center = new Point2D.Float((int) x, (int) y);
				float radius = d / 2;
				float[] dist = { 0.0f, 1.0f };
				RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colorArr);
				g2d.setPaint(rgp);

				g2d.fillOval((int) x - d / 2, (int) y - d / 2, d, d);
				g2d.setColor(Color.BLACK);
				g2d.drawOval((int) x - d / 2, (int) y - d / 2, d, d);
			}

			public void moveBall() {
				checkBounce();
				if (direction == down) {
					y += downSpeed;
					downSpeed += speedIncrement;
				} else if (direction == up) {
					y -= upSpeed;
					upSpeed -= speedIncrement;
				}
			}

			private void checkBounce() {
				if (this.y + d / 2 + downSpeed + 2 >= floor) {
					y = floor - d / 2;
					direction = up;

					upSpeed = defaultUpSpeed;
					speedIncrement = isi;
					downSpeed = initSpeed;
				} else if (upSpeed <= 0) {
					direction = down;

					upSpeed = initSpeed;
					downSpeed = initSpeed;
					speedIncrement = isi;
				}
			}

			public void setColor(Color c) {
				colorArr = new Color[] { Color.WHITE, c };
			}

			public void setInnerColor(Color c) {
				colorArr = new Color[] { c, colorArr[1] };
			}
		}
	}
}
