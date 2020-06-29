package editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.*;

import resources.objects.*;

public class LevelTester extends JPanel implements ActionListener, KeyListener {
	private JDialog frame;
	private ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
	private double starRotateAngle;
	private Timer check = new Timer(1000 / 60, this);

	private ArrayList<Platform> plats = new ArrayList<Platform>();
	private ArrayList<StarShape> stars = new ArrayList<StarShape>();
	private BouncyBall ball;

	public LevelTester(GameObject[] objs, Dimension d, Point loc) {
		basicOps_1();
		frame.setLocation(loc);
		setPreferredSize(d);

		for (GameObject obj : objs)
			allObjects.add(obj);
		for (Object obj : allObjects) {
			if (obj instanceof Platform)
				plats.add((Platform) obj);
			else if (obj instanceof StarShape)
				stars.add((StarShape) obj);
			else if (obj instanceof BouncyBall) {
				BouncyBall temp = (BouncyBall) obj;
				ball = new BouncyBall(temp.getX(), temp.getY(), temp.getDiameter(), temp.getColor(), temp.getRLSpeed(),
						temp.getDefaultUpSpeed());
			}
		}

		basicOps_2();
	}

	public LevelTester(File levelFile, Point loc) {
		basicOps_1();
		frame.setLocation(loc);

		try {
			FileInputStream fis = new FileInputStream(levelFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			CustomSaver cs = (CustomSaver) ois.readObject();
			setPreferredSize(cs.getSize());

			allObjects = cs.getAllObjects();

			for (Object obj : allObjects) {
				if (obj instanceof Platform)
					plats.add((Platform) obj);
				else if (obj instanceof StarShape)
					stars.add((StarShape) obj);
				else if (obj instanceof BouncyBall) {
					BouncyBall temp = (BouncyBall) obj;
					ball = new BouncyBall(temp.getX(), temp.getY(), temp.getDiameter(), Color.BLUE, temp.getRLSpeed(),
							temp.getDefaultUpSpeed());
				}
			}

			fis.close();
			ois.close();

			basicOps_2();
		} catch (Exception e) {
			System.err.println("Error Loading File");
			JOptionPane.showMessageDialog(frame, "Could Not Load Level", "Error", JOptionPane.ERROR_MESSAGE);
			closer();
		}
	}

	private void basicOps_1() {
		frame = new JDialog(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getFullScreenWindow(),
				"Level Tester");
		frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		frame.addWindowListener(closer());
		frame.setResizable(false);
		frame.addKeyListener(this);
	}

	private void basicOps_2() {
		Container canvas = frame.getContentPane();
		canvas.add(this);

		frame.pack();
		check.start();
		frame.setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		for (Object obj : allObjects) {
			if (obj instanceof Platform)
				((Platform) obj).drawObject(g2d);
			else if (obj instanceof StarShape) {
				AffineTransform old = g2d.getTransform();

				StarShape s = (StarShape) obj;

				int centerX = s.getX() / 2;
				int centerY = s.getY() / 2;

				g2d.rotate(starRotateAngle, centerX, centerY);

				g2d.setColor(Color.YELLOW);
				g2d.fill(s.getShape());
				g2d.setColor(Color.BLACK);
				g2d.draw(s.getShape());

				g2d.setTransform(old);
			} else if (obj instanceof TextBox)
				((TextBox) obj).drawObject(g2d);
		}

		if (ball != null)
			ball.drawObject(g2d);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == check) {
			ball.moveBall();
			boolean platCol = false;
			int platIndex = -1;
			// Checks if the ball has collided with any platforms
			for (int x = 0; x < plats.size(); x++) {
				if (ballPlatCollision(plats.get(x), ball)) {
					platCol = true;
					platIndex = x;
					break;
				} else
					platCol = false;
			}

			// Handles the collisions
			if (platCol) {
				int side = plats.get(platIndex).checkSide(ball);
				if (side == Platform.TOP) {
					ball.setDirectUp();
					if (plats.get(platIndex).getType() == Platform.BOOST)
						ball.setUpSpeed(plats.get(platIndex).getBoost());
					else
						ball.resetUpSpeed();
				} else if (side == Platform.BOTTOM)
					ball.setDirectDown();
				else if (side == Platform.LEFT)
					ball.pushLeft();
				else if (side == Platform.RIGHT)
					ball.pushRight();
			}

			// Checks if the ball collides with a star
			for (int x = 0; x < stars.size(); x++) {
				if (starCollision(stars.get(x), ball)) {
					for (int i = 0; i < allObjects.size(); i++)
						if (allObjects.get(i).equals(stars.get(x)))
							allObjects.remove(i);
					stars.remove(x);
				}
			}

			// Moves the moving platforms
			for (Platform p : plats)
				if (p.getType() == Platform.MOVING_HORIZONTAL || p.getType() == Platform.MOVING_VERTICAL)
					p.movePlatform();

			// Checks if there are no more stars left
			if (stars.isEmpty())
				close();

			// Rotates the stars
			if (starRotateAngle < 360)
				starRotateAngle += StarShape.STAR_ROTATE_SPEED;
			else
				starRotateAngle = 0;
		}
		repaint();
	}

	private boolean ballPlatCollision(Platform p, BouncyBall b) {
		Area pA = new Area(p.getShape());
		Ellipse2D ballShape = new Ellipse2D.Double(b.getX() - b.getDiameter() / 2,
				b.getY() - b.getDiameter() / 2 + b.getDownSpeed(), b.getDiameter(), b.getDiameter());
		Area bA = new Area(ballShape);
		pA.intersect(bA);
		return !pA.isEmpty();
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
			ball.setLeft(true);
		else if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
			ball.setRight(true);
	}

	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
			ball.setLeft(false);
		else if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
			ball.setRight(false);
	}

	private boolean starCollision(StarShape s, BouncyBall b) {
		Area sA = new Area(s.getShape());
		Area bA = new Area(b.getShape());
		sA.intersect(bA);
		return !sA.isEmpty();
	}

	private WindowListener closer() {
		return new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		};
	}

	private void close() {
		check.stop();
		check = null;
		frame.dispose();
	}

	public void keyTyped(KeyEvent e) {
	}
}
