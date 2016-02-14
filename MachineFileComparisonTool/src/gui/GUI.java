package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;

import logic.FileLoader;

@SuppressWarnings("serial")
public class GUI extends JPanel {
	private static final String DELETE_ACTION_MAP_KEY = "delete";
	public static final String TITLE = "Machine File Comparison Tool";
	private MachineFilePanel leftFile;
	private MachineFilePanel rightFile;
	private FileLoader fileLoader = new FileLoader();

	public GUI() {
		// set the delay before tooltips pop up
		ToolTipManager.sharedInstance().setInitialDelay(200);
		
		// spaces are set as default so the title automatically has space in the
		// layout
		JLabel leftTitle = new JLabel(" ");
		leftTitle.setToolTipText("No source file loaded");
		JLabel rightTitle = new JLabel(" ");
		rightTitle.setToolTipText("No target file loaded");
		JLabel leftFileLength = new JLabel(" ");
		leftFileLength.setToolTipText("Lines in file");
		JLabel rightFileLength = new JLabel(" ");
		rightFileLength.setToolTipText("Lines in file");

		leftFile = new MachineFilePanel(fileLoader, leftTitle, leftFileLength);
		rightFile = new MachineFilePanel(fileLoader, rightTitle, rightFileLength);
		addDeleteBind();

		leftFile.setLinkedPanel(rightFile);
		rightFile.setLinkedPanel(leftFile);

		JSplitPane leftTitleFileLength = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTitle, leftFileLength);
		leftTitleFileLength.setResizeWeight(.9);
		JSplitPane rightTitleFileLength = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rightTitle, rightFileLength);
		rightTitleFileLength.setResizeWeight(.9);
		
		JSplitPane leftTitleFileSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftTitleFileLength, leftFile);
		JSplitPane rightTitleFileSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightTitleFileLength, rightFile);

		setLayout(new BorderLayout());
		JSplitPane panelSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTitleFileSplit, rightTitleFileSplit);
		panelSplit.setResizeWeight(.5f);
		JSplitPane upDownSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createToolbar(), panelSplit);
		add(BorderLayout.CENTER, upDownSplit);
	}

	private JPanel createToolbar() {
		JButton carryOverButton = new JButton("Carry over");
		carryOverButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightFile.setValue(rightFile.getSelectedIndex(), leftFile.getSelectedValue());
			}
		});
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileLoader.pickFileAndSave(rightFile.getFullText(), saveButton);
			}
		});
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new BorderLayout());
		toolbar.add(BorderLayout.CENTER, carryOverButton);
		toolbar.add(BorderLayout.EAST, saveButton);
		return toolbar;
	}

	private void addDeleteBind() {
		rightFile.setFocusable(true);
		rightFile.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				DELETE_ACTION_MAP_KEY);
		rightFile.getActionMap().put(DELETE_ACTION_MAP_KEY, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(rightFile.getSelectedIndex());
				rightFile.removeLine(rightFile.getSelectedIndex());
				System.out.println(rightFile.getSelectedIndex());
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(TITLE);
		frame.add(new GUI());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
