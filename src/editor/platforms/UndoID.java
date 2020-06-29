package editor.platforms;

public class UndoID {
	private String id;
	private int index;

	public static final String ADDED_OBJECT = "Added Object";
	public static final String REMOVED_OBJECT = "Removed Object";
	public static final String MOVED_OBJECT = "Moved Object";
	public static final String CHANGED_OBJECT = "Changed Object";

	public UndoID(String id, int index) {
		this.id = id;
		this.index = index;
	}

	public String getID() {
		return id;
	}

	public int getIndex() {
		return index;
	}
}
