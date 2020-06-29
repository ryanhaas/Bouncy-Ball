package resources;

import java.awt.*;

public class ColorTransitioner {
	private Color[] allColors;
	private int counter = 1;
	private Color firstColor;
	private Color secondColor;
	private Color currentColor;

	private int r, g, b;

	private int speed;

	public ColorTransitioner(Color c1, Color c2) {
		allColors = new Color[] { c1, c2 };
		firstColor = c1;
		secondColor = c2;

		r = firstColor.getRed();
		g = firstColor.getGreen();
		b = firstColor.getBlue();

		currentColor = new Color(r, g, b);

		speed = 2;
	}

	public ColorTransitioner(Color[] colors) {
		allColors = colors;
		firstColor = allColors[counter];
		secondColor = allColors[counter++];

		r = firstColor.getRed();
		g = firstColor.getGreen();
		b = firstColor.getBlue();

		currentColor = new Color(r, g, b);

		speed = 2;
	}

	public ColorTransitioner(Color c1, Color c2, int speed) {
		allColors = new Color[] { c1, c2 };
		firstColor = c1;
		secondColor = c2;

		r = firstColor.getRed();
		g = firstColor.getGreen();
		b = firstColor.getBlue();

		currentColor = new Color(r, g, b);

		this.speed = speed;
	}

	public ColorTransitioner(Color[] colors, int speed) {
		allColors = colors;
		firstColor = allColors[counter];
		secondColor = allColors[counter++];

		r = firstColor.getRed();
		g = firstColor.getGreen();
		b = firstColor.getBlue();

		currentColor = new Color(r, g, b);

		this.speed = speed;
	}

	public void transitionColor() {
		// R
		if (currentColor.getRed() - speed > secondColor.getRed())
			r -= speed;
		else if (currentColor.getRed() + speed < secondColor.getRed())
			r += speed;
		else
			r += 0;

		// G
		if (currentColor.getGreen() - speed > secondColor.getGreen())
			g -= speed;
		else if (currentColor.getGreen() + speed < secondColor.getGreen())
			g += speed;
		else
			g += 0;

		// B
		if (currentColor.getBlue() - speed > secondColor.getBlue())
			b -= speed;
		else if (currentColor.getBlue() + speed < secondColor.getBlue())
			b += speed;
		else
			b += 0;

		currentColor = new Color(r, g, b);

		if (equalColors(speed)) {
			if (counter < allColors.length - 1)
				counter++;
			else
				counter = 0;
			secondColor = allColors[counter];
		}
	}

	public Color getColor() {
		return currentColor;
	}

	public Color[] getColorArray() {
		return allColors;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	// Fixes going out of 255
	private boolean equalColors(double speed) {
		boolean sameRed = currentColor.getRed() >= secondColor.getRed() - speed
				&& currentColor.getRed() <= secondColor.getRed() + speed;
		boolean sameGreen = currentColor.getGreen() >= secondColor.getGreen() - speed
				&& currentColor.getGreen() <= secondColor.getGreen() + speed;
		boolean sameBlue = currentColor.getBlue() >= secondColor.getBlue() - speed
				&& currentColor.getBlue() <= secondColor.getBlue() + speed;

		return sameRed && sameGreen && sameBlue;
	}
}
