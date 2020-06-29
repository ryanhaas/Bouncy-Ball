package screens;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import bbsource.BouncyBallV5;
import resources.components.LevelButton;
import resources.components.RoundRectText;

public class PlayMenu extends JPanel {
	private BouncyBallV5 bb5;

	private static final int TIER_ONE = 1;
	private static final int TIER_TWO = 2;
	private int tier;
	
	public static final int NUM_OF_LEVELS = 21;

	private LevelButton[] tierOneButtons = new LevelButton[NUM_OF_LEVELS];
	private LevelButton[] tierTwoButtons = new LevelButton[NUM_OF_LEVELS];

	private CardLayout tierPaneController = new CardLayout();
	private JPanel tierPaneContainer = new JPanel(tierPaneController);

	private Color[] tierColors = { new Color(0, 100, 255), new Color(181, 0, 0) };
	private Color[] tierHighlightColors = { tierColors[0].brighter(), tierColors[1].brighter() };

	private int selectedIndex;

	private Rectangle2D menu;
	private RoundRectangle2D menuButton;

	private boolean menuExtended = false;
	private boolean moveBack = false;
	private boolean moveForward = false;

	private RoundRectText tierOneMenu;
	private RoundRectText tierTwoMenu;

	public PlayMenu(BouncyBallV5 bb5) {
		tier = TIER_ONE;
		this.bb5 = bb5;
		setOpaque(true);
		setBackground(BouncyBallV5.BG_COLOR);
		setLayout(new BorderLayout());
		addMouseListener(bb5);
		addMouseMotionListener(bb5);
		tierPaneContainer.setOpaque(false);
		tierPaneContainer.add(tierOnePane(), "1");
		tierPaneContainer.add(tierTwoPane(), "2");
		add(tierPaneContainer);

		tierPaneController.show(tierPaneContainer, Integer.toString(tier));

		selectedIndex = 0;
		if (tier == TIER_ONE)
			tierOneButtons[selectedIndex].setSelected(true);
		else if (tier == TIER_TWO)
			tierTwoButtons[selectedIndex].setSelected(true);

		int width = 150;
		menu = new Rectangle2D.Double(-width, 20, width, 230);
		menuButton = new RoundRectangle2D.Double(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);

		redefineMenus();

		setBorder(new EmptyBorder(10, (int) menuButton.getMaxX() + 5, 10, -(int) menuButton.getX() + 5));
	}

