package screens;

import java.awt.Color;
import java.io.Serializable;

public class SettingsSave implements Serializable {
	private static final long serialVersionUID = 2L;

	private boolean musicOn;
	private boolean antialiased;
	private Color ballColor;
	private boolean isCustomColor;
	private boolean changingColor;
	private int[] unlockedLevels;

	public SettingsSave(boolean musicOn, boolean antialiased, Color ballColor, boolean isCustomColor,
			int[] unlockedLevels, boolean changingColor) {
		this.musicOn = musicOn;
		this.antialiased = antialiased;
		this.ballColor = ballColor;
		this.isCustomColor = isCustomColor;
		this.unlockedLevels = unlockedLevels;
		this.changingColor = changingColor;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public boolean isAntialiased() {
		return antialiased;
	}

	public Color getBallColor() {
		return ballColor;
	}

	public boolean isCustomColor() {
		return isCustomColor;
	}

	public boolean equals(Object o) {
		if (o instanceof SettingsSave) {
			SettingsSave other = (SettingsSave) o;
			return musicOn == other.isMusicOn() && antialiased == other.isAntialiased()
					&& ballColor.equals(other.getBallColor()) && isCustomColor == other.isCustomColor()
					&& changingColor == other.changesColors();
		}
		else
			return false;
	}

	public int[] unlockedLevels() {
		return unlockedLevels;
	}

	public String toString() {
		return "antialias: " + antialiased + ", music: " + musicOn + ", ballColor: " + ballColor + ", custom: "
				+ isCustomColor + ", unlockedLevels: " + unlockedLevels;
	}

	public boolean changesColors() {
		return changingColor;
	}
}
