package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import logic.FileLoader;
import logic.MachineFile;
import logic.TextFile;

@SuppressWarnings("serial")
public class MachineFilePanel extends JScrollPane implements MouseListener {
	private JList<String> lines;
	private FileLoader fileLoader;
	// the data of the file
	private MachineFile machineFile;
	// the panel to compare with
	private MachineFilePanel linkedPanel;

	private List<Integer> differentLines;
	// change this title whenever loading a file
	private JLabel title;

	public MachineFilePanel(FileLoader loader, JLabel title) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.fileLoader = loader;
		this.title = title;

		lines = new JList<>();
		lines.setListData(new String[] { "Double click to load file" });
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

	public void setLinkedPanel(MachineFilePanel linkedPanel) {
		this.linkedPanel = linkedPanel;
	}

	/**
	 * Sets this file's contents to the given array
	 * 
	 * @param linesArr
	 */
	public void setContents(String[] linesArr) {
		if (linesArr == null)
			return;
		lines.setListData(linesArr);
		machineFile = new MachineFile(linesArr);
		updateCompare();
		linkedPanel.updateCompare();
	}

	/**
	 * Updates the comparison data with the linked panel
	 */
	private void updateCompare() {
		if (linkedPanel == null || linkedPanel.machineFile == null || machineFile == null)
			return;

		differentLines = machineFile.getDifferentLines(linkedPanel.machineFile);
		repaint();
	}

	public int getSelectedIndex() {
		return lines.getSelectedIndex();
	}

	public String getSelectedValue() {
		return lines.getSelectedValue();
	}

	/**
	 * Sets a single row of the data
	 * 
	 * @param index
	 * @param value
	 */
	public void setValue(int index, String value) {
		ListModel<String> model = lines.getModel();

		if (index < 0 || index >= model.getSize())
			return;

		String[] lineArr = new String[model.getSize()];
		for (int i = 0; i < model.getSize(); i++) {
			lineArr[i] = model.getElementAt(i);
		}

		lineArr[index] = value;
		setContents(lineArr);
	}

	public void removeLine(int index) {
		ListModel<String> model = lines.getModel();

		if (index < 0 || index >= model.getSize())
			return;

		String[] lineArr = new String[model.getSize() - 1];
		for (int i = 0; i < lineArr.length; i++) {
			if (i < index) {
				lineArr[i] = model.getElementAt(i);
			} else {
				lineArr[i] = model.getElementAt(i + 1);
			}
		}

		setContents(lineArr);
	}

	public String getFullText() {
		ListModel<String> model = lines.getModel();
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < model.getSize(); i++) {
			text.append(model.getElementAt(i)).append('\n');
		}
		return text.toString();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			TextFile file = fileLoader.pickFileAndGetLines(this);
			if (file == null || file.getContents() == null || file.getTitle() == null) {
				return;
			}
			setContents(file.getContents());
			title.setToolTipText(file.getFilepath());
			title.setText(file.getTitle());
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
}
