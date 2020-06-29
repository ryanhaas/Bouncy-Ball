import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.*;

import resources.JLabelWithFont;
import resources.LimitTextField;
import resources.SettingsSave;

public class Settings extends JPanel implements ActionListener {
	private Environment env;
	private boolean showCustomColor = false;
	private boolean wasCustom = false;

	private String[] enabledDisabled = {"Enabled", "Disabled"};
	private String[] colorOptions = {"Blue", "Black", "Yellow", "Cyan", "Red", "Green", "Gray", "Magenta", "Random", "Custom"};
	private Color[] listOfColors = {Color.BLUE, Color.BLACK, Color.YELLOW, Color.CYAN, Color.RED, Color.GREEN,
			Color.GRAY, Color.MAGENTA};

	private JComboBox<String> antiSelector = new JComboBox<String>(enabledDisabled);
	private JComboBox<String> colorSelector = new JComboBox<String>(colorOptions);
	private JComboBox<String> musicSelector = new JComboBox<String>(enabledDisabled);

	private JTextField customR = new JTextField(10), customG = new JTextField(10), customB = new JTextField(10);

	private JButton cancel, apply;

	public Settings(Environment e) {
		env = e;
		setLocation(0, env.getMouseLine());
		setSize(new Dimension(env.getWidth(), env.getHeight() - env.getMouseLine()));
		setOpaque(true);
		setLayout(new BorderLayout());
		int inset = 10;
		setBorder(new EmptyBorder(inset, inset, inset, inset));

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				env.frame.requestFocus();
			}
		});

		customR.addFocusListener(unfocusedListener());
		customG.addFocusListener(unfocusedListener());
		customB.addFocusListener(unfocusedListener());

		checkSettings();
		makeLayout();
		addButtons();
	}

	private void addButtons() {
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBorder(new EmptyBorder(0, 5, 5, 10));
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setFocusable(false);
		buttonPane.add(cancel);
		apply = new JButton("Apply");
		apply.addActionListener(this);
		apply.setFocusable(false);
		buttonPane.add(apply);

		add(buttonPane, BorderLayout.CENTER);
	}

	private void makeLayout() {
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel containerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb = BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK), "Settings");
		tb.setTitleFont(tb.getTitleFont().deriveFont(22f));
		tb.setTitleJustification(TitledBorder.CENTER);
		containerPane.setBorder(tb);
		int fontSize = 16;

		JLabel customLabel = new JLabelWithFont("Custom Color: ", fontSize);

		antiSelector.setFont(antiSelector.getFont().deriveFont((float)fontSize - 2));
		antiSelector.setPreferredSize(new Dimension(125, antiSelector.getPreferredSize().height));
		antiSelector.setFocusable(false);

		colorSelector.setFont(antiSelector.getFont().deriveFont((float)fontSize - 2));
		colorSelector.setPreferredSize(new Dimension(125, antiSelector.getPreferredSize().height));
		colorSelector.setFocusable(false);
		colorSelector.addActionListener(this);

		musicSelector.setFont(antiSelector.getFont().deriveFont((float)fontSize - 2));
		musicSelector.setPreferredSize(new Dimension(125, antiSelector.getPreferredSize().height));
		musicSelector.setFocusable(false);

		JPanel settingsPane = new JPanel(new GridBagLayout());
		settingsPane.setBorder(new EmptyBorder(0, 5, 5 ,5));

		//Settings Placement
		int ypad = 15;
		gbc.gridx = 0; gbc.gridy = 0; gbc.ipadx = 10; gbc.ipady = ypad; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
		settingsPane.add(new JLabelWithFont("Antialias Graphics: ", fontSize), gbc);
		gbc.gridx = 1; gbc.ipady = 0;
		settingsPane.add(antiSelector, gbc);

		gbc.gridx = 0; gbc.gridy++; gbc.ipady = ypad;
		settingsPane.add(new JLabelWithFont("Ball Color: ", fontSize), gbc);
		gbc.gridx = 1; gbc.ipady = 0;
		settingsPane.add(colorSelector, gbc);

		if(showCustomColor) {
			gbc.gridx = 0; gbc.gridy++; gbc.ipady = ypad;
			settingsPane.add(customLabel, gbc);
			gbc.gridx = 1; gbc.ipady = 0;

			JPanel customPane = new JPanel();
			GridBagConstraints customGBC = new GridBagConstraints();
			customPane.setLayout(new GridBagLayout());

			customGBC.gridx = 0; customGBC.gridy = 0; customGBC.ipady = 5; customGBC.ipadx = 2;
			customPane.add(new JLabelWithFont("R: ", fontSize), customGBC);
			customGBC.gridx = 1; customGBC.ipady = 0;
			customPane.add(customR, customGBC);
			customR.setDocument(new LimitTextField(3));

			customGBC.gridx = 0; customGBC.gridy = 1; customGBC.ipady = 5;
			customPane.add(new JLabelWithFont("G: ", fontSize), customGBC);
			customGBC.gridx = 1; customGBC.ipady = 0;
			customPane.add(customG, customGBC);
			customG.setDocument(new LimitTextField(3));

			customGBC.gridx = 0; customGBC.gridy = 2; customGBC.ipady = 5;
			customPane.add(new JLabelWithFont("B: ", fontSize), customGBC);
			customGBC.gridx = 1; customGBC.ipady = 0;
			customPane.add(customB, customGBC);
			customB.setDocument(new LimitTextField(3));

			if(wasCustom) {
				customR.setText(Integer.toString(env.getBallColor().getRed()));
				customG.setText(Integer.toString(env.getBallColor().getGreen()));
				customB.setText(Integer.toString(env.getBallColor().getBlue()));
			}

			settingsPane.add(customPane, gbc);
		}

		gbc.gridx = 0; gbc.gridy++; gbc.ipady = ypad;
		settingsPane.add(new JLabelWithFont("Music: ", fontSize), gbc);
		gbc.gridx = 1; gbc.ipady = 0;
		settingsPane.add(musicSelector, gbc);

		containerPane.add(settingsPane);
		add(containerPane, BorderLayout.NORTH);
	}

	private void checkSettings() {
		Color bC = env.getBallColor();
		if(!env.getCustom()) {
			for(int x = 0; x < listOfColors.length; x++) {
				if((bC.getRed() == listOfColors[x].getRed()) && (bC.getGreen() == listOfColors[x].getGreen()) &&
						(bC.getBlue() == listOfColors[x].getBlue())) {
					colorSelector.setSelectedIndex(x);
					break;
				}
				else
					colorSelector.setSelectedIndex(colorOptions.length - 2);
			}
		}
		else {
			showCustomColor = true;
			wasCustom = true;
			colorSelector.setSelectedIndex(colorOptions.length - 1);
		}

		if(env.getAntialias()) antiSelector.setSelectedIndex(0);
		else antiSelector.setSelectedIndex(1);

		if(env.getMusic()) musicSelector.setSelectedIndex(0);
		else musicSelector.setSelectedIndex(1);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == colorSelector) {
			if(colorSelector.getSelectedItem().equals(colorOptions[colorOptions.length - 1])) {
				showCustomColor = true;
				removeAll();
				makeLayout();
				addButtons();
				colorSelector.setSelectedIndex(colorOptions.length-1);
				revalidate();
			}
			else {
				showCustomColor = false;
				int index = colorSelector.getSelectedIndex();
				removeAll();
				makeLayout();
				addButtons();
				colorSelector.setSelectedIndex(index);
				revalidate();
			}
		}
		else if(source == cancel)
			env.moveBackScreen();
		else if(source == apply) {
			boolean keepGoing = true;

			if(antiSelector.getSelectedIndex() == 0)
				env.setAntialias(true);
			else
				env.setAntialias(false);

			if(colorSelector.getSelectedIndex() == colorOptions.length - 2) {
				Random random = new Random();
				int r = random.nextInt(256);
				int g = random.nextInt(256);
				int b = random.nextInt(256);
				env.setBallColor(new Color(r, g, b));
			}
			else if(colorSelector.getSelectedIndex() == colorOptions.length - 1) {
				try {
					int r, g, b;
					if(customR.getText().equals("")) r = 0;
					else r = Integer.parseInt(customR.getText());

					if(customG.getText().equals("")) g = 0;
					else g = Integer.parseInt(customG.getText());

					if(customB.getText().equals("")) b = 0;
					else b = Integer.parseInt(customB.getText());

					if(r > 255) r = 255; if(r < 0) r = 0;
					if(g > 255) g = 255; if(g < 0) g = 0;
					if(b > 255) b = 255; if(b < 0) b = 0;

					env.setBallColor(new Color(r, g, b));
				} catch(NumberFormatException nfe) {
					JOptionPane.showMessageDialog(this, "Improper Character in Custom Field", "Error Creating Ball"
							, JOptionPane.ERROR_MESSAGE);
					keepGoing = false;
				}
			}
			else
				env.setBallColor(listOfColors[colorSelector.getSelectedIndex()]);

			if(keepGoing) {
				if(musicSelector.getSelectedIndex() == 0)
					env.setMusic(true);
				else
					env.setMusic(false);

				if(!showCustomColor)
					env.saveSettings(new SettingsSave(env.getAntialias(), env.getBallColor(), env.getMusic(), false));
				else
					env.saveSettings(new SettingsSave(env.getAntialias(), env.getBallColor(), env.getMusic(), true));
				env.moveBackScreen();
			}
		}
	}

	private FocusAdapter unfocusedListener() {
		return new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				env.unfocusedException = true;
			}
			public void focusLost(FocusEvent e) {
				env.unfocusedException = false;
				env.outOfWindow = true;
			}
		};
	}
}