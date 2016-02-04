package logic;

import java.io.File;
import java.util.Arrays;

public class TextFile {
	private final String title;
	private final String filepath;
	private final String[] contents;

	public TextFile(File f, String[] contents) {
		this.title = f.getName();
		this.filepath = f.getAbsolutePath();
		this.contents = contents;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return a copy of the length of this array
	 */
	public String[] getContents() {
		if (contents == null) {
			return null;
		}
		return Arrays.copyOf(contents, contents.length);
	}

	public String getFilepath() {
		return filepath;
	}
}
