package screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import bbsource.BouncyBallV5;
import resources.LimitTextField;
import resources.components.JLabelWithFont;

public class Settings extends JPanel implements ActionListener {
	private BouncyBallV5 bb5;

	private JTextField customR = new JTextField("0", 10), customG = new JTextField("0", 10),
			customB = new JTextField("0", 10);

	private String[] colorOptions = { "Blue", "Black", "Yellow", "Cyan", "Red", "Green", "Gray", "Magenta", "Orange",
			"Pink", "Dynamic", "Random", "Custom" };
	private final JComboBox aBox = new JComboBox(new String[] { "Enabled", "Disabled" });
	private final JComboBox colorSelector = new JComboBox(colorOptions);
	private final JComboBox mBox = new JComboBox(new String[] { "Enabled", "Disabled" });

	private JButton apply = new JButton("Apply");
	private JButton cancel = new JButton("Cancel");

	private SettingsSave initSettings;
	private SettingsSave currentSettings;

	private boolean isAntialiased;
	private boolean musicOn;
	private Color ballColor;
	private boolean isCustomColor;
	private boolean dynamicBallColor;

	private boolean fieldHasFocus = false;

	public Settings(BouncyBallV5 b) {
		this.bb5 = b;
		initSettings = bb5.getCurrentSettings();
		setSettingVars();
		currentSettings = bb5.getCurrentSettings();
		setBackground(bb5.getBackground());
		// setLayout(new GridLayout(1, 2, 5, 0));
		setLayout(new BorderLayout());
		setOpaque(true);

		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			aBox.setSelectedItem("Enabled");
			aBox.setEnabled(false);
		}

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				bb5.getFrame().requestFocus();
			}
		});

		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK),
				"Settings");
		// tb.setTitleFont(tb.getTitleFont().deriveFont(22f));
		tb.setTitleFont(new Font("Segoe UI", Font.PLAIN, 28));
		tb.setTitleJustification(TitledBorder.CENTER);

		setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 100, 10, 100), tb));

		makeLayout();
		addAllListeners();
	}

	private void addAllListeners() {
		KeyListener colorListener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					bb5.getFrame().requestFocus();

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						apply.setEnabled(!sameSettings());
					}
				});
			}
		};

		FocusListener fieldListener = new FocusListener() {
			public void focusLost(FocusEvent e) {
				fieldHasFocus = false;
				System.out.println("lost");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						bb5.checkFocusGained();
					}
				});
			}

			public void focusGained(FocusEvent e) {
				fieldHasFocus = true;
				System.out.println("gained");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						bb5.checkFocusGained();
					}
				});
			}
		};

		customR.addKeyListener(colorListener);
		customG.addKeyListener(colorListener);
		customB.addKeyListener(colorListener);
		customR.addFocusListener(fieldListener);
		customG.addFocusListener(fieldListener);
		customB.addFocusListener(fieldListener);

		colorSelector.addActionListener(this);
		aBox.addActionListener(this);
		mBox.addActionListener(this);

		apply.addActionListener(this);
		cancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == colorSelector) {
			if (colorSelector.getSelectedItem().equals("Custom"))
				isCustomColor = true;
			else
				isCustomColor = false;

			if (colorSelector.getSelectedItem().equals("Dynamic"))
				dynamicBallColor = true;
			else
				dynamicBallColor = false;

			if (colorSelector.getSelectedItem().equals("Blue"))
				ballColor = Color.BLUE;
			else if (colorSelector.getSelectedItem().equals("Black"))
				ballColor = Color.BLACK;
			else if (colorSelector.getSelectedItem().equals("Yellow"))
				ballColor = Color.YELLOW;
			else if (colorSelector.getSelectedItem().equals("Cyan"))
				ballColor = Color.CYAN;
			else if (colorSelector.getSelectedItem().equals("Red"))
				ballColor = Color.RED;
			else if (colorSelector.getSelectedItem().equals("Green"))
				ballColor = Color.GREEN;
			else if (colorSelector.getSelectedItem().equals("Gray"))
				ballColor = Color.GRAY;
			else if (colorSelector.getSelectedItem().equals("Magenta"))
				ballColor = Color.MAGENTA;
			else if (colorSelector.getSelectedItem().equals("Orange"))
				ballColor = Color.ORANGE;
			else if (colorSelector.getSelectedItem().equals("Pink"))
				ballColor = Color.PINK;
			else if (colorSelector.getSelectedItem().equals("Random")) {
				int r = (int) (256 * Math.random());
				int g = (int) (256 * Math.random());
				int b = (int) (256 * Math.random());
				ballColor = new Color(r, g, b);
			}
			updateGUI();
		} else if (source == mBox)
			if (mBox.getSelectedItem().equals("Enabled"))
				musicOn = true;
			else
				musicOn = false;
		else if (source == aBox)
			if (aBox.getSelectedItem().equals("Enabled"))
				isAntialiased = true;
			else
				isAntialiased = false;
		else if (source == cancel)
			bb5.moveBackScreen();
		else if (source == apply) {
			if (colorSelector.getSelectedItem().equals("Custom")) {
				boolean shown = false;
				try {
					if (!customR.getText().isEmpty() && !customG.getText().isEmpty() && !customB.getText().isEmpty()) {
						int r = Integer.parseInt(customR.getText());
						int g = Integer.parseInt(customG.getText());
						int b = Integer.parseInt(customB.getText());
						if (r < 0)
							r = 0;
						else if (r > 255)
							r = 255;
						if (g < 0)
							g = 0;
						else if (g > 255)
							g = 255;
						if (b < 0)
							b = 0;
						else if (b > 255)
							b = 255;
						ballColor = new Color(r, g, b);
						currentSettings = new SettingsSave(musicOn, isAntialiased, ballColor, isCustomColor,
								bb5.getUnlockedLevels(), dynamicBallColor);
						bb5.saveSettings(currentSettings);
						bb5.moveBackScreen();
					} else {
						JOptionPane.showMessageDialog(this, "Empty Field or Improper Character", "Error",
								JOptionPane.ERROR_MESSAGE);
						shown = true;
					}
				} catch (NumberFormatException nfe) {
					if (!shown)
						JOptionPane.showMessageDialog(this, "Empty Field or Improper Character", "Error",
								JOptionPane.ERROR_MESSAGE);
				}
			} else {
				bb5.saveSettings(currentSettings);
				bb5.moveBackScreen();
			}
		}

		currentSettings = new SettingsSave(musicOn, isAntialiased, ballColor, isCustomColor, bb5.getUnlockedLevels(),
				dynamicBallColor);
		apply.setEnabled(!sameSettings());
	}

	private void updateGUI() {
		removeAll();
		makeLayout();
		revalidate();
		repaint();
		updateUI();

		if (isCustomColor) {
			customR.setText(Integer.toString(initSettings.getBallColor().getRed()));
			customG.setText(Integer.toString(initSettings.getBallColor().getGreen()));
			customB.setText(Integer.toString(initSettings.getBallColor().getBlue()));
		}
		apply.setEnabled(!sameSettings());
	}

	private void makeLayout() {
		Font font = new Font("Segoe UI", Font.PLAIN, 16);

		final JLabel aLabel = new JLabel("Antialiasing: ");
		aLabel.setFont(font);
		aLabel.setForeground(Color.BLACK);
		aLabel.setOpaque(true);
		aLabel.setPreferredSize(new Dimension(10, 10));

		JLabel mLabel = new JLabel("Music: ");
		mLabel.setFont(font);
		mLabel.setForeground(Color.BLACK);
		// aLabel.setBackground(Color.RED);
		mLabel.setPreferredSize(aLabel.getPreferredSize());

		GridBagConstraints gbc = new GridBagConstraints();
		JPanel containerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		containerPane.setOpaque(false);
		int fontSize = 16;

		int width = 150;

		JLabel customLabel = new JLabelWithFont(" ", fontSize);

		aBox.setFont(aBox.getFont().deriveFont((float) fontSize - 2));
		aBox.setPreferredSize(new Dimension(width, aBox.getPreferredSize().height));
		aBox.setFocusable(false);

		colorSelector.setFont(aBox.getFont().deriveFont((float) fontSize - 2));
		colorSelector.setPreferredSize(new Dimension(width, aBox.getPreferredSize().height));
		colorSelector.setFocusable(false);

		mBox.setFont(aBox.getFont().deriveFont((float) fontSize - 2));
		mBox.setPreferredSize(new Dimension(width, aBox.getPreferredSize().height));
		mBox.setFocusable(false);

		JPanel settingsPane = new JPanel(new GridBagLayout());
		settingsPane.setOpaque(false);
		settingsPane.setBorder(new EmptyBorder(0, 5, 5, 5));

		// Settings Placement
		int ypad = 15;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 10;
		gbc.ipady = ypad;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		settingsPane.add(new JLabelWithFont("Antialias Graphics: ", fontSize), gbc);
		gbc.gridx = 1;
		gbc.ipady = 0;
		gbc.ipadx = 50;
		settingsPane.add(aBox, gbc);
		gbc.ipadx = 0;

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.ipady = ypad;
		settingsPane.add(new JLabelWithFont("Ball Color: ", fontSize), gbc);
		gbc.gridx = 1;
		gbc.ipady = 0;
		settingsPane.add(colorSelector, gbc);

		if (isCustomColor) {
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.ipady = ypad;
			settingsPane.add(customLabel, gbc);
			gbc.gridx = 1;
			gbc.ipady = 0;

			JPanel customPane = new JPanel();
			customPane.setOpaque(false);
			GridBagConstraints customGBC = new GridBagConstraints();
			customPane.setLayout(new GridBagLayout());

			customGBC.gridx = 0;
			customGBC.gridy = 0;
			customGBC.ipady = 5;
			customGBC.ipadx = 2;
			customPane.add(new JLabelWithFont("R: ", fontSize), customGBC);
			customGBC.gridx = 1;
			customGBC.ipady = 0;
			customPane.add(customR, customGBC);
			customR.setDocument(new LimitTextField(3));

			customGBC.gridx = 0;
			customGBC.gridy = 1;
			customGBC.ipady = 5;
			customPane.add(new JLabelWithFont("G: ", fontSize), customGBC);
			customGBC.gridx = 1;
			customGBC.ipady = 0;
			customPane.add(customG, customGBC);
			customG.setDocument(new LimitTextField(3));

			customGBC.gridx = 0;
			customGBC.gridy = 2;
			customGBC.ipady = 5;
			customPane.add(new JLabelWithFont("B: ", fontSize), customGBC);
			customGBC.gridx = 1;
			customGBC.ipady = 0;
			customPane.add(customB, customGBC);
			customB.setDocument(new LimitTextField(3));

			settingsPane.add(customPane, gbc);
		}

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.ipady = ypad;
		settingsPane.add(new JLabelWithFont("Music: ", fontSize), gbc);
		gbc.gridx = 1;
		gbc.ipady = 0;
		settingsPane.add(mBox, gbc);
		gbc.gridx = 0;

		containerPane.add(settingsPane);
		JScrollPane jsp = new JScrollPane(containerPane);
		jsp.setBorder(null);
		jsp.getVerticalScrollBar().setUnitIncrement(4);
		jsp.setBackground(getBackground());
		jsp.getViewport().setBackground(getBackground());
		
		add(jsp, BorderLayout.CENTER);

		createButtons();
	}

	private void createButtons() {
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPane.setOpaque(false);
		cancel.setFocusable(false);
		cancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cancel.setOpaque(false);
		cancel.setPreferredSize(new Dimension(100, 28));
		apply.setFocusable(false);
		apply.setFont(cancel.getFont());
		apply.setOpaque(false);
		apply.setPreferredSize(cancel.getPreferredSize());
		apply.setEnabled(!sameSettings());
		buttonPane.add(apply);
		buttonPane.add(cancel);
		add(buttonPane, BorderLayout.SOUTH);
	}

	private boolean sameSettings() {
		if (isCustomColor) {
			boolean sameOptions = initSettings.equals(currentSettings);
			boolean sameValues = false;
			try {
				int initR = initSettings.getBallColor().getRed();
				int initG = initSettings.getBallColor().getGreen();
				int initB = initSettings.getBallColor().getBlue();

				int currentR = Integer.parseInt(customR.getText());
				int currentG = Integer.parseInt(customG.getText());
				int currentB = Integer.parseInt(customB.getText());
				sameValues = (initR == currentR && initG == currentG && initB == currentB);
				if (!sameOptions)
					if (!initSettings.getBallColor().equals(ballColor) && initSettings.isAntialiased() == isAntialiased
							&& initSettings.isMusicOn() == musicOn && sameValues)
						sameOptions = true;
			} catch (NumberFormatException nfe) {
				sameValues = false;
			}
			return sameOptions && sameValues;
		} else
			return initSettings.equals(currentSettings);
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			initSettings = bb5.getCurrentSettings();
			setSettingVars();
		}
		updateGUI();
	}

	private void setSettingVars() {
		isAntialiased = initSettings.isAntialiased();
		musicOn = initSettings.isMusicOn();
		ballColor = initSettings.getBallColor();
		isCustomColor = initSettings.isCustomColor();
		dynamicBallColor = initSettings.changesColors();

		if (isAntialiased)
			aBox.setSelectedItem("Enabled");
		else
			aBox.setSelectedItem("Disabled");

		if (musicOn)
			mBox.setSelectedItem("Enabled");
		else
			mBox.setSelectedItem("Disabled");

		if (!isCustomColor && !dynamicBallColor) {
			if (ballColor.equals(Color.BLUE))
				colorSelector.setSelectedItem("Blue");
			else if (ballColor.equals(Color.BLACK))
				colorSelector.setSelectedItem("Black");
			else if (ballColor.equals(Color.YELLOW))
				colorSelector.setSelectedItem("Yellow");
			else if (ballColor.equals(Color.CYAN))
				colorSelector.setSelectedItem("Cyan");
			else if (ballColor.equals(Color.RED))
				colorSelector.setSelectedItem("Red");
			else if (ballColor.equals(Color.GREEN))
				colorSelector.setSelectedItem("Green");
			else if (ballColor.equals(Color.GRAY))
				colorSelector.setSelectedItem("Gray");
			else if (ballColor.equals(Color.MAGENTA))
				colorSelector.setSelectedItem("Magenta");
			else if (ballColor.equals(Color.ORANGE))
				colorSelector.setSelectedItem("Orange");
			else if (ballColor.equals(Color.PINK))
				colorSelector.setSelectedItem("Pink");
		}
		updateGUI();

		if (isCustomColor) {
			colorSelector.setSelectedItem("Custom");
			customR.setText("" + ballColor.getRed());
			customG.setText("" + ballColor.getGreen());
			customB.setText("" + ballColor.getBlue());
		} else if (dynamicBallColor)
			colorSelector.setSelectedItem("Dynamic");
	}

	public void setAllEnabled(boolean b) {
		aBox.setEnabled(b);
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			aBox.setSelectedItem("Enabled");
			aBox.setEnabled(false);
		}
		mBox.setEnabled(b);
		colorSelector.setEnabled(b);
		customR.setEnabled(b);
		customG.setEnabled(b);
		customB.setEnabled(b);
		cancel.setEnabled(b);
		if (b == true)
			apply.setEnabled(!sameSettings());
		else
			apply.setEnabled(b);
	}

	public boolean fieldHasFocus() {
		return fieldHasFocus;
	}
}