	private JPanel tierOnePane() {
		JPanel tierOne = new JPanel(new GridBagLayout());
		tierOne.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		;
		int index = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 7; col++) {
				gbc.gridx = col;
				gbc.gridy = row;
				gbc.weightx = .1;
				gbc.weighty = .1;
				int ipadd = 20;
				gbc.ipadx = ipadd;
				gbc.ipady = ipadd;
				LevelButton lb = new LevelButton(index + 1, false, true);
				lb.setAntialiased(bb5.isAntialiased());
				lb.setArcSize(25);
				lb.setBackground(tierColors[0]);
				lb.setForeground(Color.WHITE);
				if (index % 2 == 0)
					lb.setEnabled(false);
				lb.setFont(bb5.samsung1);
				int d = 45;
				lb.setPreferredSize(new Dimension(d, d));
				lb.addMouseListener(bb5);
				tierOne.add(lb, gbc);
				tierOneButtons[index] = lb;
				index++;
			}
		}

		for (int x = 0; x < bb5.getUnlockedLevels()[0]; x++) {
			tierOneButtons[x].setEnabled(true);
		}

		return tierOne;
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		if (bb5.isAntialiased()) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		if (menuExtended) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}

		drawSubMenu(g2d);
	}

	private void drawSubMenu(Graphics2D g2d) {
		g2d.setColor(new Color(0, 97, 155));
		g2d.fill(menu);
		g2d.setColor(new Color(0, 134, 213));
		g2d.fill(new Rectangle2D.Double(menuButton.getX(), menuButton.getY(), menuButton.getArcWidth(),
				menuButton.getHeight()));
		g2d.fill(menuButton);
		tierOneMenu.drawRoundRectText(g2d);
		tierTwoMenu.drawRoundRectText(g2d);

		AffineTransform old = g2d.getTransform();
		g2d.rotate(Math.toRadians(90));
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Segoe UI", Font.BOLD, 26));
		FontMetrics fm = g2d.getFontMetrics();
		int x = -(int) menuButton.getX() - 10;
		int y = (int) menuButton.getY() + 10 + fm.stringWidth("Tier Menu") / 3;
		g2d.drawString("Tier Menu", y, x);
		g2d.setTransform(old);
	}

	private JPanel tierTwoPane() {
		JPanel tierTwo = new JPanel(new GridBagLayout());
		tierTwo.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		;
		int index = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 7; col++) {
				gbc.gridx = col;
				gbc.gridy = row;
				gbc.weightx = .1;
				gbc.weighty = .1;
				int ipadd = 20;
				gbc.ipadx = ipadd;
				gbc.ipady = ipadd;
				LevelButton lb = new LevelButton(index + 1, false, true);
				lb.setAntialiased(bb5.isAntialiased());
				lb.setArcSize(25);
				lb.setBackground(tierColors[1]);
				lb.setForeground(Color.WHITE);
				lb.setFocusable(false);
				lb.setFont(bb5.samsung1);
				int d = 45;
				lb.setPreferredSize(new Dimension(d, d));
				lb.addMouseListener(bb5);
				tierTwo.add(lb, gbc);
				tierTwoButtons[index] = lb;
				index++;
			}
		}

		for (int x = 0; x < bb5.getUnlockedLevels()[1]; x++) {
			tierTwoButtons[x].setEnabled(true);
		}

		return tierTwo;
	}

	public void mouseEnteredButtons(MouseEvent e) {
		Object source = e.getSource();
		if (!menuExtended && source != this) {
			for (int x = 0; x < tierOneButtons.length; x++) {
				tierOneButtons[x].setSelected(false);
				tierTwoButtons[x].setSelected(false);
			}

			if (tier == TIER_ONE) {
				for (int x = 0; x < tierOneButtons.length; x++) {
					if (source == tierOneButtons[x]) {
						tierOneButtons[x].setSelected(true);
						selectedIndex = x;
						break;
					}
				}
			} else if (tier == TIER_TWO) {
				for (int x = 0; x < tierTwoButtons.length; x++) {
					if (source == tierTwoButtons[x]) {
						tierTwoButtons[x].setSelected(true);
						selectedIndex = x;
						break;
					}
				}
			}
		}
	}

	public void mouseExitedButtons(MouseEvent e) {

	}

	private boolean mouseShapeCol(int mouseX, int mouseY, Shape obj) {
		Rectangle mouseRect = new Rectangle(mouseX, mouseY, 1, 1);
		Area mA = new Area(mouseRect);
		Area oA = new Area(obj);
		mA.intersect(oA);
		return !mA.isEmpty();
	}

	public void moveMenu() {
		int speed = 15;
		if (moveForward) {
			if (menu.getX() < 0) {
				menu.setRect(menu.getX() + speed, menu.getY(), menu.getWidth(), menu.getHeight());
				menuButton.setRoundRect(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);
				redefineMenus();
			} else {
				menu.setRect(0, menu.getY(), menu.getWidth(), menu.getHeight());
				menuButton.setRoundRect(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);
				redefineMenus();
				moveForward = false;
			}
		}
		if (moveBack) {
			if (menu.getX() > -menu.getWidth()) {
				menu.setRect(menu.getX() - speed, menu.getY(), menu.getWidth(), menu.getHeight());
				menuButton.setRoundRect(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);
				redefineMenus();
			} else {
				menu.setRect(-menu.getWidth(), menu.getY(), menu.getWidth(), menu.getHeight());
				menuButton.setRoundRect(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);
				redefineMenus();
				moveBack = false;
			}
		}

		setBorder(new EmptyBorder(10, (int) menuButton.getMaxX() + 5, 10, -(int) menuButton.getX() + 5));

		repaint();
	}

	public void mousePressedButtons(MouseEvent e) {
		Object source = e.getSource();
		if (!menuExtended)
			if (tier == TIER_ONE)
				for (int x = 0; x < tierOneButtons.length; x++)
					if (source == tierOneButtons[x] && tierOneButtons[x].isEnabled())
						buttonFunc(0);
					else
						;
			else if (tier == TIER_TWO)
				for (int x = 0; x < tierTwoButtons.length; x++)
					if (source == tierTwoButtons[x] && tierTwoButtons[x].isEnabled())
						buttonFunc(1);
					else
						;
			else
				;
		else {
			boolean changed = false;
			if (mouseShapeCol(e.getX(), e.getY(), tierOneMenu)) {
				tier = TIER_ONE;
				changed = true;
			} else if (mouseShapeCol(e.getX(), e.getY(), tierTwoMenu)) {
				tier = TIER_TWO;
				changed = true;
			}

			if (changed) {
				tierPaneController.show(tierPaneContainer, Integer.toString(tier));
				menuExtended = false;
				moveForward = false;
				moveBack = true;
			}
		}

		if (source == this) {
			if (mouseShapeCol(e.getX(), e.getY(), menuButton)) {
				if (!menuExtended) {
					menuExtended = true;
					moveForward = true;
					moveBack = false;
				} else {
					menuExtended = false;
					moveForward = false;
					moveBack = true;
				}
			}
		}
	}

	public void recheckLevels() {
		for (int x = 0; x < tierOneButtons.length; x++) {
			tierOneButtons[x].setEnabled(false);
			tierTwoButtons[x].setEnabled(false);
		}

		for (int x = 0; x < bb5.getUnlockedLevels()[0]; x++)
			tierOneButtons[x].setEnabled(true);

		for (int x = 0; x < bb5.getUnlockedLevels()[1]; x++)
			tierTwoButtons[x].setEnabled(true);
		repaint();
	}

	public void keyMethod(KeyEvent e) {
		int k = e.getKeyCode();
		if (!menuExtended) {
			if (k == KeyEvent.VK_DOWN)
				if (selectedIndex < 14)
					selectedIndex += 7;
				else
					;
			else if (k == KeyEvent.VK_UP)
				if (selectedIndex >= 7)
					selectedIndex -= 7;
				else
					;
			else if (k == KeyEvent.VK_LEFT)
				if (selectedIndex > 0)
					selectedIndex -= 1;
				else
					;
			else if (k == KeyEvent.VK_RIGHT)
				if (selectedIndex < 20)
					selectedIndex += 1;
				else
					;
			else if (k == KeyEvent.VK_ENTER)
				if (tier == TIER_ONE)
					if (tierOneButtons[selectedIndex].isEnabled())
						buttonFunc(0);
					else
						;
				else if (tier == TIER_TWO)
					if (tierTwoButtons[selectedIndex].isEnabled())
						buttonFunc(1);
					else
						;

			for (int x = 0; x < tierOneButtons.length; x++) {
				tierOneButtons[x].setSelected(false);
				tierTwoButtons[x].setSelected(false);
			}

			if (tier == TIER_ONE)
				tierOneButtons[selectedIndex].setSelected(true);
			else if (tier == TIER_TWO)
				tierTwoButtons[selectedIndex].setSelected(true);
		}
	}

	public void mouseMovedButtons(MouseEvent e) {
		if (menuExtended) {
			if (mouseShapeCol(e.getX(), e.getY(), tierOneMenu))
				tierOneMenu.setBackground(tierHighlightColors[0]);
			else if (mouseShapeCol(e.getX(), e.getY(), tierTwoMenu))
				tierTwoMenu.setBackground(tierHighlightColors[1]);
			else {
				tierOneMenu.setBackground(tierColors[0]);
				tierTwoMenu.setBackground(tierColors[1]);
			}
		}
		repaint();
	}

	private void buttonFunc(int tier) {
		if (bb5.levelLoader.loadLevel(bb5.levels[tier][selectedIndex])) {
			bb5.levelLoader.setLevel(tier, selectedIndex + 1);
			bb5.setScreen(BouncyBallV5.LEVEL_LOADER);
		} else
			JOptionPane.showMessageDialog(bb5.getFrame(),
					"File For Level " + (selectedIndex + 1) + " Missing or Not Yet Released", "Error Loading Level",
					JOptionPane.ERROR_MESSAGE);
	}

	public boolean menuExtended() {
		return menuExtended;
	}

	public void resetMenu() {
		menuExtended = false;
		moveBack = false;
		moveForward = false;
		int width = 150;
		menu = new Rectangle2D.Double(-width, 20, width, 230);
		menuButton = new RoundRectangle2D.Double(menu.getMaxX(), menu.getY(), 40, menu.getHeight(), 10, 10);
	}

	private void redefineMenus() {
		int width = (int) menu.getWidth() - 10;
		tierOneMenu = new RoundRectText((int) menu.getX() + 5, (int) menu.getY() + 5, width, 50, "Tier One", 10,
				RoundRectText.CENTER);
		tierOneMenu.setBackground(tierColors[0]);
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			tierOneMenu.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		else
			tierOneMenu.setFont(bb5.samsung1.deriveFont(Font.PLAIN, 30f));
		tierOneMenu.setForeground(Color.WHITE);

		tierTwoMenu = new RoundRectText((int) menu.getX() + 5, (int) tierOneMenu.getMaxY() + 5, width, 50, "Tier Two",
				10, RoundRectText.CENTER);
		tierTwoMenu.setBackground(tierColors[1]);
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			tierTwoMenu.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		else
			tierTwoMenu.setFont(bb5.samsung1.deriveFont(Font.PLAIN, 30f));
		tierTwoMenu.setForeground(Color.WHITE);
	}
}
