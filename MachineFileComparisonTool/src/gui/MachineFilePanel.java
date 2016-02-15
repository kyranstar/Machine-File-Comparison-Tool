package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import logic.FileLoader;
import logic.TextFile;

@SuppressWarnings("serial")
public class MachineFilePanel extends JScrollPane implements MouseListener {
	private JList<String> lines;
	private FileLoader fileLoader;
	// the panel to compare with
	private MachineFilePanel linkedPanel;

	private List<Integer> differentLines;
	// change this title whenever loading a file
	private JLabel title;
	// change this title whenever file length changes
	private JLabel fileLength;

	public MachineFilePanel(FileLoader loader, JLabel title, JLabel fileLength) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.fileLoader = loader;
		this.title = title;
		this.fileLength = fileLength;

		lines = new JList<>();
		DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
		defaultListModel.addElement("Double click to load file");
		lines.setModel(defaultListModel);
		// file numbers in differentLines are highlighted red
		lines.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (differentLines != null && differentLines.contains(index)) {
					if (isSelected) {
						setBackground(Color.ORANGE);
					} else {
						setBackground(Color.RED);
					}
				}

				return this;
			}
		});
		addMouseListener(this);
		lines.addMouseListener(this);
		getViewport().add(lines);
	}

	public String getLine(int i) {
		return getModel().getElementAt(i);
	}

	public void setLinkedPanel(MachineFilePanel linkedPanel) {
		this.linkedPanel = linkedPanel;
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

	/**
	 * Updates the comparison data with the linked panel
	 */
	private void updateCompare() {
		if (linkedPanel == null)
			return;

		differentLines = getDifferentLines();
		repaint();

		linkedPanel.differentLines = linkedPanel.getDifferentLines();
		linkedPanel.repaint();
	}

	public int[] getSelectedIndices() {
		return lines.getSelectedIndices();
	}

	public void deleteSelected() {
		if (lines.getSelectedIndices().length > 0) {
			int[] tmp = lines.getSelectedIndices();
			int[] selectedIndices = lines.getSelectedIndices();

			for (int i = tmp.length - 1; i >= 0; i--) {
				selectedIndices = lines.getSelectedIndices();
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

	private DefaultListModel<String> getModel() {
		return ((DefaultListModel<String>) lines.getModel());
	}

	public String getFullText() {
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < getLineCount(); i++) {
			text.append(getModel().getElementAt(i)).append('\n');
		}
		return text.toString();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			lines.clearSelection();
			linkedPanel.lines.clearSelection();
		} else if (e.getClickCount() >= 2) {
			TextFile file = fileLoader.pickFileAndGetLines(this);
			if (file == null || file.getContents() == null || file.getTitle() == null) {
				return;
			}
			setContents(file.getContents());
			title.setToolTipText(file.getFilepath());
			title.setText(file.getTitle());
			fileLength.setText(String.valueOf(file.getLength()));
		} else {
			// if we haven't selected anything on the other side, select the
			// same lines
			if (linkedPanel.lines.getSelectedIndices().length == 0) {
				linkedPanel.lines.setSelectedIndices(lines.getSelectedIndices());
			}
		}
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

	/**
	 * Gives a list of lines with the same index that do not satisfy linesEqual
	 * between this panel and the linkedPanel
	 * 
	 * @return
	 */
	public List<Integer> getDifferentLines() {
		List<Integer> differentLines = new ArrayList<>();
		int end = Math.min(getLineCount(), linkedPanel.getLineCount());
		for (int i = 0; i < end; i++) {
			if (!linesEqual(getLine(i), linkedPanel.getLine(i))) {
				differentLines.add(i);
			}
		}
		// add unmatched lines
		for (int i = end; i < getLineCount(); i++) {
			differentLines.add(i);
		}
		for (int i = end; i < linkedPanel.getLineCount(); i++) {
			differentLines.add(i);
		}

		return differentLines;
	}

	private int getLineCount() {
		return getModel().getSize();
	}

	/**
	 * Checks whether two lines are "equal." It must account for slightly
	 * different spacing.
	 * 
	 * @param line
	 * @param line2
	 * @return
	 */
	private final static boolean linesEqual(String line, String line2) {
		String[] first = line.split("\\s+");
		String[] second = line2.split("\\s+");
		if (first.length != second.length) {
			return false;
		}
		for (int i = 0; i < first.length; i++) {
			if (!first[i].trim().equals(second[i].trim())) {
				return false;
			}
		}

		return true;

	}

}
