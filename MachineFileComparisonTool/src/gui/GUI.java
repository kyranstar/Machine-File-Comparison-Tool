package gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;

import fileloading.FileLoader;

@SuppressWarnings("serial")
public class GUI extends JPanel {
	private static final String DELETE_ACTION_MAP_KEY = "delete";
	public static final String TITLE = "Machine File Comparison Tool";
	protected static final String VERSION_NUMBER = "1.0";
	private static Image icon = new ImageIcon(GUI.class.getResource("/res/icon.gif")).getImage();
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
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GUI.this,
						"Developed by Kyran Adams, Student of Tesla STEM\nVersion " + VERSION_NUMBER, "About",
						JOptionPane.OK_OPTION, new ImageIcon(icon));
			}
		});

		JButton carryOverButton = new JButton("Carry over");
		carryOverButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] leftSelectedIndices = leftFile.getLines().getSelectedIndices();
				int[] rightSelectedIndices = rightFile.getLines().getSelectedIndices();
				for (int i = 0; i < leftSelectedIndices.length && i < rightSelectedIndices.length; i++) {
					rightFile.setValue(rightSelectedIndices[i], leftFile.getLines().getElement(leftSelectedIndices[i]));
				}
			}
		});
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileLoader.pickFileAndSave(rightFile.getFullText(), saveButton);
			}
		});
		final JPanel toolbar = new JPanel();
		toolbar.setLayout(new BorderLayout());
		toolbar.add(BorderLayout.WEST, aboutButton);
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
				rightFile.deleteSelected();
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(TITLE);
		frame.add(new GUI());
		setIconImages(frame);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void setIconImages(JFrame frame) {
		// on windows 7, icon on upper right of app must be 16x16, while the
		// icon on the taskbar at the bottom must be 32x32 according to
		// http://stackoverflow.com/questions/15004874/use-different-icons-in-jframe-and-windows-taskbar
		BufferedImage upperLeftIcon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Image scaledTaskbarIcon = GUI.icon.getScaledInstance(32, 32, BufferedImage.SCALE_SMOOTH);
		
		frame.setIconImages(Arrays.asList(upperLeftIcon, scaledTaskbarIcon));
	}

}
