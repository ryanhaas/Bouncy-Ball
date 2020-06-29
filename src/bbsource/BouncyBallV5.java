package bbsource;

/*
 * Created by Ryan Haas
 * Began Mid September
 * 
 * Approximately 8,129 lines of code as of 12/17/2016
 * Approximately 9,179 lines of code as of 2/17/2017
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import editor.EditorMenu;
import resources.ColorTransitioner;
import resources.LevelLoader;
import resources.components.ComponentMover;
import resources.objects.BouncyBall;
import screens.*;

public class BouncyBallV5 extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener, FocusListener {
	private ColorTransitioner ct = new ColorTransitioner(
			new Color[] { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA },
			8);

	// Version Number
	public static final String VERSION = "5.0.0";
	public static final String LAST_BENCHMARK = "February 17, 2017";

	private JFrame frame = new JFrame("Bouncy Ball v5");
	private Container canvas;

	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 300;

	// Screens
	private GameMenu gameMenu;
	private About about;
	private Help help;
	private EditorMenu editorMenu;
	private Settings settings;
	private PlayMenu playMenu;
	private final String[] allScreens = { "Menu", "About", "Help", "Editor", "Settings", "Play" };

	// Layout/Screen Controller
	private CardLayout screenController = new CardLayout();
	private JPanel screenContainer = new JPanel(screenController);

	// r

	// Used to move frame while being undecorated
	private ComponentMover cm = new ComponentMover();

	private Timer refresh = new Timer(1000 / 60, this);
	private Timer clearGarbage = new Timer(10000, this);
	private Timer minimizeTimer = new Timer(1000 / 30, this);

	// Header Buttons
	private JButton quit = new JButton("\u2715");
	private JButton minimize = new JButton("\u2015");
	private JButton back = new JButton("\u2190");

	// Various Colors
	private final Color buttonColor = new Color(0, 150, 250);
	private Color mouseLineColor = new Color(0, 150, 250);
	public static final Color BG_COLOR = new Color(210, 210, 210);

	// Screen ints (to set and change screens)
	public static final int GAME_MENU = 1;
	public static final int PLAY_MENU = 2;
	public static final int SETTINGS = 3;
	public static final int ABOUT = 4;
	public static final int HELP = 5;
	public static final int EDITOR_MENU = 6;
	public static final int LEVEL_LOADER = 7;
	private int screen;

	// In order for back button to work
	private ArrayList<Integer> screenHistory = new ArrayList<Integer>();
	private int screenCounter = -1;

	// Fonts
	public Font comfortaa;
	public Font samsung1;
	public Font blackTuesday;
	public Font timeburner;
	public Font apple2;
	public Font custom3;
	public Font openSans;

	// Booleans, self-explanatory
	private boolean outOfFocus;
	private boolean consoleOpen = false;

	// Components for header
	private JPanel headPane;
	private JLabel headTitle;
	private JPanel buttonPane;

	// Root path for files and saves
	/*
	 * public static final String FILE_PATH =
	 * ((BouncyBallV5.class.getResource("BouncyBallV5.class").toString().
	 * startsWith("jar") ? ((System.getProperty("os.name")).contains("Mac") ?
	 * "BB5_Info/" : ".BB5_Info/") :
	 * ((System.getProperty("os.name")).contains("Mac") ?
	 * BouncyBallV5.class.getProtectionDomain().getCodeSource().getLocation().
	 * getPath() + "BB5_Info/" :
	 * BouncyBallV5.class.getProtectionDomain().getCodeSource().getLocation().
	 * getPath() + ".BB5_Info/")));
	 */
	public static final String FILE_PATH = ((BouncyBallV5.class.getResource("BouncyBallV5.class").toString()
			.startsWith("jar") ? "BouncyBallFiles/"
					: BouncyBallV5.class.getProtectionDomain().getCodeSource().getLocation().getPath()
							+ "BouncyBallFiles/"));

	// Directories
	public static final File LOG_DIR = new File(FILE_PATH + "logs/");
	public static final File LEVELDIR = new File(FILE_PATH + "levels/");
	public static final File CUSTOM_LEVEL_DIR = new File(FILE_PATH + "levels/custom/");
	private static final File GAME_LEVEL_DIR = new File(FILE_PATH + "levels/game/");

	// Files
	public static final File ADMIN_FILE = new File(FILE_PATH + "admins.txt");
	public static final File SETTINGS_FILE = new File(FILE_PATH + "settings.txt");

	// Icon File
	public ImageIcon fav;
	public ImageIcon helpImg;
	public ImageIcon saveImg;
	public ImageIcon saveAsImg;

	// For Log File Output
	private File logFile;
	private BufferedWriter logWriter;

	// Settings Variables
	private boolean isAntialised;
	private boolean musicOn;
	private Color ballColor;
	private boolean isCustomColor;
	private boolean changingColor;
	private int[] unlockedLevels;

	// Levels Arr;
	public File[][] levels = new File[2][21];
	public LevelLoader levelLoader = new LevelLoader(this);

	private class TranslucentPane extends JPanel {
		public TranslucentPane() {
			setOpaque(false);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.Src.derive(0.0f));
			g2d.setColor(getBackground());
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	private final class Splash extends JPanel {
		private final JFrame frame = new JFrame();
		private final ImageIcon load = new ImageIcon(getClass().getResource("/resources/images/load.gif"));
		private String status = "BouncyBall Started";
		private long statusDelay;
		private Thread checkThread;
		private boolean loaded = false;

		public Splash() {
			frame.setUndecorated(true);
			frame.setBackground(new Color(0, 0, 0, 0));
			frame.setContentPane(new TranslucentPane());
			frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			statusDelay = 0;

			checkThread = new Thread(new Runnable() {
				public void run() {
					stall(10000);
					if(!loaded) {
						System.out.println("Rip");
						log("rip");
						log("-------");
						
						frame.setVisible(false);
						JOptionPane.showMessageDialog(frame, "Error Loading Game\nLog File At: " + logFile, "Error", JOptionPane.ERROR_MESSAGE, null);
						System.exit(0);
					}
				}
			});
			checkThread.start();

			setPreferredSize(new Dimension(load.getIconWidth(), load.getIconHeight()));
			Container canvas = frame.getContentPane();
			canvas.add(this);
			Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
			frame.setLocation(p.x - getPreferredSize().width / 2, p.y - getPreferredSize().height / 2);
			frame.pack();
			frame.setVisible(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.drawImage(load.getImage(), 0, 0, this);
			g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			g2d.setColor(Color.BLACK);
			g2d.drawString(status, 16, getHeight() - 10);
		}

		public void end() {
			frame.setCursor(Cursor.getDefaultCursor());
			frame.dispose();
			loaded = true;
		}

		public void setStatus(String status) {
			this.status = status;
			repaint();
			stall(statusDelay);
		}

		@SuppressWarnings("unused")
		public void setDelay(long delay) {
			statusDelay = delay;
		}

		public void stall(long millis) {
			try {
				Thread.sleep(millis);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public BouncyBallV5(int locX, int locY) {
		// Basic Window Setup
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		long init = System.currentTimeMillis();
		Splash s = new Splash();
		s.setStatus("Setting Up Environment");
		frame.addWindowListener(closer());
		frame.setLocation(locX, locY);
		frame.setUndecorated(true);
		frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		frame.addKeyListener(this);
		frame.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		frame.addFocusListener(this);
		frame.setResizable(false);
		setBackground(BG_COLOR);

		int numOfTiers = 2;
		unlockedLevels = new int[numOfTiers];

		createDirectories(s);
		checkSettings(s);
		loadImages(s);
		extractLevels(s, false);
		loadLevels(s);
		log("-------");

		// Adds the initial screen
		screen = GAME_MENU;
		screenHistory.add(screen);
		addHeader();

		// y

		System.out.println((int) (5000 * Math.random() + 1));

		addMouseListener(this);
		addMouseMotionListener(this);

		canvas = frame.getContentPane();
		canvas.add(this);

		// Allows Mac computers to use the menu bar
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		s.setStatus("Loading Fonts");
		// Registers all Fonts, and sets the UI Look and Feel to the system's
		try {
			String[] sourceStrings = { "resources\\fonts\\comfortaa.ttf", "resources\\fonts\\samsung.ttf",
					"resources\\fonts\\black_tuesday.ttf", "resources\\fonts\\timeburnernormal.ttf",
					"resources\\fonts\\Apple2.ttf", "resources\\fonts\\MyCustomFont3.ttf",
					"resources\\fonts\\OpenSans-Regular.ttf" };
			comfortaa = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[0].replace("\\", "/")));
			s.setStatus(sourceStrings[0]);
			samsung1 = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[1].replace("\\", "/")));
			s.setStatus(sourceStrings[1]);
			blackTuesday = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[2].replace("\\", "/")));
			s.setStatus(sourceStrings[2]);
			timeburner = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[3].replace("\\", "/")));
			s.setStatus(sourceStrings[3]);
			apple2 = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[4].replace("\\", "/")));
			s.setStatus(sourceStrings[4]);
			custom3 = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[5].replace("\\", "/")));
			openSans = Font.createFont(Font.TRUETYPE_FONT,
					getClass().getClassLoader().getResourceAsStream(sourceStrings[6].replace("\\", "/")));
			s.setStatus(sourceStrings[6]);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			s.setStatus("Registering Fonts");
			ge.registerFont(comfortaa);
			ge.registerFont(samsung1);
			ge.registerFont(blackTuesday);
			ge.registerFont(timeburner);
			ge.registerFont(apple2);
			ge.registerFont(custom3);
			ge.registerFont(openSans);

			log("Fonts registered");

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
			log("Error Registering Fonts");
		}

		s.setStatus("Settings Dock Image");
		// Sets the icon of the window
		frame.setIconImage(fav.getImage());
		try {
			Class.forName("com.apple.eawt.Application");
			com.apple.eawt.Application.getApplication().setDockIconImage(frame.getIconImage());
		} catch(Exception e) {
			// e.printStackTrace();
		}
		System.out.println("Running " + System.getProperty("os.name"));
		log("Running " + System.getProperty("os.name"));

		frame.pack();

		s.setStatus("Creating Screens");
		// Initializes all the screens
		gameMenu = new GameMenu(this, ballColor);
		about = new About(this);
		help = new Help(this);
		editorMenu = new EditorMenu(this);
		settings = new Settings(this);
		playMenu = new PlayMenu(this);

		// Adds the screen to the controller panel
		frame.add(screenContainer, BorderLayout.CENTER);
		screenContainer.add(this, allScreens[0]);
		screenContainer.add(about, allScreens[1]);
		screenContainer.add(help, allScreens[2]);
		screenContainer.add(editorMenu, allScreens[3]);
		screenContainer.add(settings, allScreens[4]);
		screenContainer.add(playMenu, allScreens[5]);

		// Shows the menu
		screenController.show(screenContainer, allScreens[0]);

		// Registers components, allowing the frame to move around the screen
		// while undecorated == true
		cm.registerComponent(frame, about, help, editorMenu, settings, playMenu, this);
		cm.setChangeCursor(false);
		/*
		 * if(System.getProperty("os.name").contains("Windows"))
		 * cm.setEdgeInsets(new Insets(-1000, -1000, 1000, 1000));
		 */

		// Starts the timers
		refresh.start();
		clearGarbage.start();

		log("Environment setup successful");
		s.setStatus("Done");
		int stallTime = 200;
		s.stall(stallTime);

		// Runs Garbage Collector
		Runtime.getRuntime().gc();
		System.out.println("Load Time: " + ((System.currentTimeMillis() - init - stallTime) / 1000.0) + " seconds");
		log("Loading Time: " + ((System.currentTimeMillis() - init - stallTime) / 1000.0) + " seconds");
		s.end();

		// Sets the window visible
		frame.setVisible(true);
		outOfFocus = false;
	}

	private void extractLevels(Splash s, boolean extractCBBLs) {
		try {
			InputStream zipLoc = BouncyBallV5.class.getClassLoader().getResourceAsStream("resources/levels.zip");
			if(zipLoc != null) {
				ZipInputStream zipstream = new ZipInputStream(zipLoc);
				byte[] buf = new byte[1024];
				while(true) {
					ZipEntry e = zipstream.getNextEntry();
					if(e == null) break;
					if(!e.isDirectory()) {
						File entry = new File(e.getName());
						File parent = entry.getParentFile();

						boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

						File tempDir = new File(GAME_LEVEL_DIR + ((isMac) ? File.separator : "/")
								+ parent.getPath().substring(parent.getPath().indexOf(File.separator) + 1));
						if(!tempDir.isDirectory() && !tempDir.exists()) tempDir.mkdir();
						String newFileName = e.getName()
								.substring(e.getName().lastIndexOf((isMac) ? File.separator : "/"));
						File toOutput = new File(tempDir + newFileName);
						if(!toOutput.exists() && (toOutput.toString().endsWith(".bb5") || extractCBBLs)) {
							try {
								toOutput.createNewFile();
							} catch(Exception e1) {
								e1.printStackTrace();
							}
							FileOutputStream fos = new FileOutputStream(toOutput);
							int n;
							while((n = zipstream.read(buf, 0, 1024)) > -1)
								fos.write(buf, 0, n);
							fos.close();
							if(toOutput.toString().endsWith(".bb5")) toOutput.setWritable(false);
							if(s != null) s.setStatus("Extracting: " + e.getName());
						}
						else {

						}
					}
				}
				zipstream.close();
			}
			else
				System.out.println("crap");
		} catch(Exception e) {
			System.err.println("Error Extracting Levels");
			if(s != null) {
				s.setStatus("Error Extracting Levels");
				s.stall(1000);
			}
			e.printStackTrace();
		}
	}

	public void extractCBBLs() {
		extractLevels(null, true);
	}

	private void loadLevels(Splash s) {
		s.setStatus("Loading Levels");
		File initSource = GAME_LEVEL_DIR;
		File bbFile = new File(FILE_PATH);
		try {
			if(initSource.listFiles().length > 0) {
				for(int x = 0; x < initSource.listFiles().length; x++) {
					if(initSource.listFiles()[x].isDirectory()) {
						if(initSource.listFiles()[x].listFiles().length > 0) {
							for(int i = 0; i < initSource.listFiles()[x].listFiles().length; i++) {
								levels[x][i] = initSource.listFiles()[x].listFiles()[i];
								s.setStatus(initSource.listFiles()[x].listFiles()[i].getAbsolutePath()
										.substring(bbFile.getAbsoluteFile().getParentFile().getPath().length() - 1
												- bbFile.getAbsoluteFile().getParentFile().getName().length()));
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void loadImages(Splash s) {
		s.setStatus("Loading Images");
		String[] sourceStrings = { "\\resources\\images\\favicon.png", "\\resources\\images\\helpImg.png",
				"\\resources\\images\\saveImg.png", "\\resources\\images\\saveAsImg.png" };
		fav = new ImageIcon(getClass().getResource(sourceStrings[0].replace("\\", "/")));
		s.setStatus(sourceStrings[0]);
		helpImg = new ImageIcon(getClass().getResource(sourceStrings[1].replace("\\", "/")));
		s.setStatus(sourceStrings[1]);
		saveImg = new ImageIcon(getClass().getResource(sourceStrings[2].replace("\\", "/")));
		s.setStatus(sourceStrings[2]);
		saveAsImg = new ImageIcon(getClass().getResource(sourceStrings[3].replace("\\", "/")));
		s.setStatus(sourceStrings[3]);
	}

	private void createDirectories(Splash s) {
		s.setStatus("Creating Directories");
		// Checks if FILE_PATH exists
		File bbFile = new File(FILE_PATH);
		if(!bbFile.isDirectory()) {
			bbFile.mkdir();
			s.setStatus(bbFile.getAbsolutePath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
					- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
		}

		// Checks if logDirectory exists
		if(!LOG_DIR.isDirectory()) {
			LOG_DIR.mkdir();
			s.setStatus(LOG_DIR.getAbsolutePath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
					- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
		}

		if(!LEVELDIR.isDirectory()) {
			LEVELDIR.mkdir();
			s.setStatus(LEVELDIR.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length() - 1
					- bbFile.getAbsoluteFile().getParentFile().getName().length()));
		}

		if(!CUSTOM_LEVEL_DIR.isDirectory()) {
			CUSTOM_LEVEL_DIR.mkdir();
			s.setStatus(CUSTOM_LEVEL_DIR.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
					- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
		}

		if(!GAME_LEVEL_DIR.isDirectory()) {
			GAME_LEVEL_DIR.mkdir();
			s.setStatus(GAME_LEVEL_DIR.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
					- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
		}

		// For log file
		DateTime dt = new DateTime();
		String year = Integer.toString(dt.getYear());
		String month = Integer.toString(dt.getMonthOfYear()).length() < 2 ? "0" + Integer.toString(dt.getMonthOfYear())
				: Integer.toString(dt.getMonthOfYear());
		String day = Integer.toString(dt.getDayOfMonth()).length() < 2 ? "0" + Integer.toString(dt.getDayOfMonth())
				: Integer.toString(dt.getDayOfMonth());
		String hour = "";
		if(dt.getHourOfDay() > 12)
			if(Integer.toString(dt.getHourOfDay() - 12).length() < 2)
				hour = "0" + Integer.toString(dt.getHourOfDay() - 12);
			else
				hour = Integer.toString(dt.getHourOfDay() - 12);
		else
			hour = Integer.toString(dt.getHourOfDay());

		String minute = Integer.toString(dt.getMinuteOfHour()).length() < 2
				? "0" + Integer.toString(dt.getMinuteOfHour()) : Integer.toString(dt.getMinuteOfHour());
		String second = Integer.toString(dt.getSecondOfMinute()).length() < 2
				? "0" + Integer.toString(dt.getSecondOfMinute()) : Integer.toString(dt.getSecondOfMinute());

		logFile = new File(LOG_DIR.getPath() + "/log" + year + "-" + month + "-" + day + "-" + hour + "-" + minute + "-"
				+ second + ".txt");

		try {
			if(!logFile.exists()) {
				logFile.createNewFile();
				s.setStatus(logFile.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length() - 1
						- bbFile.getAbsoluteFile().getParentFile().getName().length()));
			}

			logWriter = new BufferedWriter(new FileWriter(logFile));

			if(!ADMIN_FILE.exists()) {
				ADMIN_FILE.createNewFile();
				s.setStatus(ADMIN_FILE.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
						- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
			}
			if(!SETTINGS_FILE.exists()) {
				SETTINGS_FILE.createNewFile();
				s.setStatus(
						SETTINGS_FILE.getPath().substring(bbFile.getAbsoluteFile().getParentFile().getPath().length()
								- 1 - bbFile.getAbsoluteFile().getParentFile().getName().length()));
				createDefaultSettings();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Adds antialiasing to make graphics look better
		Graphics2D g2d = (Graphics2D) g;
		if(isAntialised) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}

		// Draws the Menu
		if(screen == GAME_MENU)
			gameMenu.drawGameMenu(g2d);
		else if(screen == LEVEL_LOADER) levelLoader.drawLevel(g2d);
	}

	// Adds the title bar
	private void addHeader() {
		headPane = new JPanel();
		headPane.setLayout(new BoxLayout(headPane, BoxLayout.LINE_AXIS));
		headPane.setBackground(mouseLineColor);

		buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
		buttonPane.setBackground(mouseLineColor);

		Font buttonFont = new Font("", Font.PLAIN, 18);

		int width = 35;
		// Minimize Button
		minimize.setFocusable(false);
		minimize.setPreferredSize(new Dimension(width, 20));
		minimize.setMargin(new Insets(0, 0, 0, 0));
		minimize.setOpaque(false);
		minimize.setBorder(null);
		minimize.setForeground(Color.WHITE);
		minimize.setOpaque(true);
		minimize.setFont(buttonFont);
		minimize.setBackground(buttonColor);

		// Quit Button
		quit.setFocusable(false);
		quit.setPreferredSize(new Dimension(width, 20));
		quit.setMargin(new Insets(0, 0, 0, 0));
		quit.setBorder(null);
		quit.setForeground(Color.WHITE);
		quit.setOpaque(true);
		quit.setFont(buttonFont);
		quit.setBackground(buttonColor);

		// Back Button
		back.setFocusable(false);
		back.setPreferredSize(new Dimension(width, 20));
		back.setMargin(new Insets(0, 0, 0, 0));
		back.setOpaque(false);
		back.setBorder(null);
		back.setForeground(Color.WHITE);
		back.setOpaque(true);
		back.setFont(buttonFont);
		back.setBackground(buttonColor);

		buttonPane.add(back);
		back.setVisible(false);

		buttonPane.add(minimize);
		buttonPane.add(quit);

		// Title Label
		headTitle = new JLabel("Bouncy Ball");
		headTitle.setBorder(new EmptyBorder(0, 5, 0, 0));
		headTitle.setFont(new Font("Open Sans", Font.BOLD, 16));
		headTitle.setForeground(Color.WHITE);
		headTitle.setOpaque(true);
		headTitle.setFocusable(false);
		headTitle.setBackground(mouseLineColor);
		headPane.setFocusable(false);
		buttonPane.setFocusable(false);

		headPane.add(headTitle);
		headPane.add(Box.createHorizontalGlue());
		headPane.add(buttonPane);

		minimize.addActionListener(this);
		quit.addActionListener(this);
		back.addActionListener(this);

		minimize.addMouseListener(this);
		quit.addMouseListener(this);
		back.addMouseListener(this);

		headPane.setPreferredSize(new Dimension(headPane.getPreferredSize().width, 24));
		frame.add(headPane, BorderLayout.NORTH);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == refresh) {
			repaint();
			if(screen == GAME_MENU)
				gameMenu.moveSample();
			else if(screen == EDITOR_MENU) {
				// Animates Editor Menu Buttons
				editorMenu.moveCreate();
				editorMenu.moveEdit();
				editorMenu.movePlay();
			}
			else if(screen == PLAY_MENU)
				playMenu.moveMenu();
			else if(screen == LEVEL_LOADER) levelLoader.levelOps();

			if(changingColor) {
				ct.transitionColor();
				if(screen == GAME_MENU) {
					gameMenu.setBallColor(ct.getColor());
					gameMenu.setInnerColor(Color.LIGHT_GRAY);
				}
				else if(screen == LEVEL_LOADER) {
					levelLoader.setBallColor(ct.getColor());
					levelLoader.setInnerBallColor(Color.LIGHT_GRAY);
				}
			}
		}
		else if(source == quit) {
			try {
				log("Exiting");
				log("-------");
				logWriter.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
		else if(source == minimize) {
			log("Frame minimized");
			frame.setState(JFrame.ICONIFIED);
			refresh.stop();
			minimizeTimer.start();
		}
		else if(source == clearGarbage)
			// Runs Garabage Collector
			Runtime.getRuntime().gc();
		else if(source == back)
			moveBackScreen();
		else if(source == minimizeTimer) {
			if(frame.getState() != JFrame.ICONIFIED) {
				minimizeTimer.stop();
				refresh.start();
				log("Frame re-opened");
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();
		if(!outOfFocus) {
			if(screen == GAME_MENU) {
				// Checks if the moused was pressed over the Menu Buttons
				gameMenu.clickedButtons(e.getX(), e.getY());

				// Shows screens
				if(screen == ABOUT)
					screenController.show(screenContainer, allScreens[1]);
				else if(screen == HELP)
					screenController.show(screenContainer, allScreens[2]);
				else if(screen == EDITOR_MENU) {
					screenController.show(screenContainer, allScreens[3]);
					changeSize(DEFAULT_WIDTH, frame.getHeight() - 30);
				}
				else if(screen == SETTINGS) {
					screenController.show(screenContainer, allScreens[4]);
					changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT + 30);
				}
				else if(screen == PLAY_MENU) screenController.show(screenContainer, allScreens[5]);
			}
			else if(screen == EDITOR_MENU)
				if(source == editorMenu.createButton)
					editorMenu.createPressed();
				else if(source == editorMenu.editButton)
					editorMenu.editPressed();
				else if(source == editorMenu.playButton)
					editorMenu.playPressed();
				else
					;
			else if(screen == PLAY_MENU) playMenu.mousePressedButtons(e);
		}
	}

	// a

	public void mouseReleased(MouseEvent e) {
		Object source = e.getSource();
		if(!outOfFocus) {
			if(screen == EDITOR_MENU) if(source == editorMenu.createButton)
				editorMenu.createReleased();
			else if(source == editorMenu.editButton)
				editorMenu.editReleased();
			else if(source == editorMenu.playButton) editorMenu.playReleased();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(!outOfFocus) if(screen == GAME_MENU)
			gameMenu.mouseButtons(e.getX(), e.getY());
		else if(screen == PLAY_MENU) playMenu.mouseMovedButtons(e);
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		Object source = e.getSource();
		if(!outOfFocus) {
			if(source == quit)
				quit.setBackground(Color.RED);
			else if(source == minimize)
				minimize.setBackground(new Color(48, 174, 255));
			else if(source == back) back.setBackground(new Color(48, 174, 255));

			if(screen == EDITOR_MENU)
				if(source == editorMenu.createButton)
					editorMenu.createEntered();
				else if(source == editorMenu.editButton)
					editorMenu.editEntered();
				else if(source == editorMenu.playButton)
					editorMenu.playEntered();
				else
					;
			else if(screen == PLAY_MENU) playMenu.mouseEnteredButtons(e);
		}
		else if(source == quit)
			quit.setBackground(Color.RED);
		else if(source == minimize)
			minimize.setBackground(mouseLineColor.darker());
		else if(source == back)
			back.setBackground(mouseLineColor.darker());
		else
			;
		repaint();
	}

	public void mouseExited(MouseEvent e) {
		Object source = e.getSource();
		if(source == quit)
			quit.setBackground(mouseLineColor);
		else if(source == minimize)
			minimize.setBackground(mouseLineColor);
		else if(source == back) back.setBackground(mouseLineColor);

		/*
		 * if(screen == EDITOR_MENU) if(source == editorMenu.createButton)
		 * editorMenu.createExited(); else if(source == editorMenu.editButton)
		 * editorMenu.editExited(); else if(source == editorMenu.playButton)
		 * editorMenu.playExited(); else; else
		 */if(screen == PLAY_MENU) playMenu.mouseExitedButtons(e);
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_BACK_QUOTE) {
			if(!consoleOpen) {
				// Opens Console
				new Console(this);
				consoleOpen = true;
				log("Opening Console");
			}
		}
		else if(k == KeyEvent.VK_1) {
			frame.setLocation(10, 10);
			log("Moving frame to 10x10");
		}
		else if(k == KeyEvent.VK_2) {
			frame.setLocation(1930, 10);
			log("Moving frame to 1930x10");
		}
		else if(k == KeyEvent.VK_BACK_SPACE)
			if(back.isVisible())
				moveBackScreen();
			else
				;
		else if(k == KeyEvent.VK_S) System.out.println(getScreenText());

		if(screen == GAME_MENU) {
			if(k == KeyEvent.VK_UP)
				gameMenu.moveSelectedUp();
			else if(k == KeyEvent.VK_DOWN)
				gameMenu.moveSelectedDown();
			else if(k == KeyEvent.VK_ENTER) gameMenu.enterFunc();
		}
		else if(screen == EDITOR_MENU) {
			if(k == KeyEvent.VK_UP)
				editorMenu.moveSelectedUp();
			else if(k == KeyEvent.VK_DOWN)
				editorMenu.moveSelectedDown();
			else if(k == KeyEvent.VK_ENTER) editorMenu.enterFunc();
		}
		else if(screen == PLAY_MENU)
			playMenu.keyMethod(e);
		else if(screen == LEVEL_LOADER) levelLoader.keyPressedMethod(e);
	}

	public void keyReleased(KeyEvent e) {
		if(screen == LEVEL_LOADER) levelLoader.keyReleasedMethod(e);
	}

	public void setScreen(int screen) {
		// Sets the screen and adds the previous screen to the history
		// to allow use of the back button
		if(this.screen == LEVEL_LOADER && screen == PLAY_MENU) screenController.show(screenContainer, allScreens[5]);

		if(this.screen != LEVEL_LOADER) screenHistory.add(this.screen);
		this.screen = screen;

		if(screen == LEVEL_LOADER) screenController.show(screenContainer, allScreens[0]);

		// Shows the back button as long as the screen
		// is not that game menu because it is unnecessary
		if(screen != GAME_MENU) setBackButton(true);

		if(screen != LEVEL_LOADER) screenCounter++;
		//frame.requestFocus();

		log("Moved to " + getScreenText() + " screen");
	}

	public String getScreenText() {
		if(screen == GAME_MENU)
			return "Game Menu";
		else if(screen == ABOUT)
			return "About";
		else if(screen == HELP)
			return "Help";
		else if(screen == EDITOR_MENU)
			return "Editor Menu";
		else if(screen == SETTINGS)
			return "Settings";
		else if(screen == LEVEL_LOADER)
			return "Level Loader";
		else if(screen == PLAY_MENU)
			return "Play Menu";
		else
			return "";
	}
	
	public String[] allScreens() {
		return new String[]{
				"Game Menu (menu)",
				"About",
				"Editor Menu (editor)",
				"Settings",
				"Help",
				"Play Menu (levels)"
		};
	}
	
	public int getScreen() {
		return screen;
	}

	// Serves same function as the standard setScreen() method
	// but is primarily for console functionality
	public void setScreenManually(int screen) {
		if(screen != GAME_MENU && screen != this.screen && screen != LEVEL_LOADER) {
			screenHistory.add(this.screen);
			screenCounter++;
		}
		this.screen = screen;

		if(screen == ABOUT) {
			screenController.show(screenContainer, allScreens[1]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(true);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		else if(screen == HELP) {
			screenController.show(screenContainer, allScreens[2]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(true);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		else if(screen == EDITOR_MENU) {
			screenController.show(screenContainer, allScreens[3]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(true);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT - 30);
		}
		else if(screen == GAME_MENU) {
			screenController.show(screenContainer, allScreens[0]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(false);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		else if(screen == SETTINGS) {
			screenController.show(screenContainer, allScreens[4]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(true);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT + 30);
		}
		else if(screen == PLAY_MENU) {
			screenController.show(screenContainer, allScreens[5]);
			setCursor(Cursor.getDefaultCursor());
			setBackButton(true);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}

		frame.requestFocus();

		log("Moved to " + getScreenText() + " screen");
	}

	// Changes the size of the window after it is already visible
	public void changeSize(Dimension d) {
		frame.setPreferredSize(d);
		canvas = frame.getContentPane();
		frame.pack();
	}

	public void changeSize(int w, int h) {
		Dimension d = new Dimension(w, h);
		frame.setPreferredSize(d);
		canvas = frame.getContentPane();
		frame.pack();
	}

	// Moves back a screen
	public void moveBackScreen() {
		boolean skip = false;
		if(screen == PLAY_MENU)
			if(playMenu.menuExtended())
				playMenu.resetMenu();
			else
				;
		else if(screen == LEVEL_LOADER) {
			setScreen(BouncyBallV5.PLAY_MENU);
			levelLoader.loadLevel(null);
			levelLoader.setLevel(-1, -1);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			skip = true;
		}

		if(!skip)
			screen = screenHistory.get(screenCounter);
		else
			screen = PLAY_MENU;

		if(screen == GAME_MENU) {
			setBackButton(false);
			screenController.show(screenContainer, allScreens[0]);
			changeSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		screenCounter--;
		frame.requestFocus();

		log("Moved back to " + getScreenText() + " screen");
	}

	// Checks to see if the window gains or loses focus
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if(source == frame) {
			refresh.start();
			mouseLineColor = new Color(0, 150, 250);
			headTitle.setBackground(mouseLineColor);
			buttonPane.setBackground(mouseLineColor);
			headPane.setBackground(mouseLineColor);
			quit.setBackground(mouseLineColor);
			minimize.setBackground(mouseLineColor);
			back.setBackground(mouseLineColor);

			settings.setAllEnabled(true);

			outOfFocus = false;
		}
	}

	public void focusLost(FocusEvent e) {
		final Object source = e.getSource();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(source == frame) checkFocusGained();
			}
		});
	}

	public void checkFocusGained() {
		if(!settings.fieldHasFocus() && !consoleOpen && !frame.hasFocus()) {
			refresh.stop();
			mouseLineColor = BG_COLOR;
			headTitle.setBackground(mouseLineColor);
			buttonPane.setBackground(mouseLineColor);
			headPane.setBackground(mouseLineColor);
			quit.setBackground(mouseLineColor);
			minimize.setBackground(mouseLineColor);
			back.setBackground(mouseLineColor);
			outOfFocus = true;
			settings.setAllEnabled(false);
			Runtime.getRuntime().gc();
		}
	}

	// Sets the back button's visibility
	public void setBackButton(boolean b) {
		back.setVisible(b);
	}

	public void log(String logTXT) {
		try {
			logWriter.write(logTXT + "\n");
			logWriter.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void closerLogger() {
		try {
			logWriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private WindowListener closer() {
		return new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				log("Exiting");
				log("-------");
				closerLogger();
				System.exit(0); // n
			}
		};
	}

	public JFrame getFrame() {
		return frame;
	}

	public boolean isConsoleOpen() {
		return consoleOpen;
	}

	public void setConsoleOpen(boolean b) {
		consoleOpen = b;
	}

	public Color getBallColor() {
		return ballColor;
	}

	public void setBallColor(Color bc) {
		ballColor = bc;
		gameMenu.setBallColor(bc);
	}

	public boolean isCustomColor() {
		return isCustomColor;
	}

	public void setCustomColor(boolean cc) {
		isCustomColor = cc;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean m) {
		musicOn = m;
	}

	public boolean isAntialiased() {
		return isAntialised;
	}

	public void setAntialiased(boolean a) {
		isAntialised = a;
	}

	public void createDefaultSettings() {
		isAntialised = true;
		musicOn = true;
		ballColor = Color.BLUE;
		isCustomColor = false;
		changingColor = false;
		unlockedLevels[0] = 1;
		unlockedLevels[1] = 0;
		if(playMenu != null) playMenu.recheckLevels();
		saveSettings(new SettingsSave(musicOn, isAntialised, ballColor, isCustomColor, unlockedLevels, changingColor));
	}

	public void saveSettings(SettingsSave ss) {
		try {
			FileOutputStream fos = new FileOutputStream(SETTINGS_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ss);
			oos.close();
			fos.close();

			isAntialised = ss.isAntialiased();
			musicOn = ss.isMusicOn();
			ballColor = ss.getBallColor();
			isCustomColor = ss.isCustomColor();
			changingColor = ss.changesColors();
			if(gameMenu != null) gameMenu.setBallColor(ballColor);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void checkSettings(Splash s) {
		s.setStatus("Checking Settings");
		try {
			FileInputStream fis = new FileInputStream(SETTINGS_FILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			SettingsSave ss = (SettingsSave) ois.readObject();
			if(!System.getProperty("os.name").contains("Mac"))
				isAntialised = ss.isAntialiased();
			else
				isAntialised = true;
			musicOn = ss.isMusicOn();
			ballColor = ss.getBallColor();
			isCustomColor = ss.isCustomColor();
			unlockedLevels = ss.unlockedLevels();
			changingColor = ss.changesColors();
			ois.close();
			fis.close();
		} catch(Exception e) {
			if(e instanceof InvalidClassException) {
				createDefaultSettings();
			}
		}
	}

	public int[] getUnlockedLevels() {
		return unlockedLevels;
	}

	public void setUnlockedLevels(int[] ul) {
		unlockedLevels = ul;
		playMenu.recheckLevels();
		saveSettings(getCurrentSettings());
	}

	public void setUnlockedLevel(int tier, int level) {
		if(unlockedLevels[tier] < level) {
			unlockedLevels[tier] = level;
			playMenu.recheckLevels();
			saveSettings(getCurrentSettings());
		}
	}

	public void setUnlockedLevelManually(int tier, int level) {
		unlockedLevels[tier] = level;
		if(unlockedLevels[tier] >= PlayMenu.NUM_OF_LEVELS && unlockedLevels.length > tier + 1)
			unlockedLevels[tier + 1] = 1;
		playMenu.recheckLevels();
		saveSettings(getCurrentSettings());
	}

	public SettingsSave getCurrentSettings() {
		return new SettingsSave(musicOn, isAntialised, ballColor, isCustomColor, unlockedLevels, changingColor);
	}

	// Unused Listener Functions
	public void mouseClicked(MouseEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
	
	public BouncyBall getBall() {
		return levelLoader.getBall();
	}
}