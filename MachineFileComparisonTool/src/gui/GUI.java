package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import logic.FileLoader;

@SuppressWarnings("serial")
public class GUI extends JPanel {
	public static final String TITLE = "Machine File Comparison Tool";
	private MachineFilePanel leftFile;
	private MachineFilePanel rightFile;
	private FileLoader fileLoader = new FileLoader();

	public GUI() {
		leftFile = new MachineFilePanel(fileLoader);
		rightFile = new MachineFilePanel(fileLoader);
		
		leftFile.setLinkedPanel(rightFile);
		rightFile.setLinkedPanel(leftFile);
		
		setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftFile, rightFile);
		split.setResizeWeight(.5f);
		add(BorderLayout.CENTER, split);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(TITLE);
		frame.add(new GUI());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
