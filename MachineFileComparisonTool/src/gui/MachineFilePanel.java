package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import fileloading.FileLoader;
import fileloading.TextFile;

@SuppressWarnings("serial")
public class MachineFilePanel extends JScrollPane implements MouseListener {
	private MachineFile lines;
	private FileLoader fileLoader;
	// the panel to compare with
	private MachineFilePanel linkedPanel;

	List<Integer> differentLines;
	// change this title whenever loading a file
	private JLabel title;
	// change this title whenever file length changes
	private JLabel fileLength;

	public MachineFilePanel(FileLoader loader, JLabel title, JLabel fileLength) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.fileLoader = loader;
		this.title = title;
		this.fileLength = fileLength;

		lines = new MachineFile(this);

		addMouseListener(this);
		getViewport().add(lines);
	}

	public void setLinkedPanel(MachineFilePanel linkedPanel) {
		this.linkedPanel = linkedPanel;
	}

	public MachineFile getLines() {
		return lines;
	}

	/**
	 * Sets this file's contents to the given array
	 * 
	 * @param linesArr
	 */
	private void setContents(String[] linesArr) {
		if (linesArr == null)
			return;
		getModel().removeAllElements();
		for (String item : linesArr) {
			getModel().addElement(item);
		}
		updateCompare();
		linkedPanel.updateCompare();
		fileLength.setText(String.valueOf(linesArr.length));
	}

	public void deleteSelected() {
		if (getLines().getSelectedIndices().length > 0) {
			int[] tmp = getLines().getSelectedIndices();
			int[] selectedIndices = getLines().getSelectedIndices();

			for (int i = tmp.length - 1; i >= 0; i--) {
				selectedIndices = getLines().getSelectedIndices();
				System.out.println("Removing: " + selectedIndices[i]);
				getModel().remove(selectedIndices[i]);
			}
		}
		updateCompare();
	}

	public void setValue(int i, String line) {
		getModel().set(i, line);
		updateCompare();
	}

	/**
	 * Updates the comparison data with the linked panel
	 */
	private void updateCompare() {
		if (linkedPanel == null)
			return;

		linkedPanel.differentLines = differentLines = getLines().calculateDifferentLines(linkedPanel.getLines());
		repaint();
		linkedPanel.repaint();
	}

	private DefaultListModel<String> getModel() {
		return ((DefaultListModel<String>) getLines().getModel());
	}

	public String getFullText() {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < getModel().size(); i++) {
			text.append(getModel().getElementAt(i)).append('\n');
		}
		return text.toString();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			getLines().clearSelection();
			linkedPanel.getLines().clearSelection();
		} else if (e.getClickCount() >= 2) {
			askForAndLoadFile();
		} else {
			// if we haven't selected anything on the other side, select the
			// same lines
			if (linkedPanel.getLines().getSelectedIndices().length == 0) {
				linkedPanel.getLines().setSelectedIndices(getLines().getSelectedIndices());
			}
		}
	}

	/**
	 * Picks a file using {@link FileLoader} and then loads that file into this
	 * panel.
	 */
	private void askForAndLoadFile() {
		TextFile file = fileLoader.pickFileAndGetLines(this);
		if (file == null || file.getContents() == null || file.getTitle() == null) {
			return;
		}
		setContents(file.getContents());
		title.setToolTipText(file.getFilepath());
		title.setText(file.getTitle());
		fileLength.setText(String.valueOf(file.getLength()));
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
