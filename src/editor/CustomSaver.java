package editor;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import resources.objects.GameObject;

public class CustomSaver implements Serializable {
	private static final long serialVersionUID = 2L;
	private ArrayList<GameObject> objs;
	private Dimension size;

	public CustomSaver(ArrayList<GameObject> objs, Dimension size) {
		this.objs = objs;
		this.size = size;
	}

	public ArrayList<GameObject> getAllObjects() {
		return objs;
	}

	public Dimension getSize() {
		return size;
	}
}
