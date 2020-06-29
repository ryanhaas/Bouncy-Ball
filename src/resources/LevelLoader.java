package resources;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.ArrayList;

import bbsource.BouncyBallV5;
import editor.CustomSaver;
import resources.objects.*;
import resources.components.RoundRectText;;

public class LevelLoader {
	private BouncyBallV5 bb5;

	private ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
	private ArrayList<GameObject> initAllObjects = new ArrayList<GameObject>();
	private double starRotateAngle;

	private ArrayList<Platform> plats = new ArrayList<Platform>();
	private ArrayList<Platform> initPlats = new ArrayList<Platform>();
	private ArrayList<StarShape> stars = new ArrayList<StarShape>();
	private ArrayList<StarShape> initStars = new ArrayList<StarShape>();
	private BouncyBall ball;

	private boolean levelLoaded;

	private int tier;
	private int level;

	private boolean gameFinished = false;
	private boolean pause = false;

	private RoundRectText nxtLvl, back;

	private boolean blur = false;

	public LevelLoader(BouncyBallV5 bb5) {
		this.bb5 = bb5;
		levelLoaded = false;
	}

	public boolean loadLevel(File levelFile) {
		if(levelFile != null) {
			try {
				fullReset();
				FileInputStream fis = new FileInputStream(levelFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				CustomSaver cs = (CustomSaver) ois.readObject();
				allObjects = cs.getAllObjects();
				boolean rewrite = false;

				for(GameObject o : allObjects) {
					initAllObjects.add(o.clone());
					if(o instanceof Platform) {
						plats.add((Platform) o);
						initPlats.add(((Platform) o).clone());
					}
					else if(o instanceof StarShape) {
						stars.add((StarShape) o);
						initStars.add(((StarShape) o).clone());
					}
					else if(o instanceof BouncyBall) {
						BouncyBall temp = (BouncyBall) o;
						temp = new BouncyBall(temp.getX(), temp.getY(), temp.getDiameter(), Color.BLUE,
								temp.getRLSpeed(), temp.getDefaultUpSpeed());
						if(temp.getRLSpeed() == 0 || temp.getDefaultUpSpeed() == 0) {
							temp.resetToStandard();
							rewrite = true;
						}
						ball = temp;
						if(rewrite) allObjects.set(allObjects.indexOf(o), ball);
					}
				}

				bb5.changeSize(new Dimension(cs.getSize().width, cs.getSize().height + 27));
				ball.setColor(bb5.getBallColor());

				fis.close();
				ois.close();

				if(rewrite) {
					if(!levelFile.canWrite()) levelFile.setWritable(true);
					FileOutputStream fos = new FileOutputStream(levelFile);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(new CustomSaver(allObjects, cs.getSize()));
					fos.close();
					oos.close();
					if(levelFile.canWrite()) levelFile.setWritable(false);
					System.out.println("Needed Rewrite");
				}

				levelLoaded = true;
				gameFinished = false;
				return true;
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			levelLoaded = false;
			plats.clear();
			stars.clear();
			ball = null;
			return false;
		}
	}

	public void drawLevel(Graphics2D g2d) {
		if(levelLoaded && !pause) {
			for(Object obj : allObjects) {
				AffineTransform old = g2d.getTransform();
				if(obj instanceof Platform)
					((Platform) obj).drawObject(g2d);
				else if(obj instanceof StarShape) {

					StarShape s = (StarShape) obj;

					int centerX = s.getX() / 2;
					int centerY = s.getY() / 2;

					g2d.rotate(starRotateAngle, centerX, centerY);

					g2d.setColor(Color.YELLOW);
					g2d.fill(s.getShape());
					g2d.setColor(Color.BLACK);
					g2d.draw(s.getShape());

				}
				else if(obj instanceof TextBox) ((TextBox) obj).drawObject(g2d);
				g2d.setTransform(old);
			}

			if(ball != null) ball.drawObject(g2d);

			if(gameFinished) {
				g2d.setColor(new Color(0, 0, 0, 125));
				g2d.fillRect(0, 0, bb5.getWidth(), bb5.getHeight());
			}
		}
	}

	public void levelOps() {
		if(levelLoaded) {
			if(!gameFinished) {
				ball.moveBall();
				boolean platCol = false;
				int platIndex = -1;
				// Checks if the ball has collided with any platforms
				for(int x = 0; x < plats.size(); x++) {
					if(ballPlatCollision(plats.get(x), ball)) {
						platCol = true;
						platIndex = x;
						break;
					}
				}

				// Handles the collisions
				if(platCol) {
					int side = plats.get(platIndex).checkSide(ball);
					if(side == Platform.TOP) {
						ball.setDirectUp();
						if(plats.get(platIndex).getType() == Platform.BOOST)
							ball.setUpSpeed(plats.get(platIndex).getBoost());
						else
							ball.resetUpSpeed();
					}
					else if(side == Platform.BOTTOM)
						ball.setDirectDown();
					else if(side == Platform.LEFT)
						ball.pushLeft();
					else if(side == Platform.RIGHT) ball.pushRight();
				}

				// Checks if the ball collides with a star
				for(int x = 0; x < stars.size(); x++) {
					if(starCollision(stars.get(x), ball)) {
						for(int i = 0; i < allObjects.size(); i++)
							if(allObjects.get(i).equals(stars.get(x))) allObjects.remove(i);
						stars.remove(x);
					}
				}

				// Checks if ball is out of bounds
				if(ball.getMixX() < -ball.getDiameter() * 3 || ball.getMaxX() > bb5.getWidth() + ball.getDiameter() * 3
						|| ball.getMinY() > bb5.getHeight() + ball.getDiameter() * 3)
					reset();

				// Moves the moving platforms
				for(Platform p : plats)
					if(p.getType() == Platform.MOVING_HORIZONTAL || p.getType() == Platform.MOVING_VERTICAL)
						p.movePlatform();

				// Checks if there are no more stars left
				if(stars.isEmpty()) {
					bb5.setUnlockedLevel(tier, level + 1);
					gameFinished = true;
					if(bb5.levels[tier][level] != null) {
						loadLevel(bb5.levels[tier][level]);
						setLevel(tier, level + 1);
					}
					else {
						bb5.changeSize(BouncyBallV5.DEFAULT_WIDTH, BouncyBallV5.DEFAULT_HEIGHT);
						bb5.setScreen(BouncyBallV5.PLAY_MENU);
						bb5.changeSize(BouncyBallV5.DEFAULT_WIDTH, BouncyBallV5.DEFAULT_HEIGHT);
					}
				}

				// Rotates the stars
				if(starRotateAngle < 360)
					starRotateAngle += StarShape.STAR_ROTATE_SPEED;
				else
					starRotateAngle = 0;
			}
		}
	}

	private boolean ballPlatCollision(Platform p, BouncyBall b) {
		Area pA = new Area(p.getShape());
		Ellipse2D ballShape = new Ellipse2D.Double(b.getX() - b.getDiameter() / 2,
				b.getY() - b.getDiameter() / 2 + b.getDownSpeed(), b.getDiameter(), b.getDiameter());
		Area bA = new Area(ballShape);
		pA.intersect(bA);
		return !pA.isEmpty();
	}

	private boolean starCollision(StarShape s, BouncyBall b) {
		Area sA = new Area(s.getShape());
		Ellipse2D ballShape = new Ellipse2D.Double(b.getX() - b.getDiameter() / 2,
				b.getY() - b.getDiameter() / 2 + b.getDownSpeed(), b.getDiameter(), b.getDiameter());
		Area bA = new Area(ballShape);
		sA.intersect(bA);
		return !sA.isEmpty();
	}

	public void keyPressedMethod(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
			ball.setLeft(true);
		else if(k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
			ball.setRight(true);
		else if(k == KeyEvent.VK_B) blur = !blur;
	}

	public void keyReleasedMethod(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
			ball.setLeft(false);
		else if(k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT) ball.setRight(false);
	}

	public void setLevel(int tier, int level) {
		this.tier = tier;
		this.level = level;
	}

	public void setBallColor(Color c) {
		ball.setColor(c);
	}

	public void setInnerBallColor(Color c) {
		ball.setInnerColor(c);
	}

	private void fullReset() {
		plats.clear();
		stars.clear();
		allObjects.clear();
		initPlats.clear();
		initStars.clear();
		initAllObjects.clear();
		ball = null;
	}

	private void reset() {
		loadLevel(bb5.levels[tier][level - 1]);
	}

	public boolean getGameFinished() {
		return gameFinished;
	}

	public void mouseMoveFunc(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}
	
	public BouncyBall getBall() {
		return ball;
	}
}
