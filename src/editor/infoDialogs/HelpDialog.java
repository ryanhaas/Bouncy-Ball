package editor.infoDialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import editor.CustomEditor;

public class HelpDialog extends JDialog {
	private CustomEditor ce;

	public HelpDialog(CustomEditor ce) {
		super(ce.frame, "Help", Dialog.ModalityType.APPLICATION_MODAL);
		this.ce = ce;
		setPreferredSize(new Dimension(300, 400));

		JPanel container = new JPanel();

		pack();
		setVisible(true);
	}
}
