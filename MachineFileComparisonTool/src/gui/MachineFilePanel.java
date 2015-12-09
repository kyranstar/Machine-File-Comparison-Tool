package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JScrollPane;

import logic.FileLoader;
import logic.MachineFile;

@SuppressWarnings("serial")
public class MachineFilePanel extends JScrollPane implements MouseListener {
	private JList<String> lines;
	private FileLoader fileLoader;
	// the data of the file
	private MachineFile machineFile;
	// the panel to compare with
	private MachineFilePanel linkedPanel;

	private List<Integer> differentLines;

	public MachineFilePanel(FileLoader loader) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.fileLoader = loader;

		lines = new JList<>();
		lines.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (differentLines != null && differentLines.contains(index)) {
					setBackground(Color.RED);
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

	public void setContents(String[] linesArr) {
		if (linesArr == null)
			return;
		lines.setListData(linesArr);
		machineFile = new MachineFile(linesArr);
		updateCompare();
		linkedPanel.updateCompare();
	}

	private void updateCompare() {
		if (linkedPanel == null || linkedPanel.machineFile == null || machineFile == null)
			return;

		differentLines = machineFile.getDifferentLines(linkedPanel.machineFile);
		repaint();
	}

	public int getSelectedIndex() {
		return lines.getSelectedIndex();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() >= 2) {
			setContents(fileLoader.pickFileAndGetLines(this));
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
