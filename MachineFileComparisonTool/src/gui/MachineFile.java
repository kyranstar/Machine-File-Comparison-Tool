package gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class MachineFile extends JList<String> {
	public MachineFile(final MachineFilePanel panel) {
		DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
		defaultListModel.addElement("Double click to load file");
		setModel(defaultListModel);

		// file numbers in differentLines are highlighted red
		setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (panel.differentLines != null && panel.differentLines.contains(index)) {
					if (isSelected) {
						setBackground(Color.ORANGE);
					} else {
						setBackground(Color.RED);
					}
				}
				return this;
			}
		});

		addMouseListener(panel);
	}

	/**
	 * Gives a list of lines with the same index that do not satisfy linesEqual
	 * between this panel and the linkedPanel.
	 * 
	 * @return
	 */
	public List<Integer> calculateDifferentLines(MachineFile other) {
		List<Integer> differentLines = new ArrayList<>();
		int end = Math.min(getDefaultModel().size(), other.getDefaultModel().size());
		for (int i = 0; i < end; i++) {
			if (!linesEqual(getDefaultModel().getElementAt(i), other.getDefaultModel().getElementAt(i))) {
				differentLines.add(i);
			}
		}
		// add unmatched lines
		for (int i = end; i < getDefaultModel().size(); i++) {
			differentLines.add(i);
		}
		for (int i = end; i < other.getDefaultModel().size(); i++) {
			differentLines.add(i);
		}

		return Collections.unmodifiableList(differentLines);
	}

	public String getElement(int i) {
		return getDefaultModel().getElementAt(i);
	}
	
	private DefaultListModel<String> getDefaultModel() {
		return ((DefaultListModel<String>) getModel());
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
