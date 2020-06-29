package editor.platforms;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BoostPlatform extends JPanel {
	private JTextField xField, yField, wField, hField, boostField;

	public BoostPlatform() {
		setOpaque(true);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(0, 5, 5, 5));

		Font f = new Font("", Font.PLAIN, 14);

		xField = new JTextField();
		xField.setFont(f);
		yField = new JTextField();
		yField.setFont(f);
		wField = new JTextField();
		wField.setFont(f);
		hField = new JTextField();
		hField.setFont(f);
		boostField = new JTextField();
		boostField.setFont(f);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 2, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = .05;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = .05;
		gbc.ipady = 10;
		JLabel xLabel = new JLabel("X Coord: ");
		xLabel.setFont(f);
		add(xLabel, gbc);
		gbc.weightx = .95;
		gbc.gridx = 1;

		gbc.weighty = .05;
		add(xField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = .05;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = .05;
		gbc.ipady = 10;
		JLabel yLabel = new JLabel("Y Coord: ");
		yLabel.setFont(f);
		add(yLabel, gbc);
		gbc.weightx = .95;
		gbc.gridx = 1;

		gbc.weighty = .05;
		add(yField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = .05;
		gbc.weightx = .05;
		gbc.ipady = 10;
		JLabel wLabel = new JLabel("Width: ");
		wLabel.setFont(f);
		add(wLabel, gbc);
		gbc.weightx = .95;
		gbc.gridx = 1;

		gbc.weighty = .05;
		add(wField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = .05;
		gbc.weightx = .05;
		gbc.ipady = 10;
		JLabel hLabel = new JLabel("Height: ");
		hLabel.setFont(f);
		add(hLabel, gbc);
		gbc.weightx = .95;

		gbc.gridx = 1;
		gbc.weighty = .05;
		add(hField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weighty = .05;
		gbc.weightx = .05;
		gbc.ipady = 10;
		JLabel bLabel = new JLabel("Boost: ");
		bLabel.setFont(f);
		add(bLabel, gbc);

		gbc.weightx = .95;
		gbc.gridx = 1;
		gbc.weighty = .05;
		add(boostField, gbc);
	}

	public JTextField[] getFields() {
		return new JTextField[] { xField, yField, wField, hField, boostField };
	}

	public void setValues(Object[] values) {
		xField.setText(Integer.toString((Integer) values[0]));
		yField.setText(Integer.toString((Integer) values[1]));
		wField.setText(Integer.toString((Integer) values[2]));
		hField.setText(Integer.toString((Integer) values[3]));
		boostField.setText(Double.toString((Double) values[4]));
	}
}
