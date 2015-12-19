package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import logic.FileLoader;

@SuppressWarnings("serial")
public class GUI extends JPanel{
	private static final String DELETE_ACTION_MAP_KEY = "delete";
	public static final String TITLE = "Machine File Comparison Tool";
	private MachineFilePanel leftFile;
	private MachineFilePanel rightFile;
	private FileLoader fileLoader = new FileLoader();

	public GUI() {
		leftFile = new MachineFilePanel(fileLoader);
		rightFile = new MachineFilePanel(fileLoader);
		addDeleteBind();
		
		leftFile.setLinkedPanel(rightFile);
		rightFile.setLinkedPanel(leftFile);
		
		setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftFile, rightFile);
		split.setResizeWeight(.5f);
		JSplitPane upDownSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createToolbar(), split);
		add(BorderLayout.CENTER, upDownSplit);
	}

	private JPanel createToolbar() {
		JButton undoButton = new JButton("Undo");
		JButton carryOverButton = new JButton("Carry over");
		carryOverButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				rightFile.setValue(rightFile.getSelectedIndex(), leftFile.getSelectedValue());
			}});
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fileLoader.pickFileAndSave(rightFile.getFullText(), saveButton);
			}
		});
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new BorderLayout());
		toolbar.add(BorderLayout.WEST, undoButton);
		toolbar.add(BorderLayout.CENTER, carryOverButton);
		toolbar.add(BorderLayout.EAST, saveButton);
		return toolbar;
	}

	private void addDeleteBind() {
		rightFile.setFocusable(true);
		rightFile.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0), DELETE_ACTION_MAP_KEY);
		rightFile.getActionMap().put(DELETE_ACTION_MAP_KEY,new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				rightFile.removeLine(rightFile.getSelectedIndex());
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
