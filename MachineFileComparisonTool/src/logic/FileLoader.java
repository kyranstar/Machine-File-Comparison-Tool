package logic;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Loads files and stores the location the file was retrieved from.
 * @author Kyran Adams
 *
 */
public class FileLoader {
	private File lastLocation = null;

	public String[] pickFileAndGetLines(Component parent) {
		JFileChooser fc = new JFileChooser(lastLocation);
		fc.showOpenDialog(parent);
		File file = fc.getSelectedFile();
		if (file == null){
			return null;
		}
		// store location
		lastLocation = file;
		return readLines(parent, file);

	}

	private String[] readLines(Component parent, File file) {
		try {
			// read lines from file
			Stream<String> fileLines = Files.readAllLines(file.toPath()).stream();

			// convert to array
			return fileLines.toArray(new IntFunction<String[]>() {
				@Override
				public String[] apply(int size) {
					return new String[size];
				}
			});
		} catch (IOException e) {
			JOptionPane.showMessageDialog(parent, "Error opening file");
			e.printStackTrace();
			return null;
		}
	}
}
